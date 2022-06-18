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
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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


        BoolQueryBuilder bool = QueryBuilders.boolQuery();

        if (StringUtils.isNotBlank(term.getKeyword())) {
            /**
             * 查询title字段中，会将输入keyword拆词
             * 相当于把"道德老子1"分词了，再查询；
             * 不指定feild，查询范围为所有feild
             *
             * 指定多个queryStringQuery("道德").field().field()
             */
            bool.must(QueryBuilders.queryStringQuery(term.getKeyword()).defaultField("title"));

//            bool.must(QueryBuilders.queryStringQuery(term.getKeyword())
//                    .field("name", 1.0F)
//                    .field("code", 1.0F));


            //termQuery精确查询
//            bool.must(QueryBuilders.termQuery("title", term.getKeyword()));


            // 关键字支持分词
//            bool.must(QueryBuilders.matchQuery("title", term.getKeyword()));

            // 关键字支持分词 匹配多个字段
//            bool.must(QueryBuilders.multiMatchQuery(term.getKeyword(), "title", "name", "author"));


            /**
             *  模糊查询
             *  模糊，是指查询关键字与目标关键字可以模糊匹配。
             *  1.左右模糊查询，其中fuzziness的参数作用是在查询时，es动态的将查询关键词前后增加或者删除一个词，然后进行匹配
             */
//            bool.must(QueryBuilders.fuzzyQuery("title", term.getKeyword()).fuzziness(Fuzziness.AUTO));


            /**
             * 2.前缀查询；
             */
//            bool.must(QueryBuilders.prefixQuery("title", term.getKeyword()));


            /**
             *  3.通配符查询，支持*和?，?表示单个字符；注意不建议将通配符作为前缀，否则导致查询很慢.
             */
//            bool.must(QueryBuilders.wildcardQuery("title", term.getKeyword() + "*"));
//            bool.must(QueryBuilders.wildcardQuery("title", "道?经"));


        }

        if (StringUtils.isNotBlank(term.getName())) {
            /**
             * 精确匹配
             */
            bool.must(QueryBuilders.termQuery("name", term.getName()));
        }

        if (StringUtils.isNotBlank(term.getAuthor())) {
//            bool.must(QueryBuilders.termQuery("author", term.getAuthor()));
            bool.must(QueryBuilders.termQuery("author.keyword", term.getAuthor()));
        }
        if (!CollectionUtils.isEmpty(term.getCodes())) {
            bool.must(QueryBuilders.termsQuery("code", term.getCodes()));
        }

        if (term.getStartTime() != null) {
            bool.must(QueryBuilders.rangeQuery("createTime").from(term.getStartTime()));
        }
        if (term.getEndTime() != null) {
            bool.must(QueryBuilders.rangeQuery("createTime").to(term.getEndTime()));
        }

        if (term.getStartPrice() != null && term.getEndPrice() != null) {
            bool.must(QueryBuilders.rangeQuery("price").from(term.getStartPrice()).to(term.getEndPrice()));
        }


        /**
         * 构建一个NativeSearchQueryBuilder对象
         * NativeSearchQuery是当您有复杂查询或无法使用CriteriaAPI 表达的查询时使用的类，例如在构建查询和使用聚合时。
         * 它允许使用QueryBuilderElasticsearch 库中的所有不同实现，因此命名为“native”。
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
    public Page<BookIndex> search2(BookTerm term, Pageable pageable) {

        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(term.getKeyword())) {


            /**
             *
             * 2.表达式
             */
            criteria.and(new Criteria("title").startsWith(term.getKeyword()));
//            criteria.and(new Criteria("title").endsWith(term.getKeyword()));
//            criteria.and(new Criteria("title").expression("*".concat(term.getKeyword())));

            /**
             * 有效
             */
//           criteria.and(new Criteria("title").expression("*".concat(term.getKeyword()) + "*"));

        }

        if (StringUtils.isNotBlank(term.getName())) {

            /**
             *
             * 2.表达式
             */
            criteria.and(new Criteria("name").startsWith(term.getName()));

        }

        if (term.getStartPrice() != null) {
            criteria.and(new Criteria("price").greaterThanEqual(term.getStartPrice()).lessThanEqual(term.getEndPrice()));
        }
        if (StringUtils.isNotBlank(term.getAuthor())) {
            criteria.and(new Criteria("author").is(term.getAuthor()));
        }
        if (!CollectionUtils.isEmpty(term.getCodes())) {
            criteria.and(new Criteria("code").in(term.getCodes()));
        }
        if (term.getStartTime() != null) {
            criteria.and(new Criteria("createTime").greaterThanEqual(term.getStartTime()));
        }
        if (term.getEndTime() != null) {
            criteria.and(new Criteria("createTime").lessThanEqual(term.getEndTime()));
        }
        Query query = new CriteriaQuery(criteria);
        if (StringUtils.isBlank(term.getKeyword())) {
            query.addSort(Sort.by(Sort.Direction.DESC, "createTime"));
        }


        final SearchHits<BookIndex> searchHits = elasticsearchOperations.search(query, BookIndex.class);

        /**
         * 返回分页对象
         */
        List<BookIndex> contents = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
        Page<BookIndex> page = new PageImpl<>(contents, pageable, searchHits.getTotalHits());
        return page;
    }

    @Override
    public Page<BookIndex> search3(BookTerm term, Pageable pageable) {


        final String source = "{\"bool\":{\"must\": [{ \"match\": { \"name\": { \"query\": \"*" + term.getName() + "\" } } },{\"range\":{\"price\":{\"gte\":189,\"lte\":190}}} ]}} ";


        Query query = new StringQuery(source, pageable);
        if (StringUtils.isBlank(term.getKeyword())) {
            query.addSort(Sort.by(Sort.Direction.DESC, "createTime"));
        }


        final SearchHits<BookIndex> searchHits = elasticsearchOperations.search(query, BookIndex.class);
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

    @Override
    public boolean removeAll() {
        indexRepository.deleteAll();
        return true;
    }
}
