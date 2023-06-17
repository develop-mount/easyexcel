package com.alibaba.excel.write.metadata.fill.pipe;

import com.alibaba.excel.write.handler.PipeDataWrapper;
import com.alibaba.excel.write.handler.PipeFilterFactory;
import org.junit.jupiter.api.Test;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/28 11:33
 */
class PipeFilterFactoryTest {

    @Test
    void testSubstr() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter(null);
        pipeFilterFactory.addParams("test | substring : 0,10 ");
        System.out.println(pipeFilterFactory.apply(PipeDataWrapper.success("这个示例程序创建了两个 double 类型的变量 a 和 b，分别赋值为 10 和 5，然后调用 Calculator 类中的四个方法，输出运算结果。其中，divide 方法在除数为0时会抛出异常，这里使用了 try-catch 语句捕获异常，并输出错误信息。")));
    }
}
