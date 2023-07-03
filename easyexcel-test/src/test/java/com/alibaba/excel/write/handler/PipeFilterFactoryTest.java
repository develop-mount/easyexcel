package com.alibaba.excel.write.handler;

import com.alibaba.easyexcel.test.demo.fill.FillData;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/17 13:02
 */
class PipeFilterFactoryTest {

    private List<FillData> dataPipe() {
        List<FillData> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            FillData fillData = new FillData();
            list.add(fillData);
            fillData.setName(" 张三 ");
            fillData.setNumber(5.2);
            fillData.setDate(new Date());
            if (i == 0) {
                fillData.setImages(Arrays.asList("http://www.baidu.com/images/m100-1.1.jpg"
                    , "http://www.baidu.com/images/m100-1.2.jpg"
                    , "http://www.baidu.com/images/m100-1.3.jpg"
                    , "http://www.baidu.com/images/m100-1.4.jpg"
                    , "http://www.baidu.com/images/m100-1.5.jpg"));
            } else {
                fillData.setImages(Arrays.asList("http://www.baidu.com/images/m100-1.1.jpg"
                    , "http://www.baidu.com/images/m100-1.2.jpg"
                    , "http://www.baidu.com/images/m100-1.3.jpg"
                    , "http://www.baidu.com/images/m100-1.4.jpg"
                    , "http://www.baidu.com/images/m100-1.5.jpg"
                    , "http://www.baidu.com/images/K100-1.2.jpg"));
            }
        }
        return list;
    }

    @Test
    void testEndsWithError() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter(null);
        pipeFilterFactory.addParams("test | ends-with :  ");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(Arrays.asList("http://www.baidu.com/images/m100-1.1.jpg"
            , "http://www.baidu.com/images/m100-1.2.jpg"
            , "http://www.baidu.com/images/m100-1.3.jpg"
            , "http://www.baidu.com/images/m100-1.4.jpg"
            , "http://www.baidu.com/images/m100-1.5.jpg"
            , "http://www.baidu.com/images/K100-1.2.jpg")));
        System.out.println(apply.getMessage());
        Assert.isTrue(!apply.success(), "成功");
    }

    @Test
    void testEndsWith() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter(null);
        pipeFilterFactory.addParams("test | ends-with : m100-1.3.jpg,m100-1.5.jpg,m100-1.4.jpg ");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(Arrays.asList("http://www.baidu.com/images/m100-1.1.jpg"
            , "http://www.baidu.com/images/m100-1.2.jpg"
            , "http://www.baidu.com/images/m100-1.3.jpg"
            , "http://www.baidu.com/images/m100-1.4.jpg"
            , "http://www.baidu.com/images/m100-1.5.jpg"
            , "http://www.baidu.com/images/K100-1.2.jpg")));
        Assert.isTrue(apply.success(), "失败");
    }

    @Test
    void testProEndsWith() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter(null);
        pipeFilterFactory.addParams("test | prior-ends-with : m100-1.3.jpg,m100-1.5.jpg,m100-1.4.jpg ");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(Arrays.asList("http://www.baidu.com/images/m100-1.1.jpg"
            , "http://www.baidu.com/images/m100-1.2.jpg"
            , "http://www.baidu.com/images/m100-1.3.jpg"
            , "http://www.baidu.com/images/m100-1.4.jpg"
            , "http://www.baidu.com/images/m100-1.5.jpg"
            , "http://www.baidu.com/images/K100-1.2.jpg")));
        Assert.isTrue(apply.success(), "失败");
    }


    @Test
    void testSubstr() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter(null);
        pipeFilterFactory.addParams("test | substring : 0,10 ");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success("这个示例程序创建了两个 double 类型的变量 a 和 b，分别赋值为 10 和 5，然后调用 Calculator 类中的四个方法，输出运算结果。其中，divide 方法在除数为0时会抛出异常，这里使用了 try-catch 语句捕获异常，并输出错误信息。"));
        Assert.isTrue(apply.success(), "失败");
    }

    @Test
    void testEquals() {
//
        Map<String, Object> titleMap = new HashMap<>(2);
        titleMap.put("eBayTitle", "eBayTitle这个示例程序创建了两个 double 类型的变量 a 和 b，分别赋值为 10 和 5，然后调用 Calculator 类中的四个方法，输出运算结果");
        titleMap.put("amazonTitle", "amazonTitle其中，divide 方法在除数为0时会抛出异常，这里使用了 try-catch 语句捕获异常，并输出错误信息。");
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter(null);
        pipeFilterFactory.addParams("PublishCenter.titleMap | equals:amazonTitle,eBayTitle");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(titleMap));
        Assert.isTrue(apply.success() && apply.getData().toString().startsWith("amazonTitle"), "失败");
    }

    @Test
    void testNumber() {
//        ebayManno.price | cal-mul:1.4,int | cal-add:0.99,number_2
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter(null);
        pipeFilterFactory.addParams("ebayManno.price | cal-mul:1.4,int | cal-add:0.99,number_2");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success("34"));
        Assert.isTrue(apply.success() && apply.getData().toString().equals("47.99"), "失败");
    }

    @Test
    void testEcho() {
//        PhotoStore.attach | prior-ends-with:m100-8.jpg | echo:wrap
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter(null);
        pipeFilterFactory.addParams("ebayManno.price | prior-ends-with:m100-1.4.jpg | echo:wrap");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(Arrays.asList("http://www.baidu.com/images/m100-1.1.jpg"
            , "http://www.baidu.com/images/m100-1.2.jpg"
            , "http://www.baidu.com/images/m100-1.3.jpg"
            , "http://www.baidu.com/images/m100-1.4.jpg"
            , "http://www.baidu.com/images/m100-1.5.jpg"
            , "http://www.baidu.com/images/K100-1.2.jpg")));
        Assert.isTrue(apply.success() && apply.getData().toString().endsWith("\n"), "失败");
    }

    @Test
    void testEchoNone() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter(null);
        pipeFilterFactory.addParams("ebayManno.price | prior-ends-with:m100-1.4.jpg | echo:wrap");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(Arrays.asList("http://www.baidu.com/images/m100-1.1.jpg"
            , "http://www.baidu.com/images/m100-1.2.jpg"
            , "http://www.baidu.com/images/m100-1.3.jpg"
            , "http://www.baidu.com/images/m100-1.5.jpg"
            , "http://www.baidu.com/images/K100-1.2.jpg")));
        Assert.isTrue(apply.success() && apply.getData().toString().endsWith("\n"), "失败");
    }

    @Test
    void testListEcho() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter(null);
        pipeFilterFactory.addParams("ebayManno.price | list-echo:wrap");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(Arrays.asList("http://www.baidu.com/images/m100-1.1.jpg"
            , "http://www.baidu.com/images/m100-1.2.jpg"
            , "http://www.baidu.com/images/m100-1.3.jpg"
            , "http://www.baidu.com/images/m100-1.5.jpg"
            , "http://www.baidu.com/images/K100-1.2.jpg")));
        Assert.isTrue(apply.success(), "失败");
    }

    @Test
    void testReplace() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter(null);
        pipeFilterFactory.addParams("ebayManno.price | replace:è,e,1 | replace:é,e,1 | replace:ê,e,1 | replace:à,a,1");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success("sdès,èu,àê"));
        Assert.isTrue(apply.success(), "失败");
    }

    @Test
    void testConfition() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter(null);
        pipeFilterFactory.addParams("ebayManno.price | condition-echo:是,Yes,No");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success("是"));
        Assert.isTrue(apply.success(), "失败");
    }

    @Test
    void testProEndsWithMap() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter(null);
        pipeFilterFactory.addParams("test | ends-with : m100-1.1.jpg | prior-equals:size | max-size:10000");

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("url", "http://www.baidu.com/images/m100-1.1.jpg");
        dataMap.put("size", 123123);
        Map<String, Object> dMap = new HashMap<>();
        dMap.put("http://www.baidu.com/images/m100-1.1.jpg", dataMap);

        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(Arrays.asList(dMap)));
        Assert.isTrue(apply.success(), "失败");
    }

    @Test
    void testMust() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter(null);
        pipeFilterFactory.addParams("ebayManno.price | must");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(""));
        Assert.isTrue(apply.success(), "失败");
    }

    @Test
    void testJson() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter(null);
        pipeFilterFactory.addParams("ebayManno.price | prior-ends-with:cover-f1.jpg | prior-equals:fileSize");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(JSON.parseArray(data)));
        Assert.isTrue(apply.success(), "失败");
    }

    @Test
    void testList() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter(null);
        pipeFilterFactory.addParams("PublishCenter.Bullet Point.bulletPoints | list-index:6");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success(JSON.parseArray(data1)));
        Assert.isTrue(apply.success(), "失败");
    }

    @Test
    void testMaxSiz() {
        PipeFilterFactory pipeFilterFactory = PipeFilterFactory.createPipeFilter(null);
        pipeFilterFactory.addParams("PublishCenter.Bullet Point.bulletPoints | max-size:5");
        val apply = pipeFilterFactory.apply(PipeDataWrapper.success("3MB"));
        Assert.isTrue(apply.success(), "失败");
    }

    private static String data1 = "[\n" +
        "    \"Heavy-Duty Frame and Bags: Simplify your laundry routine with the remarkable laundry sorter cart. Its heavy-duty 600D Oxford cloth bags are waterproof and easy to clean, while the thickened metal frame and 6 horizontal bars provide enhanced stability.\",\n" +
        "    \"Ample Storage Capacity: Each heavy-duty bag holds up to 25 lbs of clothes, providing ample space for clothes, towels, bedding, and other laundry items, keeping them organized and easily accessible. The basket serves as a practical storage solution when not used for carrying laundry.\",\n" +
        "    \"3 Sections for Easy Sorting: This laundry basket is divided into 3 sections of different colors, allowing you to easily sort your laundry by whites, darks, and grey, making it convenient to keep your clothes organized.\",\n" +
        "    \"Easy to Move Around: Equipped with 4 smooth-rolling casters, our laundry hamper with wheels ensures effortless movement, making it easy to pick up laundry from every room. Two locking casters provide stability when parked, and the foamed handle on the laundry cart offers a comfortable grip, ensuring ease of maneuverability.\",\n" +
        "    \"Ultra-practical Laundry Cart: The VEVOR laundry sorter cart offers the perfect solution for all your laundry needs, ensuring everything is organized and easily accessible. Whether it's for your laundry room, bedroom, entryway, living room, small apartment, or dorm, this cart is an excellent choice to streamline your laundry routine.\"\n" +
        "]";

    private static String data = "[\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f1.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-f1.jpg\",\n" +
        "            \"fileSize\": \"1.61MB\",\n" +
        "            \"ruleName\": \"kw-f1\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f1.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-fb1.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-fb1.jpg\",\n" +
        "            \"fileSize\": \"372.06KB\",\n" +
        "            \"ruleName\": \"kw-fb1\",\n" +
        "            \"fileParameter\": \"1080x1080\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-fb1.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-x1.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-x1.jpg\",\n" +
        "            \"fileSize\": \"223.90KB\",\n" +
        "            \"ruleName\": \"kw-x1\",\n" +
        "            \"fileParameter\": \"750x1000\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-x1.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f2.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-f2.jpg\",\n" +
        "            \"fileSize\": \"1.18MB\",\n" +
        "            \"ruleName\": \"kw-f2\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f2.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f3.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-f3.jpg\",\n" +
        "            \"fileSize\": \"896.78KB\",\n" +
        "            \"ruleName\": \"kw-f3\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f3.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f4.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-f4.jpg\",\n" +
        "            \"fileSize\": \"1.14MB\",\n" +
        "            \"ruleName\": \"kw-f4\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f4.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f5.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-f5.jpg\",\n" +
        "            \"fileSize\": \"1004.00KB\",\n" +
        "            \"ruleName\": \"kw-f5\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f5.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f6.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-f6.jpg\",\n" +
        "            \"fileSize\": \"866.75KB\",\n" +
        "            \"ruleName\": \"kw-f6\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-f6.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/Chair-Cover-a100-1.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/Chair-Cover-a100-1.jpg\",\n" +
        "            \"fileSize\": \"126.27KB\",\n" +
        "            \"ruleName\": \"kw-a100-1\",\n" +
        "            \"fileParameter\": \"970x300\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/Chair-Cover-a100-1.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-2.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-2.jpg\",\n" +
        "            \"fileSize\": \"492.56KB\",\n" +
        "            \"ruleName\": \"kw-m100-2\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-2.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-2.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-a100-2.jpg\",\n" +
        "            \"fileSize\": \"50.24KB\",\n" +
        "            \"ruleName\": \"kw-a100-2\",\n" +
        "            \"fileParameter\": \"300x400\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-2.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-3.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-3.jpg\",\n" +
        "            \"fileSize\": \"222.59KB\",\n" +
        "            \"ruleName\": \"kw-m100-3\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-3.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-3.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-a100-3.jpg\",\n" +
        "            \"fileSize\": \"20.48KB\",\n" +
        "            \"ruleName\": \"kw-a100-3\",\n" +
        "            \"fileParameter\": \"350x175\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-3.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-4.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-4.jpg\",\n" +
        "            \"fileSize\": \"628.98KB\",\n" +
        "            \"ruleName\": \"kw-m100-4\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-4.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-5.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-5.jpg\",\n" +
        "            \"fileSize\": \"433.01KB\",\n" +
        "            \"ruleName\": \"kw-m100-5\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-5.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-6.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-6.jpg\",\n" +
        "            \"fileSize\": \"548.50KB\",\n" +
        "            \"ruleName\": \"kw-m100-6\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-6.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-7.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-7.jpg\",\n" +
        "            \"fileSize\": \"1.09MB\",\n" +
        "            \"ruleName\": \"kw-m100-7\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-7.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-8.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-8.jpg\",\n" +
        "            \"fileSize\": \"778.60KB\",\n" +
        "            \"ruleName\": \"kw-m100-8\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-8.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-9.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-9.jpg\",\n" +
        "            \"fileSize\": \"653.71KB\",\n" +
        "            \"ruleName\": \"kw-m100-9\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-9.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-10.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-10.jpg\",\n" +
        "            \"fileSize\": \"575.17KB\",\n" +
        "            \"ruleName\": \"kw-m100-10\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-10.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-1.1.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-1.1.jpg\",\n" +
        "            \"fileSize\": \"1.48MB\",\n" +
        "            \"ruleName\": \"kw-m100-1.1\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-1.1.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-11.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-11.jpg\",\n" +
        "            \"fileSize\": \"587.81KB\",\n" +
        "            \"ruleName\": \"kw-m100-11\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-11.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-1.2.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-1.2.jpg\",\n" +
        "            \"fileSize\": \"89.29KB\",\n" +
        "            \"ruleName\": \"kw-m100-1.2\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-1.2.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-12.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-m100-12.jpg\",\n" +
        "            \"fileSize\": \"623.69KB\",\n" +
        "            \"ruleName\": \"kw-m100-12\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-m100-12.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/Chair-Cover-m100-1.3.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/Chair-Cover-m100-1.3.jpg\",\n" +
        "            \"fileSize\": \"238.14KB\",\n" +
        "            \"ruleName\": \"kw-m100-1.3\",\n" +
        "            \"fileParameter\": \"1600x1600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/Chair-Cover-m100-1.3.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.4.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-a100-1.4.jpg\",\n" +
        "            \"fileSize\": \"270.80KB\",\n" +
        "            \"ruleName\": \"kw-a100-1.4\",\n" +
        "            \"fileParameter\": \"970x600\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.4.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.6.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-a100-1.6.jpg\",\n" +
        "            \"fileSize\": \"302.29KB\",\n" +
        "            \"ruleName\": \"kw-a100-1.6\",\n" +
        "            \"fileParameter\": \"1200x628\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.6.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.9.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-a100-1.9.jpg\",\n" +
        "            \"fileSize\": \"719.63KB\",\n" +
        "            \"ruleName\": \"kw-a100-1.9\",\n" +
        "            \"fileParameter\": \"1920x1080\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.9.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.92.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-a100-1.92.jpg\",\n" +
        "            \"fileSize\": \"739.98KB\",\n" +
        "            \"ruleName\": \"kw-a100-1.92\",\n" +
        "            \"fileParameter\": \"1920x1080\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.92.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.93.jpg\": {\n" +
        "            \"scaleAttUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/small/chair-cover-a100-1.93.jpg\",\n" +
        "            \"fileSize\": \"126.50KB\",\n" +
        "            \"ruleName\": \"kw-a100-1.93\",\n" +
        "            \"fileParameter\": \"640x360\",\n" +
        "            \"mainUrl\": \"https://d2qc09rl1gfuof.cloudfront.net/product/50TQBBSYT00000001/chair-cover-a100-1.93.jpg\",\n" +
        "            \"fileType\": \"jpg\"\n" +
        "        }\n" +
        "    }\n" +
        "]";
}
