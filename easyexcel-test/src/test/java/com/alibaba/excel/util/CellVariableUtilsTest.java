package com.alibaba.excel.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/15 17:45
 */
class CellVariableUtilsTest {

    @Test
    void getVariable() {

        CellVariableUtils.getVariable("{.ProductCenter.test sdad}");
    }

    @Test
    void testVar() {
        List<String> variable = CellVariableUtils.getVariable("{.PhotoStore.main | prior-ends-with:m100-1.2.jpg}\n" +
            "{.PhotoStore.attach | prior-ends-with:m100-8.jpg | echo:wrap}{.PhotoStore.attach | prior-ends-with:m100-2.jpg,f1.jpg | echo:wrap}{.PhotoStore.attach | prior-ends-with:m100-3.jpg,f2.jpg | echo:wrap}{.PhotoStore.attach | prior-ends-with:m100-4.jpg,f3.jpg | echo:wrap}{.PhotoStore.attach | prior-ends-with:m100-5.jpg,f4.jpg | echo:wrap}{.PhotoStore.attach | prior-ends-with:m100-6.jpg,f5.jpg | echo:wrap}{.PhotoStore.attach | prior-ends-with:m100-7.jpg,f6.jpg}\n" +
            "{.PhotoStore.detail | prior-ends-with:m100-9.jpg | echo:warp}{.PhotoStore.detail | prior-ends-with:m100-10.jpg}");

        System.out.println(variable.size());
    }
}
