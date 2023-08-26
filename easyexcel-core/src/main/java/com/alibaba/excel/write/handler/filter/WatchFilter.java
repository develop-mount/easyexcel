package com.alibaba.excel.write.handler.filter;

import icu.develop.expression.filter.BasePipeFilter;
import icu.develop.expression.filter.PipeDataWrapper;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/7/20 10:21
 */
public class WatchFilter extends BasePipeFilter<Object, Object> {
    @Override
    protected String filterName() {
        return "watch";
    }

    @Override
    public PipeDataWrapper<Object> handlerApply(PipeDataWrapper<Object> wrapper) {
        // 验证
        if (!verify(wrapper)) {
            return wrapper;
        }
        return PipeDataWrapper.error(wrapper.getMessage(), wrapper.getData());
    }
}
