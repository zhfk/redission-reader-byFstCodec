package com.zhfk.redissionreader.module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private int id;

    private String username;

    private String address;

    private int age;

}
