package com.alibaba.excel.write.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 * pipeline filter
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/27 21:58
 */
public abstract class BasePipeFilter<T, R> implements PipeFilter<T, R> {

    private final List<String> filterParams = new ArrayList<>();

    protected int rowIndex;
    protected int columnIndex;
    protected boolean lastFilter = false;

    /**
     * filter名称
     *
     * @return 名称
     */
    protected abstract String filterName();

    /**
     * call apply
     * @param wrapper
     * @return
     */
    protected abstract PipeDataWrapper<R> callApply(PipeDataWrapper<T> wrapper);

    @Override
    public PipeDataWrapper<R> apply(PipeDataWrapper<T> wrapper) {
        PipeDataWrapper<R> dataWrapper = callApply(wrapper);
        if (isValidity(dataWrapper)) {
            return dataWrapper;
        }
        return PipeDataWrapper.error(errorPrefix() + "输出数据不能是集合或对象");
    }

    private boolean isValidity(PipeDataWrapper<R> apply) {
        Object data = apply.getData();
        if (Objects.nonNull(data)) {
            return data instanceof String || data instanceof Number;
        }
        return false;
    }

    /**
     * @return 错误信息前缀
     */
    protected String errorPrefix() {
        return String.format("第[%s]列,[%s]指令错误:", columnIndex + 1, filterName());
    }

    /**
     * filter 参数
     *
     * @return 参数集合
     */
    public List<String> params() {
        return filterParams;
    }

    /**
     * 添加参数
     *
     * @param params 参数
     * @return 过滤器
     */
    public BasePipeFilter<T, R> addParams(String... params) {
        params().addAll(Arrays.asList(params));
        return this;
    }

    /**
     * 设置单元格
     *
     * @param row    行下标
     * @param column 列下标
     * @return 过滤器
     */
    public BasePipeFilter<T, R> setCell(int row, int column) {
        this.rowIndex = row;
        this.columnIndex = column;
        return this;
    }

    public void setLastFilter(boolean lastFilter) {
        this.lastFilter = lastFilter;
    }

    @Override
    public boolean isLast() {
        return lastFilter;
    }


    /**
     * 验证
     *
     * @param wrapper 通道数据包装
     * @return 是否成功
     */
    protected boolean verify(PipeDataWrapper<T> wrapper) {
        return Objects.nonNull(wrapper) && wrapper.success();
    }
}
