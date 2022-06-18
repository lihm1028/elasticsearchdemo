package com.example.esdemo.service.impl.term;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BookTerm implements Serializable {

    private String keyword;

    /**
     * 名称
     */
    private String name;

    /**
     * 编号
     */
    private String code;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 作者
     */
    private String author;

    private Date startTime;


    private Date endTime;


}
