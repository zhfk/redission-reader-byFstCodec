package com.zhfk.redissionreader.module;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyKeyValue {
    private int index;
    private String value;
    private Double score;
}
