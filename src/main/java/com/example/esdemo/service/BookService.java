package com.example.esdemo.service;

import com.example.esdemo.model.BookIndex;
import com.example.esdemo.service.impl.term.BookTerm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {


    BookIndex index(BookIndex index);

    BookIndex findById(String id);


    long countByAuthor(String author);


    Page<BookIndex> findAll(Pageable pageable);


    /**
     * 搜索
     *
     * @param term
     * @param pageable
     * @return
     */
    Page<BookIndex> search(BookTerm term, Pageable pageable);

    BookIndex findByCode(String code);

    boolean deleteById(String id);


}
