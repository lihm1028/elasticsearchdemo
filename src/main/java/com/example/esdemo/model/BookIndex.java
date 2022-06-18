package com.example.esdemo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

@Data
@Document(indexName = "book",createIndex = true)
public class BookIndex implements Serializable {


    /**
     * 主键
     */
    @Id
    @Field(type = FieldType.Text)
    private String id;

    @Field(type = FieldType.Keyword)
    private String keyword;


    /**
     * 名称
     */
    @Field(type = FieldType.Text)
    private String name;

    /**
     * 编号
     */
    private String code;

    /**
     * 邮箱
     */
    @Field(type = FieldType.Text)
    private String email;

    /**
     * 作者
     */
    private String author;

    @Field(type = FieldType.Integer)
    private Integer price;


    /**
     * 日期
     */
    @Field(type = FieldType.Date)
    private Date date;


    /**
     * 创建日期
     */
    private Date createTime;

    public BookIndex() {
    }

    @Override
    public String toString() {
        return "BookIndex{" +
                "id='" + id + '\'' +
                ", keyword='" + keyword + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", email='" + email + '\'' +
                ", author='" + author + '\'' +
                ", date=" + date +
                ", createTime=" + createTime +
                '}';
    }
}
