package com.alibaba.excel.util;

import com.alibaba.excel.enums.WriteTemplateAnalysisCellTypeEnum;
import com.alibaba.excel.write.executor.ExcelWriteFillExecutor;
import com.alibaba.excel.write.metadata.fill.AnalysisCell;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.util.*;

/**
 * Description:
 * 单元格变量工具
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/7 10:38
 */
public class CellVariableUtils {
    private static final String ESCAPE_FILL_PREFIX = "\\\\\\{";
    private static final String ESCAPE_FILL_SUFFIX = "\\\\\\}";
    private static final String FILL_PREFIX = "{";
    private static final String FILL_SUFFIX = "}";
    private static final char IGNORE_CHAR = '\\';
    private static final String COLLECTION_PREFIX = ".";

    /**
     * 根据cell值获取单元格变量
     * @author linfeng
     * @param cellValue 单元格值
     * @return 变量集合
     */
    public static List<String> getVariable(String cellValue) {

        List<String> varList = new ArrayList<>();
        int startIndex = 0;
        int length = cellValue.length();
        out:
        while (startIndex < length) {
            int prefixIndex = cellValue.indexOf(FILL_PREFIX, startIndex);
            if (prefixIndex < 0) {
                break;
            }
            if (prefixIndex != 0) {
                char prefixPrefixChar = cellValue.charAt(prefixIndex - 1);
                if (prefixPrefixChar == IGNORE_CHAR) {
                    startIndex = prefixIndex + 1;
                    continue;
                }
            }
            int suffixIndex = -1;
            while (suffixIndex == -1 && startIndex < length) {
                suffixIndex = cellValue.indexOf(FILL_SUFFIX, startIndex + 1);
                if (suffixIndex < 0) {
                    break out;
                }
                startIndex = suffixIndex + 1;
                char prefixSuffixChar = cellValue.charAt(suffixIndex - 1);
                if (prefixSuffixChar == IGNORE_CHAR) {
                    suffixIndex = -1;
                }
            }

            String variable = cellValue.substring(prefixIndex + 1, suffixIndex);
            if (StringUtils.isEmpty(variable)) {
                continue;
            }
            int collectPrefixIndex = variable.indexOf(COLLECTION_PREFIX);
            if (collectPrefixIndex == 0) {
                variable = variable.substring(collectPrefixIndex + 1);
                if (StringUtils.isEmpty(variable)) {
                    continue;
                }
            }
            varList.add(variable);
        }
        return varList;
    }

    public static void prepareData(String value) {
        if (StringUtils.isEmpty(value)) {
            return ;
        }

        List<String> variableList = new ArrayList<>();
        List<String> prepareDataList = new ArrayList<>();

        Boolean onlyOneVariable = Boolean.TRUE;
        int startIndex = 0;
        int length = value.length();
        int lastPrepareDataIndex = 0;
        out:
        while (startIndex < length) {
            int prefixIndex = value.indexOf(FILL_PREFIX, startIndex);
            if (prefixIndex < 0) {
                break;
            }
            if (prefixIndex != 0) {
                char prefixPrefixChar = value.charAt(prefixIndex - 1);
                if (prefixPrefixChar == IGNORE_CHAR) {
                    startIndex = prefixIndex + 1;
                    continue;
                }
            }
            int suffixIndex = -1;
            while (suffixIndex == -1 && startIndex < length) {
                suffixIndex = value.indexOf(FILL_SUFFIX, startIndex + 1);
                if (suffixIndex < 0) {
                    break out;
                }
                startIndex = suffixIndex + 1;
                char prefixSuffixChar = value.charAt(suffixIndex - 1);
                if (prefixSuffixChar == IGNORE_CHAR) {
                    suffixIndex = -1;
                }
            }
            String variable = value.substring(prefixIndex + 1, suffixIndex);
            if (StringUtils.isEmpty(variable)) {
                continue;
            }
            int collectPrefixIndex = variable.indexOf(COLLECTION_PREFIX);
            if (collectPrefixIndex == 0) {
                variable = variable.substring(collectPrefixIndex + 1);
                if (StringUtils.isEmpty(variable)) {
                    continue;
                }
            }
            variableList.add(variable);
            if (lastPrepareDataIndex == prefixIndex) {
                prepareDataList.add(StringUtils.EMPTY);
                if (lastPrepareDataIndex != 0) {
                    onlyOneVariable = Boolean.FALSE;
                }
            } else {
                String data = convertPrepareData(value.substring(lastPrepareDataIndex, prefixIndex));
                prepareDataList.add(data);
                onlyOneVariable = Boolean.FALSE;
            }
            lastPrepareDataIndex = suffixIndex + 1;
        }

        if (lastPrepareDataIndex == length) {
            prepareDataList.add(StringUtils.EMPTY);
        } else {
            prepareDataList.add(convertPrepareData(value.substring(lastPrepareDataIndex)));
            onlyOneVariable = Boolean.FALSE;
        }

        System.out.println("");

    }

    private static String convertPrepareData(String prepareData) {
        prepareData = prepareData.replaceAll(ESCAPE_FILL_PREFIX, FILL_PREFIX);
        prepareData = prepareData.replaceAll(ESCAPE_FILL_SUFFIX, FILL_SUFFIX);
        return prepareData;
    }
}
