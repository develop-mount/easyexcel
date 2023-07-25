package com.alibaba.excel.write.handler.filter;

import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.vevor.expression.filter.BasePipeFilter;
import com.vevor.expression.filter.PipeDataWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.Objects;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/7/18 10:35
 */
@Slf4j
public class CellRedFilter extends BasePipeFilter<Object, Object> {
    @Override
    protected String filterName() {
        return "cell-red";
    }

    @Override
    public PipeDataWrapper<Object> handlerApply(PipeDataWrapper<Object> wrapper) {

        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.success("");
        }

        WriteCellData<Object> writeCellData;
        if (value instanceof WriteCellData) {
            //noinspection unchecked
            writeCellData = (WriteCellData<Object>) value;
        } else {
            writeCellData = new WriteCellData<>(value.toString());
        }

        WriteCellStyle writeCellStyle = new WriteCellStyle();
        writeCellStyle.setFillForegroundColor(IndexedColors.RED.index);
        writeCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);

        writeCellData.setWriteCellStyle(writeCellStyle);

        if (wrapper.success()) {
            return PipeDataWrapper.success(writeCellData);
        } else {
            return PipeDataWrapper.error(wrapper.getMessage(), writeCellData);
        }
    }
}
