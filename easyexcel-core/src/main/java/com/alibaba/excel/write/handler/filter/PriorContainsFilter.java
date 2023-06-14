package com.alibaba.excel.write.handler.filter;

import com.alibaba.excel.util.PipeFilterUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.handler.BasePipeFilter;
import com.alibaba.excel.write.handler.PipeDataWrapper;

import java.util.Collection;
import java.util.Objects;

/**
 * Description:
 * 优先包含
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/30 10:37
 */
public class PriorContainsFilter extends BasePipeFilter<Object, Object> {


    @Override
    public PipeDataWrapper<Object> apply(PipeDataWrapper<Object> wrapper) {

        // 验证
        if (!verify(wrapper)) {
            return wrapper;
        }

        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.error("prior-contains错误:传入数据不能为空");
        }

        if (PipeFilterUtils.isEmpty(params())) {
            return PipeDataWrapper.error("prior-contains错误:指令缺失参数");
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
                            return PipeDataWrapper.success(col);
                        }
                    }
                }
            }
            return PipeDataWrapper.error(String.format("错误:prior-contains指令没有匹配到[%s]结果", String.join(",", params())));
        } else if (value instanceof String) {

            String col = (String) value;
            for (String center : params()) {
                if (StringUtils.isBlank(center)) {
                    continue;
                }
                if (col.contains(center)) {
                    return PipeDataWrapper.success(col);
                }
            }
            return PipeDataWrapper.error(String.format("错误:prior-contains指令没有匹配到[%s]结果", String.join(",", params())));
        } else {

            return PipeDataWrapper.error("错误:prior-contains指令传入数据不是集合或字符串");
        }
    }
}
