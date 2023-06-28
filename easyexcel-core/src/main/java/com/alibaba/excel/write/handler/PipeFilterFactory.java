package com.alibaba.excel.write.handler;

import com.alibaba.excel.context.WriteContext;
import com.alibaba.excel.exception.ExcelRuntimeException;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.util.PipeFilterUtils;
import com.alibaba.excel.write.handler.filter.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/27 22:19
 */
public class PipeFilterFactory extends BasePipeFilter<Object, Object> {

    private static final Map<String, Supplier<BasePipeFilter<Object, Object>>> PIPE_FILTER_MAP = new HashMap<>();

    static {
        // 初始化内置管道过滤器
        PIPE_FILTER_MAP.put("trim", TrimFilter::new);
        PIPE_FILTER_MAP.put("equals", EqualsFilter::new);
        PIPE_FILTER_MAP.put("prior-equals", PriorEqualsFilter::new);
        PIPE_FILTER_MAP.put("ends-with", EndsWithFilter::new);
        PIPE_FILTER_MAP.put("prior-ends-with", PriorEndsWithFilter::new);
        PIPE_FILTER_MAP.put("starts-with", StartsWithFilter::new);
        PIPE_FILTER_MAP.put("prior-starts-with", PriorStartsWithFilter::new);
        PIPE_FILTER_MAP.put("pattern", PatternFilter::new);
        PIPE_FILTER_MAP.put("prior-pattern", PriorPatternFilter::new);
        PIPE_FILTER_MAP.put("date-format", DateFormatFilter::new);
        PIPE_FILTER_MAP.put("contains", ContainsFilter::new);
        PIPE_FILTER_MAP.put("prior-contains", PriorContainsFilter::new);
        PIPE_FILTER_MAP.put("list-index", ListIndexFilter::new);
        PIPE_FILTER_MAP.put("list-echo", ListEchoFilter::new);
        PIPE_FILTER_MAP.put("list-range", ListRangeFilter::new);
        PIPE_FILTER_MAP.put("echo", EchoFilter::new);
        PIPE_FILTER_MAP.put("condition-echo", ConditionEchoFilter::new);
        PIPE_FILTER_MAP.put("cal-add", AdditionFilter::new);
        PIPE_FILTER_MAP.put("cal-sub", SubtractionFilter::new);
        PIPE_FILTER_MAP.put("cal-mul", MultiplicationFilter::new);
        PIPE_FILTER_MAP.put("cal-div", DivisionFilter::new);
        PIPE_FILTER_MAP.put("substring", SubstringFilter::new);
        PIPE_FILTER_MAP.put("replace", ReplaceFilter::new);
        PIPE_FILTER_MAP.put("formula", FormulaFilter::new);
        PIPE_FILTER_MAP.put("max-size", MaxSizeFilter::new);
        PIPE_FILTER_MAP.put("must", MustFilter::new);
    }

    private PipeFilterFactory(WriteContext writeContext) {
        if (Objects.nonNull(writeContext)
            && Objects.nonNull(writeContext.writeWorkbookHolder())
            && Objects.nonNull(writeContext.writeWorkbookHolder().getWriteWorkbook())
            && !PipeFilterUtils.isEmpty(writeContext.writeWorkbookHolder().getWriteWorkbook().getCustomPipeFilterMap())) {
            PIPE_FILTER_MAP.putAll(writeContext.writeWorkbookHolder().getWriteWorkbook().getCustomPipeFilterMap());
        }
    }

    /**
     * 创建管道过滤器
     *
     * @param writeContext 写操作上下文
     * @return 管道过滤器
     */
    public static PipeFilterFactory createPipeFilter(WriteContext writeContext) {
        return new PipeFilterFactory(writeContext);
    }

    /**
     * 创建管道过滤器
     *
     * @return 管道过滤器
     */
    public static PipeFilterFactory createPipeFilter() {
        return new PipeFilterFactory(null);
    }

    /**
     * 注册管道过滤器
     *
     * @param name       filter name
     * @param pipeFilter pipe filer
     * @return factory
     */
    public PipeFilterFactory registerPipeFilter(String name, Supplier<BasePipeFilter<Object, Object>> pipeFilter) {
        PIPE_FILTER_MAP.put(name, pipeFilter);
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

            String[] expressArray = PipeFilterUtils.getPipeFilter(pipeArray[i]);
            if (StringUtils.isBlank(expressArray[0])) {
                continue;
            }

            String filterName = PipeFilterUtils.trim(expressArray[0]).toLowerCase();
            if (StringUtils.isBlank(filterName)) {
                continue;
            }

            Supplier<BasePipeFilter<Object, Object>> supplier = PIPE_FILTER_MAP.get(filterName);
            if (Objects.isNull(supplier)) {
                throw new ExcelRuntimeException(String.format("没有[%s]的管道过滤器", filterName));
            }
            BasePipeFilter<Object, Object> pipeFilter = supplier.get();
            if (Objects.nonNull(pipeFilter)) {
                pipeFilterList.add(pipeFilter);
            }
            pipeFilter.setCell(rowIndex, columnIndex);

            if (expressArray.length > 1 && StringUtils.isNotBlank(expressArray[1])) {
                String[] paramArray = PipeFilterUtils.getPipeFilterParams(PipeFilterUtils.trim(expressArray[1]));
                pipeFilter.addParams(paramArray);
            }

            pipeFilter.setVariableName(variableName);
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
        return PipeDataWrapper.error(String.format("第[%s]列,数据错误:%s", columnIndex + 1, "变量值不能为集合或Map"));
    }


    @Override
    protected String filterName() {
        return "factory";
    }
}
