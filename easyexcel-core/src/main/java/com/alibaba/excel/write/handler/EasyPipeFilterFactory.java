package com.alibaba.excel.write.handler;

import com.alibaba.excel.exception.ExcelRuntimeException;
import com.alibaba.excel.write.handler.filter.CellRedFilter;
import com.alibaba.excel.write.handler.filter.FormulaFilter;
import com.alibaba.excel.write.handler.filter.WatchFilter;
import com.vevor.expression.filter.BasePipeFilter;
import com.vevor.expression.filter.PipeDataWrapper;
import com.vevor.expression.filter.PipeFilterFactory;
import com.vevor.expression.filter.PipeFilterPool;
import com.vevor.expression.filter.utils.PipeFilterUtils;
import com.vevor.expression.filter.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/27 22:19
 */
@Slf4j
public class EasyPipeFilterFactory extends PipeFilterFactory {

    protected int rowIndex;
    protected int columnIndex;
    protected String columnName;

    static {
        PipeFilterPool.INSTANCE.addPipeFilter("formula", FormulaFilter::new);
        PipeFilterPool.INSTANCE.addPipeFilter("cell-red", CellRedFilter::new);
        PipeFilterPool.INSTANCE.addPipeFilter("watch", WatchFilter::new);
    }

    /**
     * 创建管道过滤器
     *
     * @return 管道过滤器
     */
    public static EasyPipeFilterFactory createPipeFilter() {
        return new EasyPipeFilterFactory();
    }

    /**
     * 转换列名
     * @param column 列号
     * @return 列名
     */
    protected String convertColumnToName(int column){
        StringBuilder colIndex = new StringBuilder();
        while(column>=0){
            int i= column%26;
            colIndex.append((char)(i+65));
            column = column/26 -1;
        }
        return colIndex.reverse().toString();
    }

    /**
     * 注册管道过滤器
     *
     * @param name       filter name
     * @param pipeFilter pipe filer
     * @return factory
     */
    public EasyPipeFilterFactory registerPipeFilter(String name, Supplier<BasePipeFilter<Object, Object>> pipeFilter) {
        super.registerPipeFilter(name, pipeFilter);
        return this;
    }

    /**
     *
     * @param pipeFilterMap pipe filer map
     * @return
     */
    public EasyPipeFilterFactory customerPipeFilter(Map<String, Supplier<BasePipeFilter<Object, Object>>> pipeFilterMap) {
        PipeFilterPool.INSTANCE.addPipeFilter(pipeFilterMap);
        return this;
    }

    /**
     *
     * @param row    行下标
     * @param column 列下标
     * @return this
     */
    public EasyPipeFilterFactory setCell(int row, int column) {
        this.rowIndex = row;
        this.columnIndex = column;
        this.columnName = convertColumnToName(column);
        return this;
    }

    @Override
    public PipeDataWrapper<Object> apply(PipeDataWrapper<Object> value) {

        if (PipeFilterUtils.isEmpty(params())) {
            throw new ExcelRuntimeException("管道字符串格式不正确");
        }

        String variable = params().get(0);
        if (StringUtils.isBlank(variable)) {
            throw new ExcelRuntimeException("管道字符串格式不正确");
        }

        String[] pipeArray = PipeFilterUtils.getPipelines(variable);
        Objects.requireNonNull(pipeArray, "管道字符串格式不正确");
        if (pipeArray.length <= 1) {
            throw new ExcelRuntimeException("管道字符串格式不正确");
        }

        String variableName = pipeArray[0];

        List<BasePipeFilter<Object, Object>> pipeFilterList = new ArrayList<>();
        for (int i = 1; i < pipeArray.length; i++) {
            if (StringUtils.isBlank(pipeArray[i])) {
                continue;
            }

            String[] expressArray = PipeFilterUtils.getPipeFilter(pipeArray[i], 2);
            if (StringUtils.isBlank(expressArray[0])) {
                continue;
            }

            String filterName = PipeFilterUtils.trim(expressArray[0]).toLowerCase();
            if (StringUtils.isBlank(filterName)) {
                continue;
            }

            Supplier<BasePipeFilter<Object, Object>> supplier = PipeFilterPool.INSTANCE.getPipeFilter(filterName);
            if (Objects.isNull(supplier)) {
                throw new ExcelRuntimeException(String.format("没有[%s]的管道过滤器", filterName));
            }
            BasePipeFilter<Object, Object> pipeFilter = supplier.get();
            if (Objects.nonNull(pipeFilter)) {
                pipeFilterList.add(pipeFilter);
            }

            if (expressArray.length > 1 && StringUtils.isNotBlank(expressArray[1])) {
                String[] paramArray = PipeFilterUtils.getPipeFilterParams(PipeFilterUtils.trim(expressArray[1]));
                pipeFilter.addParams(paramArray);
            }
            pipeFilter.setNoticePrefix(String.format("[%s]列,[%s]变量的", columnName, variableName));
        }

        if (PipeFilterUtils.isEmpty(pipeFilterList)) {
            return value;
        }
        // 构建pipeline
        Function<PipeDataWrapper<Object>, PipeDataWrapper<Object>> currFilter = pipeFilterList.get(0);
        for (int i = 1; i < pipeFilterList.size(); i++) {
            currFilter = currFilter.andThen(pipeFilterList.get(i));
        }
        PipeDataWrapper<Object> dataWrapper = currFilter.apply(value);
        if (isValidity(dataWrapper)) {
            return dataWrapper;
        }
        String msg = String.format("第[%s]列,数据错误:变量值类型错误，不应该是%s", columnName, dataWrapper.getData().getClass().getSimpleName());
        log.debug(msg);
        return PipeDataWrapper.error(msg);
    }


    @Override
    protected String filterName() {
        return "factory";
    }
}
