package com.alibaba.excel.write.handler.filter;

import com.alibaba.excel.write.metadata.fill.CellInfo;
import icu.develop.expression.filter.BasePipeFilter;
import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.utils.PipeFilterUtils;

import java.util.Objects;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/8/2 11:01
 */
public class EchoCellRowFilter extends BasePipeFilter<Object, Object> {
    @Override
    protected String filterName() {
        return "echo-cell-row";
    }

    @Override
    protected PipeDataWrapper<Object> handlerApply(PipeDataWrapper<Object> wrapper) {
        if (PipeFilterUtils.isEmpty(extra())) {
            return PipeDataWrapper.error(errorPrefix() + "额外信息为空");
        }

        Object extraObj = extra().get(0);
        if (Objects.isNull(extraObj)) {
            return PipeDataWrapper.error(errorPrefix() + "额外信息为空");
        }

        CellInfo cellInfo = (CellInfo) extraObj;
        return PipeDataWrapper.success(cellInfo.getRowIndex() + 1);
    }
}
