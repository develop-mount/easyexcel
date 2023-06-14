package com.alibaba.excel.write.handler.filter;

import com.alibaba.excel.util.DateUtils;
import com.alibaba.excel.util.PipeFilterUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.handler.BasePipeFilter;

import java.util.Date;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/30 10:11
 */
public class DateFormatFilter extends BasePipeFilter<Object, Object> {

    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Object apply(Object value) {

        if (value instanceof Date) {

            String format;
            if (PipeFilterUtils.isEmpty(params())) {
                format = DEFAULT_FORMAT;
            } else {
                format = StringUtils.isBlank(params().get(0)) ? DEFAULT_FORMAT : params().get(0);
            }
            Date date = (Date) value;
            return DateUtils.format(date, format);
        } else if (value instanceof String) {

            return value;
        }
        throw new RuntimeException("错误:date-format指令传入数据不是Date或字符串");
    }
}
