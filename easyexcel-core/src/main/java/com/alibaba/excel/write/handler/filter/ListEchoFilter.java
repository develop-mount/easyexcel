package com.alibaba.excel.write.handler.filter;

import com.alibaba.excel.util.PipeFilterUtils;
import com.alibaba.excel.write.handler.BasePipeFilter;
import com.alibaba.excel.write.handler.PipeDataWrapper;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:
 * list-out
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/30 16:52
 */
public class ListEchoFilter extends BasePipeFilter<Object, Object> {

    @Override
    protected String filterName() {
        return "list-echo";
    }

    private enum Delimiter {
        /**
         * 空格
         */
        BLANK("blank", " "),
        /**
         * 回车
         */
        WRAP("wrap", "\n"),
        /**
         * 逗号
         */
        COMMA("comma", ",");

        private final String value;
        private final String delimiter;

        Delimiter(String value, String delimiter) {
            this.value = value;
            this.delimiter = delimiter;
        }

        public static Delimiter ofValue(String value) {
            for (Delimiter delim : Delimiter.values()) {
                if (delim.value.equalsIgnoreCase(value)) {
                    return delim;
                }
            }
            return null;
        }

    }

    @Override
    public PipeDataWrapper<Object> apply(PipeDataWrapper<Object> wrapper) {

        // 验证
        if (!verify(wrapper)) {
            return wrapper;
        }

        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.error(errorPrefix() + "传入数据不能为空");
        }

        if (!(value instanceof Collection)) {
            return PipeDataWrapper.error(errorPrefix() + "传入数据不是集合");
        }

        @SuppressWarnings("unchecked")
        List<Object> collection = (List<Object>) value;

        if (PipeFilterUtils.isEmpty(collection)) {
            return PipeDataWrapper.error(errorPrefix() + "传入集合为空");
        }

        if (PipeFilterUtils.isEmpty(params())) {
            return PipeDataWrapper.success(collection.stream().map(String::valueOf).collect(Collectors.joining(Delimiter.WRAP.delimiter)));
        }

        String delimiter = params().get(0);

        Delimiter delimiterEnum = Delimiter.ofValue(delimiter);
        if (Objects.nonNull(delimiterEnum)) {
            return PipeDataWrapper.success(collection.stream().map(String::valueOf).collect(Collectors.joining(delimiterEnum.delimiter)));
        }

        return PipeDataWrapper.error(errorPrefix() + "指令格式错误，example: list-echo:comma, list-echo:wrap or list-echo:blank");
    }
}
