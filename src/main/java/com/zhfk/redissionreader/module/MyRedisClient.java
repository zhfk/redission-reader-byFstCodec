package com.zhfk.redissionreader.module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyRedisClient {

    private String host;

    private int port;

    private String pass;

    private int db;

}
