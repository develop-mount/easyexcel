package com.alibaba.excel.write.handler.filter;

import com.alibaba.excel.metadata.data.FormulaData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.vevor.expression.filter.BasePipeFilter;
import com.vevor.expression.filter.PipeDataWrapper;
import com.vevor.expression.filter.utils.PipeFilterUtils;
import com.vevor.expression.filter.utils.StringUtils;

import java.util.Objects;

/**
 * Description:
 * formula
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/21 16:13
 */
public class FormulaFilter extends BasePipeFilter<Object, Object> {

    private static final int PARAMS_NUM = 1;
    public static final String SPLIT = "#";
    public static final String REPLACEMENT = ",";

    @Override
    protected String filterName() {
        return "formula";
    }

    @Override
    public PipeDataWrapper<Object> handlerApply(PipeDataWrapper<Object> wrapper) {
        // 验证
        if (!verify(wrapper)) {
            return wrapper;
        }

        if (PipeFilterUtils.isEmpty(params()) || params().size() != PARAMS_NUM) {
            return PipeDataWrapper.error(errorPrefix() + "传入参数为空或是超过1个");
        }

        String formula = params().get(0);

        if (StringUtils.isBlank(formula)) {
            return PipeDataWrapper.error(errorPrefix() + "传入数据或参数不能为空");
        }

        if (formula.contains(SPLIT)) {
            formula = formula.replaceAll(SPLIT, REPLACEMENT);
        }

        WriteCellData<Object> writeCellData;
        Object data = wrapper.getData();

        if (Objects.nonNull(data)) {

            if (!(data instanceof String)) {
                return PipeDataWrapper.error(errorPrefix() + "传入数据必须是字符串");
            }
            writeCellData = new WriteCellData<>(data.toString());
        } else {
            writeCellData = new WriteCellData<>();
        }
        FormulaData formulaData = new FormulaData();
        formulaData.setFormulaValue(formula);
        writeCellData.setFormulaData(formulaData);

        return PipeDataWrapper.success(writeCellData);
    }
}
