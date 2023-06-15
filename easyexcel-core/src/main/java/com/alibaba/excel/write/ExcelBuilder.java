package com.alibaba.excel.write;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.context.WriteContext;
import com.alibaba.excel.write.executor.ExcelWriteFillExecutor;
import com.alibaba.excel.write.merge.OnceAbsoluteMergeStrategy;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.fill.AnalysisCell;
import com.alibaba.excel.write.metadata.fill.FillConfig;

/**
 * @author jipengfei
 */
public interface ExcelBuilder {

    /**
     * WorkBook increase value
     *
     * @param data
     *            java basic type or java model extend BaseModel
     * @param writeSheet
     *            Write the sheet
     * @deprecated please use{@link ExcelBuilder#addContent(Collection, WriteSheet, WriteTable)}
     */
    @Deprecated
    void addContent(Collection<?> data, WriteSheet writeSheet);

    /**
     * WorkBook increase value
     *
     * @param data
     *            java basic type or java model extend BaseModel
     * @param writeSheet
     *            Write the sheet
     * @param writeTable
     *            Write the table
     */
    void addContent(Collection<?> data, WriteSheet writeSheet, WriteTable writeTable);

    /**
     * WorkBook fill value
     *
     * @param data
     * @param fillConfig
     * @param writeSheet
     */
    void fill(Object data, FillConfig fillConfig, WriteSheet writeSheet);

    /**
     * 得到fill 错误消息
     * @return 错误消息
     */
    Map<ExcelWriteFillExecutor.UniqueDataFlagKey, List<AnalysisCell>> fillMessage();

    /**
     * Creates new cell range. Indexes are zero-based.
     *
     * @param firstRow
     *            Index of first row
     * @param lastRow
     *            Index of last row (inclusive), must be equal to or larger than {@code firstRow}
     * @param firstCol
     *            Index of first column
     * @param lastCol
     *            Index of last column (inclusive), must be equal to or larger than {@code firstCol}
     * @deprecated please use{@link OnceAbsoluteMergeStrategy}
     */
    @Deprecated
    void merge(int firstRow, int lastRow, int firstCol, int lastCol);

    /**
     * Gets the written data
     *
     * @return
     */
    WriteContext writeContext();

    /**
     * Close io
     *
     * @param onException
     */
    void finish(boolean onException);

    /**
     * 自动填充错误，设置错误字段
     * @param errorField 错误字段
     */
    void setAutoErrorField(String errorField);
}
