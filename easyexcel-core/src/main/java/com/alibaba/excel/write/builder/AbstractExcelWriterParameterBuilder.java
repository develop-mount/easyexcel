package com.alibaba.excel.write.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;

import com.alibaba.excel.metadata.AbstractParameterBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteBasicParameter;
import icu.develop.expression.filter.BasePipeFilter;

/**
 * Build ExcelBuilder
 *
 * @author Jiaju Zhuang
 */
public abstract class AbstractExcelWriterParameterBuilder<T extends AbstractExcelWriterParameterBuilder,
    C extends WriteBasicParameter> extends AbstractParameterBuilder<T, C> {
    /**
     * Writes the head relative to the existing contents of the sheet. Indexes are zero-based.
     *
     * @param relativeHeadRowIndex
     * @return
     */
    public T relativeHeadRowIndex(Integer relativeHeadRowIndex) {
        parameter().setRelativeHeadRowIndex(relativeHeadRowIndex);
        return self();
    }

    /**
     * Need Head
     */
    public T needHead(Boolean needHead) {
        parameter().setNeedHead(needHead);
        return self();
    }

    /**
     * Custom write handler
     *
     * @param writeHandler
     * @return
     */
    public T registerWriteHandler(WriteHandler writeHandler) {
        if (parameter().getCustomWriteHandlerList() == null) {
            parameter().setCustomWriteHandlerList(new ArrayList<WriteHandler>());
        }
        parameter().getCustomWriteHandlerList().add(writeHandler);
        return self();
    }

    /**
     * Use the default style.Default is true.
     *
     * @param useDefaultStyle
     * @return
     */
    public T useDefaultStyle(Boolean useDefaultStyle) {
        parameter().setUseDefaultStyle(useDefaultStyle);
        return self();
    }

    /**
     * Whether to automatically merge headers.Default is true.
     *
     * @param automaticMergeHead
     * @return
     */
    public T automaticMergeHead(Boolean automaticMergeHead) {
        parameter().setAutomaticMergeHead(automaticMergeHead);
        return self();
    }

    /**
     * Ignore the custom columns.
     */
    public T excludeColumnIndexes(Collection<Integer> excludeColumnIndexes) {
        parameter().setExcludeColumnIndexes(excludeColumnIndexes);
        return self();
    }

    /**
     * Ignore the custom columns.
     *
     * @deprecated use {@link #excludeColumnFieldNames(Collection)}
     */
    public T excludeColumnFiledNames(Collection<String> excludeColumnFieldNames) {
        parameter().setExcludeColumnFieldNames(excludeColumnFieldNames);
        return self();
    }

    /**
     * Ignore the custom columns.
     */
    public T excludeColumnFieldNames(Collection<String> excludeColumnFieldNames) {
        parameter().setExcludeColumnFieldNames(excludeColumnFieldNames);
        return self();
    }

    /**
     * Only output the custom columns.
     */
    public T includeColumnIndexes(Collection<Integer> includeColumnIndexes) {
        parameter().setIncludeColumnIndexes(includeColumnIndexes);
        return self();
    }

    /**
     * Only output the custom columns.
     *
     * @deprecated use {@link  #includeColumnFieldNames(Collection)} spelling mistake
     */
    @Deprecated
    public T includeColumnFiledNames(Collection<String> includeColumnFieldNames) {
        parameter().setIncludeColumnFieldNames(includeColumnFieldNames);
        return self();
    }

    /**
     * Only output the custom columns.
     */
    public T includeColumnFieldNames(Collection<String> includeColumnFieldNames) {
        parameter().setIncludeColumnFieldNames(includeColumnFieldNames);
        return self();
    }

    /**
     * Data will be order by  {@link #includeColumnFieldNames} or  {@link #includeColumnIndexes}.
     * <p>
     * default is false.
     *
     * @since 3.3.0
     **/
    public T orderByIncludeColumn(Boolean orderByIncludeColumn) {
        parameter().setOrderByIncludeColumn(orderByIncludeColumn);
        return self();
    }

    /**
     * 注册 pipe filter
     *
     * @param name       pipe filter name
     * @param pipeFilter pipe filter
     */
    public T registerPipeFilterHandler(String name, Supplier<BasePipeFilter<Object, Object>> pipeFilter) {
        if (parameter().getCustomPipeFilterMap() == null) {
            parameter().setCustomPipeFilterMap(new HashMap<>(16));
        }
        if (Objects.isNull(name) || Objects.isNull(pipeFilter)) {
            return self();
        }
        parameter().getCustomPipeFilterMap().put(name.toLowerCase(), pipeFilter);
        return self();
    }
}
