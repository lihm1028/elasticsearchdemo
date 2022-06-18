package com.example.esdemo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

@Data
@Document(indexName = "book", createIndex = true, shards = 5)
public class BookIndex implements Serializable {


    /**
     * 主键
     */
    @Id
    @Field(type = FieldType.Keyword)
    private String id;


    /**
     *  Keyword 代表不分词
     *  Text 代表分词
     *
     */
    @Field(type = FieldType.Text)
    private String title;


    /**
     * 名称
     */
    @Field(type = FieldType.Text)
    private String name;

    /**
     * 编号
     */
    @Field(type = FieldType.Keyword)
    private String code;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 作者
     * 不指定FieldType默认生成
     *   "author":{
     *                     "type":"text",
     *                     "fields":{
     *                         "keyword":{
     *                             "type":"keyword",
     *                             "ignore_above":256
     *                         }
     *                     }
     *                 }
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


    /**
     * 更新时间戳
     */
    @Field(type = FieldType.Long)
    private Long updateTime = System.currentTimeMillis();

    public BookIndex() {
    }

    @Override
    public String toString() {
        return "BookIndex{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", email='" + email + '\'' +
                ", author='" + author + '\'' +
                ", date=" + date +
                ", price=" + price +
                ", createTime=" + createTime +
                '}';
    }
}
