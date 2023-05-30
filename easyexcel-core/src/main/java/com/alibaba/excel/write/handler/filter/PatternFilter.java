package com.alibaba.excel.write.handler.filter;

import com.alibaba.excel.util.PipeFilterUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.handler.BasePipeFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/29 14:48
 */
public class PatternFilter extends BasePipeFilter<Object, Object> {
    @Override
    public Object apply(Object value) {

        if (Objects.isNull(value)) {
            return "";
        }

        if (PipeFilterUtils.isEmpty(params())) {
            return value;
        }

        if (value instanceof Collection) {

            //noinspection unchecked
            Collection<Object> collection = (Collection<Object>) value;
            for (Object col : collection) {
                if (Objects.isNull(col)) {
                    continue;
                }
                if (col instanceof String) {
                    String cel = (String) col;
                    for (String regex : params()) {
                        if (StringUtils.isBlank(regex)) {
                            continue;
                        }
                        if (Pattern.matches(regex, cel)) {
                            return cel;
                        }
                    }
                }
            }
            return "没有匹配到结果";
        } else if (value instanceof String) {
            String col = (String) value;
            for (String regex : params()) {
                if (StringUtils.isBlank(regex)) {
                    continue;
                }
                if (Pattern.matches(regex, col)) {
                    return col;
                }
            }
            return "没有匹配到结果";
        }
        return "pattern filter input object is not collection or string";
    }
}
