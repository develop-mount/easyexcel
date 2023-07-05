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
public class WrapperFilter extends AbstractEchoFilter {
    @Override
    protected String filterName() {
        return "wrapper";
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
            return PipeDataWrapper.success("");
        }

        if (PipeFilterUtils.isEmpty(params())) {
            return PipeDataWrapper.success("");
        }

        String left = params().get(0);
        Delimiter leftDelimiter = Delimiter.ofValue(left);
        if (Objects.nonNull(leftDelimiter)) {
            left = leftDelimiter.getDelimiter();
        }

        String right;
        if (params().size() > 1) {
            right = params().get(1);
            if (StringUtils.isBlank(right)) {
                right = left;
            } else {
                Delimiter rightDelimiter = Delimiter.ofValue(left);
                if (Objects.nonNull(rightDelimiter)) {
                    right = rightDelimiter.getDelimiter();
                }
            }
        } else {
            right = left;
        }

        return PipeDataWrapper.success(left + val + right);
    }
}
