package com.alibaba.excel.write.handler.filter;

import com.vevor.expression.filter.BasePipeFilter;
import com.vevor.expression.filter.PipeDataWrapper;

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
        if (wrapper.success()) {
            return PipeDataWrapper.success(wrapper.getData());
        }
        return PipeDataWrapper.error(wrapper.getMessage(), wrapper.getData());
    }
}
