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
 * @since 2023/5/28 15:52
 */
public class StartsWithFilter extends BasePipeFilter<Object, Object> {

    @Override
    public Object apply(Object value) {

        if (Objects.isNull(value)) {
            return "";
        }

        if (PipeFilterUtils.isEmpty(params())) {
            throw new RuntimeException("错误:starts-with指令缺失参数");
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
                    for (String start : params()) {
                        if (Objects.isNull(start)) {
                            continue;
                        }
                        if (cel.startsWith(start)) {
                            return cel;
                        }
                    }
                }
            }
            throw new RuntimeException(String.format("错误:starts-with指令没有匹配[%s]到结果", String.join(",", params())));
        } else if (value instanceof String) {

            String col = (String) value;
            for (String start : params()) {
                if (StringUtils.isBlank(start)) {
                    continue;
                }
                if (col.startsWith(start)) {
                    return col;
                }
            }
            throw new RuntimeException(String.format("错误:starts-with指令没有匹配[%s]到结果", String.join(",", params())));
        } else {

            throw new RuntimeException("错误:starts-with指令输入数据不是字符串或集合");
        }
    }
}
