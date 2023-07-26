package com.alibaba.excel.write.handler.filter;

import com.alibaba.excel.metadata.data.FormulaData;
import com.vevor.expression.filter.BasePipeFilter;
import com.vevor.expression.filter.PipeDataWrapper;

/**
 * Description:
 * formula
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/21 16:13
 */
public class CellFormulaFilter extends BasePipeFilter<Object, Object> {
    
    @Override
    protected String filterName() {
        return "cell-formula";
    }

    @Override
    public PipeDataWrapper<Object> handlerApply(PipeDataWrapper<Object> wrapper) {

        FormulaData formulaData = new FormulaData();
        // 验证
        if (!verify(wrapper)) {
            wrapper.setExtra(formulaData);
            return wrapper;
        }

        PipeDataWrapper<Object> success = PipeDataWrapper.success(wrapper.getData());
        success.setExtra(formulaData);
        return success;
    }
}
