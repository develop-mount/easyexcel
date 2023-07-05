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

        String delimiter = params().get(0);

        Delimiter delimiterEnum = Delimiter.ofValue(delimiter);
        if (Objects.nonNull(delimiterEnum)) {
            return PipeDataWrapper.success(val + delimiterEnum.getDelimiter());
        }

        return PipeDataWrapper.success(delimiter + val + delimiter);
    }
}
