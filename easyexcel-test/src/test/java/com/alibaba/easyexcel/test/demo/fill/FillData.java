package com.alibaba.easyexcel.test.demo.fill;

import java.util.Date;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jiaju Zhuang
 */
@Getter
@Setter
@EqualsAndHashCode
public class FillData {
    private List<String> bulletPoints;
    private String name;
    private double number;
    private double test1;
    private double test2;
    private Date date;
    private List<String> images;
    private String error;
}
