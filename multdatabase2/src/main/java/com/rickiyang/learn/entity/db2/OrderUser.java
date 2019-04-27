package com.rickiyang.learn.entity.db2;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderUser {
    private Long id;

    private String name;

    private Byte sex;

    private String address1;

    private String address2;
}