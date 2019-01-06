package com.zhfk.redissionreader.module;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseData<T> {

    private int code;

    private String msg;

    private long count;

    private T data;

}
