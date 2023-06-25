package com.alibaba.excel.write.handler.filter;

import com.alibaba.excel.util.PipeFilterUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.handler.PipeDataWrapper;

import java.util.Objects;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/18 9:43
 */
public class ConditionEchoFilter extends AbstractEchoFilter {

    public static final int INT_3 = 3;

    @Override
    protected String filterName() {
        return "condition-echo";
    }

    @Override
    public PipeDataWrapper<Object> apply(PipeDataWrapper<Object> wrapper) {

        if (Objects.isNull(wrapper)) {
            return PipeDataWrapper.error(errorPrefix() + "输入数据不能为空");
        }

        if (!wrapper.success()) {
            return PipeDataWrapper.error(wrapper.getMessage(), "");
        }

        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.success("");
        }

        if (!(value instanceof String)) {
            return PipeDataWrapper.error(errorPrefix() + "传入数据不是字符串");
        }

        String val = (String) value;
        if (StringUtils.isBlank(val)) {
            return PipeDataWrapper.error(errorPrefix() + "输入数据不能为空");
        }

        if (PipeFilterUtils.isEmpty(params())) {
            return PipeDataWrapper.success("输入参数不能为空");
        }

        if (params().size() != INT_3) {
            return PipeDataWrapper.success("输入参数个数为3个");
        }

        String delimiter = params().get(0);
        String successVal = params().get(1);
        String errorVal = params().get(2);

        if (Objects.equals(val, delimiter)) {
            return PipeDataWrapper.success(successVal);
        } else {
            return PipeDataWrapper.success(errorVal);
        }
    }
}
