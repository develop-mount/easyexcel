package com.alibaba.excel.write.handler.filter;

import com.alibaba.excel.util.PipeFilterUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.handler.BasePipeFilter;
import com.alibaba.excel.write.handler.PipeDataWrapper;

import java.util.Collection;
import java.util.Objects;

/**
 * Description:
 * 优先startWith
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/28 15:52
 */
public class PriorStartsWithFilter extends BasePipeFilter<Object, Object> {

    @Override
    public PipeDataWrapper<Object> apply(PipeDataWrapper<Object> wrapper) {

        if (!verify(wrapper)) {
            return wrapper;
        }

        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.error("prior-starts-with错误:传入数据不能为空");
        }

        if (PipeFilterUtils.isEmpty(params())) {
            return PipeDataWrapper.error("prior-starts-with错误:指令缺失参数");
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
                            return PipeDataWrapper.success(col);
                        }
                    }
                }
            }
            return PipeDataWrapper.error(String.format("错误:starts-with指令没有匹配[%s]到结果", String.join(",", params())));
        } else if (value instanceof String) {

            String col = (String) value;
            for (String start : params()) {
                if (StringUtils.isBlank(start)) {
                    continue;
                }
                if (col.startsWith(start)) {
                    return PipeDataWrapper.success(col);
                }
            }
            return PipeDataWrapper.error(String.format("错误:starts-with指令没有匹配[%s]到结果", String.join(",", params())));
        } else {

            throw new RuntimeException("错误:starts-with指令输入数据不是字符串或集合");
        }
    }
}
