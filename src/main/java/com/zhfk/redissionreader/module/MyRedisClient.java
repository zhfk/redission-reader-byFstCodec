package com.zhfk.redissionreader.module;

import lombok.Data;


@Data
public class MyRedisClient {

    private String host;

    private int port;

    private String pass;

    private int db;

}
