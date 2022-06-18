package com.example.esdemo.controller;

import com.example.esdemo.model.BookIndex;
import com.example.esdemo.service.BookService;
import com.example.esdemo.service.impl.term.BookTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static AtomicInteger counter = new AtomicInteger();
    @Autowired
    BookService bookService;

    @GetMapping("/add")
    public BookIndex store() {
        BookIndex index = new BookIndex();
        index.setId(UUID.randomUUID().toString());

        int rank = counter.incrementAndGet();

        index.setName("道德经" + rank);
        index.setAuthor("李耳");
        index.setCode("daodejing" + rank);
        index.setTitle("道德经老子道家学派创始人" + rank);
        index.setCreateTime(new Date());
        index.setDate(new Date());
        index.setEmail("laozi@chunqiu.com");
        index.setPrice(188 + rank);
        return bookService.index(index);

    }

    @GetMapping("/add2")
    public BookIndex store2() {
        BookIndex index = new BookIndex();
        index.setId(UUID.randomUUID().toString());

        int rank = counter.incrementAndGet();

        index.setName("庄子" + rank);
        index.setAuthor("庄周");
        index.setCode("zhuangzi" + rank);
        index.setTitle("逍遥游、齐物论、养生主" + rank);
        index.setCreateTime(new Date());
        index.setDate(new Date());
        index.setEmail("zhuangzi@zhanguo.com");
        index.setPrice(188 + rank);
        return bookService.index(index);

    }


    @GetMapping("/{id}")
    public BookIndex findById(@PathVariable String id) {
        return bookService.findById(id);
    }


    @GetMapping("/delete/{id}")
    public boolean deleteById(@PathVariable String id) {
        return bookService.deleteById(id);
    }

    @GetMapping("/removeAll")
    public boolean removeAll() {
        return bookService.removeAll();
    }


    @GetMapping("/search")
    public Page<BookIndex> search(BookTerm term, Pageable pageable) {
        return bookService.search(term, pageable);
    }

    @GetMapping("/search2")
    public Page<BookIndex> search2(BookTerm term, Pageable pageable) {
        return bookService.search2(term, pageable);
    }

    @GetMapping("/search3")
    public Page<BookIndex> search3(BookTerm term, Pageable pageable) {
        return bookService.search3(term, pageable);
    }


}
