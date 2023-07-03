package com.alibaba.excel.write.handler.filter;

import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.NumberUtils;
import com.alibaba.excel.util.PipeFilterUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.handler.BasePipeFilter;
import com.alibaba.excel.write.handler.PipeDataWrapper;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.Objects;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/27 12:32
 */
@Slf4j
public class MaxSizeFilter extends BasePipeFilter<Object, Object> {

    private static final String NUMBER_REG = "^[0-9]+(.[0-9]+)?$";
    public static final String B_UPPER = "B";
    public static final String B_LOWER = "b";
    public static final String KB_UPPER = "KB";
    public static final String KB_LOWER = "kb";
    public static final String MB_UPPER = "MB";
    public static final String MB_LOWER = "mb";
    public static final String GB_UPPER = "GB";
    public static final String GB_LOWER = "gb";

    @Override
    protected String filterName() {
        return "max-size";
    }

    @Override
    public PipeDataWrapper<Object> apply(PipeDataWrapper<Object> wrapper) {
        // 验证
        if (!verify(wrapper)) {
            return wrapper;
        }
        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.success("");
        }
        if (PipeFilterUtils.isEmpty(params())) {
            return PipeDataWrapper.error(errorPrefix() + "参数为空");
        }
        if (params().size() != 1) {
            return PipeDataWrapper.error(errorPrefix() + "仅支持1个参数");
        }
        String param = params().get(0);
        if (StringUtils.isBlank(param)) {
            return PipeDataWrapper.error(errorPrefix() + "参数为空");
        }
        param = param.trim();

        Double paramInt;
        Double valueInt;
        String unit = KB_UPPER;
        try {
            paramInt = getParamInt(param);
            if (value instanceof String) {
                String val = ((String) value).trim();
                if (!StringUtils.isNumeric(val)) {
                    if (val.endsWith(KB_UPPER) || val.endsWith(KB_LOWER)) {
                        val = val.substring(0, val.length()-2);
                        valueInt = NumberUtils.parseDouble(val, ExcelContentProperty.EMPTY)  * 1024;
                        unit = KB_UPPER;
                    } else if (val.endsWith(MB_UPPER) || val.endsWith(MB_LOWER)) {
                        val = val.substring(0, val.length()-2);
                        valueInt = NumberUtils.parseDouble(val, ExcelContentProperty.EMPTY)  * 1024  * 1024;
                        unit = MB_UPPER;
                    } else if (val.endsWith(GB_UPPER) || val.endsWith(GB_LOWER)) {
                        val = val.substring(0, val.length()-2);
                        valueInt = NumberUtils.parseDouble(val, ExcelContentProperty.EMPTY)  * 1024  * 1024 * 1024;
                        unit = GB_UPPER;
                    } else if (val.endsWith(B_UPPER) || val.endsWith(B_LOWER)) {
                        val = val.substring(0, val.length()-1);
                        valueInt = NumberUtils.parseDouble(val, ExcelContentProperty.EMPTY);
                        unit = KB_UPPER;
                    } else {
                        throw new RuntimeException(errorPrefix() + "传入数据类型应该是数字");
                    }
                } else {
                    valueInt = NumberUtils.parseDouble(val, ExcelContentProperty.EMPTY)  * 1024;
                }
            } else if (value instanceof Number) {
                valueInt = ((Number) value).doubleValue() * 1024;
            } else {
                throw new RuntimeException(errorPrefix() + "传入数据类型应该是数字");
            }
        } catch (NumberFormatException e) {
            log.warn(e.getMessage(), e);
            return PipeDataWrapper.error(errorPrefix() + "传入数据类型应该是数字");
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return PipeDataWrapper.error(e.getMessage());
        }
        if (valueInt > paramInt) {
            return PipeDataWrapper.error(errorPrefix() + String.format("传入数字:[%s]超过[%s]大小", value, params().get(0)));
        }
        return PipeDataWrapper.success(getResult(valueInt, unit));
    }

    private Double getParamInt(String param) throws ParseException {
        double paramInt;
        if (!StringUtils.isNumeric(param)) {
            if (param.endsWith(KB_UPPER) || param.endsWith(KB_LOWER)) {
                param = param.substring(0, param.length()-2);
                if (!param.matches(NUMBER_REG)) {
                    throw new RuntimeException(errorPrefix() + "参数去掉[KB/kb]后应该是数字");
                }
                paramInt = NumberUtils.parseDouble(param, ExcelContentProperty.EMPTY) * 1024;
            } else if (param.endsWith(MB_UPPER) || param.endsWith(MB_LOWER)) {
                param = param.substring(0, param.length()-2);
                if (!param.matches(NUMBER_REG)) {
                    throw new RuntimeException(errorPrefix() + "参数去掉[MB/mb]后应该是数字");
                }
                paramInt = NumberUtils.parseDouble(param, ExcelContentProperty.EMPTY) * 1024 *1024;
            } else if (param.endsWith(GB_UPPER) || param.endsWith(GB_LOWER)) {
                param = param.substring(0, param.length()-2);
                if (!param.matches(NUMBER_REG)) {
                    throw new RuntimeException(errorPrefix() + "参数去掉[GB/gb]后应该是数字");
                }
                paramInt = NumberUtils.parseDouble(param, ExcelContentProperty.EMPTY) * 1024 *1024 * 1024;
            } else if (param.endsWith(B_UPPER) || param.endsWith(B_LOWER)) {
                param = param.substring(0, param.length()-1);
                if (!param.matches(NUMBER_REG)) {
                    throw new RuntimeException(errorPrefix() + "参数去掉[B/b]后应该是数字");
                }
                paramInt = NumberUtils.parseDouble(param, ExcelContentProperty.EMPTY);
            } else {
                throw new RuntimeException(errorPrefix() + "参数类型应该是数字");
            }
        } else {
            paramInt = NumberUtils.parseDouble(param, ExcelContentProperty.EMPTY) * 1024;
        }
        return paramInt;
    }

    /**
     *
     * @param valueInt
     * @param unit
     * @return
     */
    private static String getResult(double valueInt, String unit) {
        String valueResult;
        switch (unit) {
            case MB_UPPER:
                valueResult = (valueInt / 1024 / 1024) + KB_UPPER;
                break;
            case GB_UPPER:
                valueResult = (valueInt / 1024 / 1024 / 1024) + KB_UPPER;
                break;
            default:
                valueResult = (valueInt / 1024) + KB_UPPER;
        }
        return valueResult;
    }
}
