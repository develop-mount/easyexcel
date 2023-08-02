package com.alibaba.excel.write.metadata.fill;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/8/2 10:54
 */
@Data
@AllArgsConstructor
public class CellInfo {
    protected int rowIndex;
    protected int columnIndex;
    protected String columnName;
}
