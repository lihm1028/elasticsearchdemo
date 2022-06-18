package com.example.esdemo.service.impl;

import com.example.esdemo.model.BookIndex;
import com.example.esdemo.repository.BookIndexRepository;
import com.example.esdemo.service.BookService;
import com.example.esdemo.service.impl.term.BookTerm;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookIndexRepository indexRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;


    @Override
    public BookIndex index(BookIndex index) {
        return indexRepository.save(index);
    }

    @Override
    public BookIndex findById(String id) {
        final Optional<BookIndex> optional = indexRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public long countByAuthor(String author) {
        return indexRepository.countByAuthor(author);
    }

    @Override
    public Page<BookIndex> findAll(Pageable pageable) {
        return indexRepository.findAll(pageable);
    }

    @Override
    public Page<BookIndex> search(BookTerm term, Pageable pageable) {


        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();


        BoolQueryBuilder bool = QueryBuilders.boolQuery();

        if (StringUtils.isNotBlank(term.getKeyword())) {
            bool.must(QueryBuilders.queryStringQuery(term.getKeyword()).defaultField("name"));
//            bool.must(QueryBuilders.queryStringQuery(term.getKeyword())
//                    .field("name", 1.0F)
//                    .field("code", 1.0F));
        }

        if (StringUtils.isNotBlank(term.getName())) {
            /**
             * wildcardQuery 这里需要自己传规则 *
             */
            bool.must(QueryBuilders.wildcardQuery("name", "*".concat(term.getName()) + "*"));
        }

        if (StringUtils.isNotBlank(term.getAuthor())) {
            bool.must(QueryBuilders.termQuery("author", term.getAuthor()));
        }
        if (StringUtils.isNotBlank(term.getCode())) {
            bool.must(QueryBuilders.termQuery("code", term.getCode()));
        }

        if (term.getStartTime() != null) {
            bool.must(QueryBuilders.rangeQuery("createTime").from(term.getStartTime()));
        }
        if (term.getEndTime() != null) {
            bool.must(QueryBuilders.rangeQuery("createTime").to(term.getEndTime()));
        }


        /**
         * 构建一个NativeSearchQueryBuilder对象
         */
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // 排序
        if (StringUtils.isBlank(term.getKeyword())) {
            queryBuilder.withSorts(SortBuilders.fieldSort("createTime").order(SortOrder.ASC));
        }

        final NativeSearchQuery build = queryBuilder.withQuery(bool)
                .withPageable(pageable)
                .build();


        final SearchHits<BookIndex> searchHits = elasticsearchOperations.search(build, BookIndex.class);

        /**
         * 返回分页对象
         */
        List<BookIndex> contents = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
        Page<BookIndex> page = new PageImpl<>(contents, pageable, searchHits.getTotalHits());
        return page;
    }

    @Override
    public BookIndex findByCode(String code) {
        return indexRepository.findByCode(code);
    }

    @Override
    public boolean deleteById(String id) {
        indexRepository.deleteById(id);
        return true;
    }
}
