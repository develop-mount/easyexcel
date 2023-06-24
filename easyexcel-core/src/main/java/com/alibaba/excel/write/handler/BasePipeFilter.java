package com.alibaba.excel.write.handler;

import java.util.*;

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
    private String variableName;
    protected int rowIndex;
    protected int columnIndex;

    /**
     * filter名称
     *
     * @return 名称
     */
    protected abstract String filterName();

    protected boolean isValidity(PipeDataWrapper<R> apply) {
        Object data = apply.getData();
        if (Objects.nonNull(data)) {
            return !(data instanceof Collection || data instanceof Map);
        }
        return true;
    }

    /**
     * @return 错误信息前缀
     */
    protected String errorPrefix() {
        return String.format("第[%s]列,[%s]变量的[%s]指令错误:", columnIndex + 1, variableName, filterName());
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

    /**
     * 设置变量名称
     * @param variableName 变量名称
     */
    public void setVariableName(String variableName) {
        this.variableName = variableName;
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
