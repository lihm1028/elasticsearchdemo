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
        index.setAuthor("老子");
        index.setCode("daodejing" + rank);
        index.setKeyword("道德、老子" + rank);
        index.setCreateTime(new Date());
        index.setDate(new Date());
        index.setEmail("laozi@chunqiu.com");
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


    @GetMapping("/search")
    public Page<BookIndex> search(BookTerm term, Pageable pageable) {
        return bookService.search(term, pageable);
    }


}
