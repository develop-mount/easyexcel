package com.alibaba.excel.write.handler.filter;

import com.alibaba.excel.util.PipeFilterUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.handler.BasePipeFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/30 16:52
 */
@Slf4j
public class ListIndexFilter extends BasePipeFilter<Object, Object> {

    @Override
    public Object apply(Object value) {

        if (Objects.isNull(value)) {
            return "";
        }

        if (!(value instanceof Collection)) {
            throw new RuntimeException("错误:list-index:传入数据不是集合");
        }

        @SuppressWarnings("unchecked")
        List<Object> collection = (List<Object>) value;

        if (PipeFilterUtils.isEmpty(params()) || params().size() > 1) {
            throw new RuntimeException("错误:list-index:传入参数下标为空或是超过一个");
        }

        String index = params().get(0);
        if (StringUtils.isBlank(index)) {
            throw new RuntimeException("错误:传入参数下标为空");
        }

        try {
            int ind = Integer.parseInt(index);
            if (ind < 0) {
                ind = 0;
            }
            if (ind >= 1) {
                ind -= 1;
            }
            return collection.get(ind);
        } catch (NumberFormatException e) {
            log.warn(e.getMessage(), e);
            throw new RuntimeException("错误:list-index:下标转换错误");
        }
    }
}
