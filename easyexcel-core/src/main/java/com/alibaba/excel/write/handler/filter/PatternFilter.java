package com.alibaba.excel.write.handler.filter;

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

        if (value instanceof Collection) {

            List<Object> result = new ArrayList<>();
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
                        Pattern pattern = Pattern.compile(regex);
                        if (pattern.matcher(cel).matches()) {
                            result.add(cel);
                            break;
                        }
                    }
                }
            }
            return result;

        } else if (value instanceof String) {
            String col = (String) value;
            for (String regex : params()) {
                if (StringUtils.isBlank(regex)) {
                    continue;
                }
                Pattern pattern = Pattern.compile(regex);
                if (pattern.matcher(col).matches()) {
                    return col;
                }
            }
            return "";
        }
        return "pattern filter input object is not collection or string";
    }
}
