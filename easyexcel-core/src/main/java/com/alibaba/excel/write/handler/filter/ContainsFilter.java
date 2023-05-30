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
            for (Object col : collection) {
                if (Objects.isNull(col)) {
                    continue;
                }
                if (col instanceof String) {
                    String cel = (String) col;
                    for (String center : params()) {
                        if (Objects.isNull(center)) {
                            continue;
                        }
                        if (cel.contains(center)) {
                            return col;
                        }
                    }
                }
            }
            return "没有匹配到结果";
        } else if (value instanceof String) {

            String col = (String) value;
            for (String center : params()) {
                if (StringUtils.isBlank(center)) {
                    continue;
                }
                if (col.contains(center)) {
                    return col;
                }
            }
            return "没有匹配到结果";
        } else {

            return "contains filter input object is not collection or string";
        }
    }
}
