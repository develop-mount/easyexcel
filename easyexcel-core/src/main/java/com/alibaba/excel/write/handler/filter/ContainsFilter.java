package com.alibaba.excel.write.handler.filter;

import com.alibaba.excel.util.PipeFilterUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.handler.BasePipeFilter;

import java.util.Collection;
import java.util.Objects;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/30 10:37
 */
public class ContainsFilter extends BasePipeFilter<Object, Object> {



    @Override
    public Object apply(Object value) {

        if (Objects.isNull(value)) {
            return "";
        }

        if (PipeFilterUtils.isEmpty(params())) {
            return value;
        }

        if (value instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<Object> collection = (Collection<Object>) value;
            for (String end : params()) {
                if (Objects.isNull(end)) {
                    continue;
                }
                for (Object col : collection) {
                    if (Objects.isNull(col)) {
                        continue;
                    }
                    String cel = (String) col;
                    if (cel.contains(end)) {
                        return col;
                    }
                }
            }
            return "没有匹配到结果";
        } else if (value instanceof String) {

            String col = (String) value;
            for (String start : params()) {
                if (StringUtils.isBlank(start)) {
                    continue;
                }
                if (col.contains(start)) {
                    return col;
                }
            }
            return "没有匹配到结果";
        } else {

            return "contains filter input object is not collection or string";
        }
    }
}
