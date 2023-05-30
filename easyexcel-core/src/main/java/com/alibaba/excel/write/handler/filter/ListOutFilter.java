package com.alibaba.excel.write.handler.filter;

import com.alibaba.excel.util.PipeFilterUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.handler.BasePipeFilter;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/30 16:52
 */
public class ListOutFilter extends BasePipeFilter<Object, Object> {

    private static final String WRAP = "wrap";
    private static final String COMMA = "comma";
    private static final String DELIMITER_WRAP = "\n";
    private static final String DELIMITER_COMMA = ",";

    @Override
    public Object apply(Object value) {

        if (Objects.isNull(value)) {
            return "";
        }

        if (!(value instanceof Collection)) {
            return "list-out filter input object is not collection";
        }

        @SuppressWarnings("unchecked")
        List<Object> collection = (List<Object>) value;

        if (PipeFilterUtils.isEmpty(collection)) {
            return "list-out filter input object is empty";
        }

        if (PipeFilterUtils.isEmpty(params())) {
            return collection.stream().map(String::valueOf).collect(Collectors.joining(DELIMITER_WRAP));
        }

        String delimiter = params().get(0);
        if (WRAP.equalsIgnoreCase(delimiter)) {
            return collection.stream().map(String::valueOf).collect(Collectors.joining(DELIMITER_WRAP));
        }
        if (COMMA.equalsIgnoreCase(delimiter)) {
            return collection.stream().map(String::valueOf).collect(Collectors.joining(DELIMITER_COMMA));
        }

        return "The parameters of the list out filter are incorrect, for example: list-out:comma or list-out:wrap";

    }
}
