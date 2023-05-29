package com.alibaba.excel.write.handler.filter;

import com.alibaba.excel.exception.ExcelRuntimeException;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.handler.BasePipeFilter;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/28 15:52
 */
public class StartsWithFilter extends BasePipeFilter<Object, Object> {

    @Override
    public Object apply(Object value) {

        if (Objects.isNull(value)) {
            return "";
        }

        if (value instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<Object> collection = (Collection<Object>) value;
            for (String start : params()) {
                if (Objects.isNull(start)) {
                    continue;
                }
                for (Object coll : collection) {
                    if (Objects.isNull(coll)) {
                        continue;
                    }
                    String cel = (String) coll;
                    if (cel.startsWith(start)) {
                        return cel;
                    }
                }
            }
            return "没有匹配到结果";
        } else if (value instanceof String) {

            String col = (String) value;
            for (String start : params()) {
                if (col.startsWith(start)) {
                    return col;
                }
            }
            return "没有匹配到结果";
        } else {

            return "starts-with filter input object is not collection or string";
        }
    }
}
