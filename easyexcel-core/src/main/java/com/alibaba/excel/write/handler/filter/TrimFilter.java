package com.alibaba.excel.write.handler.filter;

import com.alibaba.excel.exception.ExcelRuntimeException;
import com.alibaba.excel.write.handler.BasePipeFilter;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:
 * trim pipe filter
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/26 8:34
 */
public class TrimFilter extends BasePipeFilter<Object, Object> {

    @Override
    public Object apply(Object value) {

        if (Objects.isNull(value)) {
            return "";
        }

        if (value instanceof String) {

            return value.toString().trim();
        } else if (value instanceof Collection) {
            //noinspection unchecked
            Collection<Object> valList = (Collection<Object>) value;
            return valList.stream().filter(Objects::nonNull).map(str -> str.toString().trim()).collect(Collectors.toList());
        }

        return "trim filter input object is not collection or string";
    }
}
