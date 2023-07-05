package com.alibaba.easyexcel.test.demo.fill;

import com.vevor.expression.filter.BasePipeFilter;
import com.vevor.expression.filter.PipeDataWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/17 11:40
 */
@Slf4j
public class ReplaceSpecialCharactersFilter extends BasePipeFilter<Object, Object> {

    private static final Map<String, String> REPLACE_MAP = new HashMap<>();

    static {
        REPLACE_MAP.put("è", "e");
        REPLACE_MAP.put("é", "e");
        REPLACE_MAP.put("ê", "e");
        REPLACE_MAP.put("à", "a");
        REPLACE_MAP.put("â", "a");
        REPLACE_MAP.put("û", "u");
        REPLACE_MAP.put("ù", "u");
        REPLACE_MAP.put("u'", "u");
        REPLACE_MAP.put("ç", "c");
        REPLACE_MAP.put("c'", "c");
        REPLACE_MAP.put("î", "i");
        REPLACE_MAP.put("ô", "o");
        REPLACE_MAP.put("d'", "de");
        REPLACE_MAP.put("l'", "l");
        REPLACE_MAP.put("É", "E");
        REPLACE_MAP.put("s'", "s");
    }

    @Override
    protected String filterName() {
        return "replace-special";
    }

    @Override
    public PipeDataWrapper<Object> apply(PipeDataWrapper<Object> wrapper) {

        // 验证
        if (!verify(wrapper)) {
            return wrapper;
        }

        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.error(errorPrefix() + "传入数据不能为空");
        }

        if (!(value instanceof String)) {
            return PipeDataWrapper.error(errorPrefix() + "指令输入数据不是字符串");
        }

        String text = ((String) value);
        for (Map.Entry<String, String> entry : REPLACE_MAP.entrySet()) {
            text = text.replaceAll(entry.getKey(), entry.getValue());
        }
        return PipeDataWrapper.success(text);
    }
}
