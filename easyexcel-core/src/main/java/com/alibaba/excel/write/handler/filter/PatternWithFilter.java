package com.alibaba.excel.write.handler.filter;

import com.alibaba.excel.util.PipeFilterUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.handler.BasePipeFilter;
import com.alibaba.excel.write.handler.PipeDataWrapper;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Description:
 * pattern
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/30 10:37
 */
public class PatternWithFilter extends BasePipeFilter<Object, Object> {

    @Override
    public PipeDataWrapper<Object> apply(PipeDataWrapper<Object> wrapper) {

        // 验证
        if (!verify(wrapper)) {
            return wrapper;
        }

        if (PipeFilterUtils.isEmpty(params())) {
            return PipeDataWrapper.error("pattern错误:指令缺失参数");
        }

        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.error("pattern错误:传入数据不能为空");
        }

        if (params().size() == 1) {

            String center = params().get(0);
            if (StringUtils.isBlank(center)) {
                return PipeDataWrapper.error("pattern错误:指令参数不能为空");
            }

            return instructHandle(value, center);
        } else {

            List<Object> result = new ArrayList<>();
            List<String> error = new ArrayList<>();
            for (String center : params()) {
                if (Objects.isNull(center)) {
                    continue;
                }
                PipeDataWrapper<Object> itemDataWrapper = instructHandle(value, center);
                if (itemDataWrapper.success()) {
                    result.add(itemDataWrapper.getData());
                } else {
                    error.add(itemDataWrapper.getMessage());
                }
            }
            if (CollectionUtils.isEmpty(error)) {
                return PipeDataWrapper.success(result);
            } else {
                return PipeDataWrapper.error("pattern错误:" + String.join(",", error), result);
            }
        }
    }

    /**
     * 指令处理
     *
     * @param value  待处理的值
     * @param center 待匹配的字符串
     * @return 数据包裹
     */
    private PipeDataWrapper<Object> instructHandle(Object value, String center) {

        if (value instanceof Collection) {
            String result = null;
            //noinspection unchecked
            Collection<Object> collection = (Collection<Object>) value;
            for (Object col : collection) {
                if (Objects.isNull(col)) {
                    continue;
                }
                if (col instanceof String) {
                    String cel = (String) col;
                    if (Pattern.matches(center, cel)) {
                        result = cel;
                        break;
                    }
                }
            }
            if (StringUtils.isNotBlank(result)) {
                return PipeDataWrapper.error(String.format("没有包含[%s]的数据", center));
            } else {
                return PipeDataWrapper.success(result);
            }
        } else if (value instanceof String) {
            String col = (String) value;
            if (Pattern.matches(center, col)) {
                return PipeDataWrapper.success(col);
            } else {
                return PipeDataWrapper.error(String.format("没有包含[%s]的数据", center));
            }
        } else {
            return PipeDataWrapper.error("pattern错误:指令传入数据不是集合或字符串", value);
        }
    }
}
