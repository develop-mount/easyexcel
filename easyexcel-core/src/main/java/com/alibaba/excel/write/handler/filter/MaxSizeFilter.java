package com.alibaba.excel.write.handler.filter;

import com.alibaba.excel.util.PipeFilterUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.handler.BasePipeFilter;
import com.alibaba.excel.write.handler.PipeDataWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/27 12:32
 */
@Slf4j
public class MaxSizeFilter extends BasePipeFilter<Object, Object> {
    @Override
    protected String filterName() {
        return "max-size";
    }

    @Override
    public PipeDataWrapper<Object> apply(PipeDataWrapper<Object> wrapper) {

        // 验证
        if (!verify(wrapper)) {
            return wrapper;
        }

        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.success("");
        }

        if (PipeFilterUtils.isEmpty(params())) {
            return PipeDataWrapper.error(errorPrefix() + "参数为空");
        }

        if (params().size() != 1) {
            return PipeDataWrapper.error(errorPrefix() + "仅支持1个参数");
        }

        String param = params().get(0);
        if (StringUtils.isBlank(param)) {
            return PipeDataWrapper.error(errorPrefix() + "参数为空");
        }
        if (!StringUtils.isNumeric(param)) {
            return PipeDataWrapper.error(errorPrefix() + "参数类型应该是数字");
        }

        int paramInt;
        int valueInt;
        try {
            paramInt = Integer.parseInt(param);
            if (value instanceof String) {
                String val = (String) value;
                if (!StringUtils.isNumeric(val)) {
                    return PipeDataWrapper.error(errorPrefix() + "传入数据类型应该是数字");
                }
                valueInt = Integer.parseInt(val);
            } else if (value instanceof Number) {
                valueInt = ((Number) value).intValue();
            } else {
                return PipeDataWrapper.error(errorPrefix() + "传入数据类型应该是数字");
            }
        } catch (NumberFormatException e) {
            log.warn(e.getMessage(), e);
            return PipeDataWrapper.error(errorPrefix() + "传入数据类型应该是数字");
        }

        if (valueInt > paramInt) {
            return PipeDataWrapper.error(errorPrefix() + String.format("传入数字:[%d]超过[%d]大小", valueInt, paramInt));
        }
        return PipeDataWrapper.success(valueInt);
    }
}
