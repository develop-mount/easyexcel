package com.alibaba.excel.write.handler.filter;

import com.alibaba.excel.util.PipeFilterUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.handler.BasePipeFilter;
import com.alibaba.excel.write.handler.PipeDataWrapper;

import java.util.Collection;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Description:
 * 优先模式匹配
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/29 14:48
 */
public class PriorPatternFilter extends BasePipeFilter<Object, Object> {
    @Override
    public PipeDataWrapper<Object> apply(PipeDataWrapper<Object> wrapper) {

        if (!verify(wrapper)) {
            return wrapper;
        }

        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.error("prior-pattern错误:传入数据不能为空");
        }

        if (PipeFilterUtils.isEmpty(params())) {
            return PipeDataWrapper.error("prior-pattern错误:指令缺失参数");
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
                            return PipeDataWrapper.success(col);
                        }
                    }
                }
            }
            return PipeDataWrapper.error(String.format("错误:prior-pattern指令没有匹配[%s]到结果", String.join(",", params())));
        } else if (value instanceof String) {
            String col = (String) value;
            for (String regex : params()) {
                if (StringUtils.isBlank(regex)) {
                    continue;
                }
                if (Pattern.matches(regex, col)) {
                    return PipeDataWrapper.success(col);
                }
            }
            return PipeDataWrapper.error(String.format("错误:prior-pattern指令没有匹配[%s]到结果", String.join(",", params())));
        }

        return PipeDataWrapper.error("错误:prior-pattern指令输入数据不是字符串或集合");
    }
}
