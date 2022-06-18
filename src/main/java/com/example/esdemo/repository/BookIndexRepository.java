package com.example.esdemo.repository;

import com.example.esdemo.model.BookIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface BookIndexRepository extends ElasticsearchRepository<BookIndex, String> {


    @Override
    Optional<BookIndex> findById(String s);


    long countByAuthor(String author);

    @Override
    Page<BookIndex> findAll(Pageable pageable);

    BookIndex findByCode(String code);

}
