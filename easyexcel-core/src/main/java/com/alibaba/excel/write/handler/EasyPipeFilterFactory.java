package com.alibaba.excel.write.handler;

import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.handler.filter.*;
import com.alibaba.excel.write.metadata.fill.CellInfo;
import com.vevor.expression.filter.BasePipeFilter;
import com.vevor.expression.filter.PipeDataWrapper;
import com.vevor.expression.filter.PipeFilterFactory;
import com.vevor.expression.filter.PipeFilterPool;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/27 22:19
 */
@Slf4j
public class EasyPipeFilterFactory extends PipeFilterFactory {

    protected CellInfo cellInfo;

    static {
        PipeFilterPool.INSTANCE.addPipeFilter("formula", FormulaFilter::new);
        PipeFilterPool.INSTANCE.addPipeFilter("cell-red", CellRedFilter::new);
        PipeFilterPool.INSTANCE.addPipeFilter("watch", WatchFilter::new);
        PipeFilterPool.INSTANCE.addPipeFilter("cell-formula", CellFormulaFilter::new);
        PipeFilterPool.INSTANCE.addPipeFilter("echo-cell-row", EchoCellRowFilter::new);
    }

    /**
     * 创建管道过滤器
     *
     * @return 管道过滤器
     */
    public static EasyPipeFilterFactory createPipeFilter() {
        return new EasyPipeFilterFactory();
    }

    /**
     * 转换列名
     *
     * @param column 列号
     * @return 列名
     */
    protected String convertColumnToName(int column) {
        StringBuilder colIndex = new StringBuilder();
        while (column >= 0) {
            int i = column % 26;
            colIndex.append((char) (i + 65));
            column = column / 26 - 1;
        }
        return colIndex.reverse().toString();
    }

    /**
     * 注册管道过滤器
     *
     * @param name       filter name
     * @param pipeFilter pipe filer
     * @return factory
     */
    public EasyPipeFilterFactory registerPipeFilter(String name, Supplier<BasePipeFilter<Object, Object>> pipeFilter) {
        super.registerPipeFilter(name, pipeFilter);
        return this;
    }

    /**
     * @param pipeFilterMap pipe filer map
     * @return easy excel pipe filter
     */
    public EasyPipeFilterFactory customerPipeFilter(Map<String, Supplier<BasePipeFilter<Object, Object>>> pipeFilterMap) {
        PipeFilterPool.INSTANCE.addPipeFilter(pipeFilterMap);
        return this;
    }

    /**
     * @param row    行下标
     * @param column 列下标
     * @return this
     */
    public EasyPipeFilterFactory setCell(int row, int column) {
        this.cellInfo = new CellInfo(row, column, convertColumnToName(column));
        return this;
    }

    @Override
    protected PipeDataWrapper<Object> applyAfter(PipeDataWrapper<Object> dataWrapper) {
        if (isValidity(dataWrapper)) {
            return dataWrapper;
        }
        String msg = String.format("第[%s]列,数据错误:变量值类型错误，不应该是%s", cellInfo.getColumnName(), dataWrapper.getData().getClass().getSimpleName());
        log.debug(msg);
        return PipeDataWrapper.error(msg);
    }

    @Override
    protected void noticePrefix(BasePipeFilter<Object, Object> pipeFilter, String variableName) {
        if (StringUtils.isBlank(cellInfo.getColumnName())) {
            throw new RuntimeException("请先调用setCell方法设置行列值");
        }
        pipeFilter.setNoticePrefix(String.format("[%s]列,[%s]变量的", cellInfo.getColumnName(), variableName));
    }

    @Override
    protected void additionalInfo(BasePipeFilter<Object, Object> pipeFilter) {
        if (StringUtils.isBlank(cellInfo.getColumnName())) {
            throw new RuntimeException("请先调用setCell方法设置行列值");
        }
        pipeFilter.addExtra(cellInfo);
    }

    @Override
    protected String filterName() {
        return "factory";
    }
}
