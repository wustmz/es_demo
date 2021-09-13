package com.example;

import com.example.dao.ProductDao;
import com.example.domain.Product;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class EsDemoApplicationTests {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private ProductDao productDao;


    //创建索引并增加映射配置
    @Test
    public void createIndex() {
        System.out.println("创建索引");
    }

    @Test
    public void deleteIndex() {
        //创建索引，系统初始化会自动创建索引
        boolean flg = elasticsearchRestTemplate.deleteIndex(Product.class);
        System.out.println("删除索引 = " + flg);
    }

    @Test
    public void existIndex() {
        boolean exists = elasticsearchRestTemplate.indexExists("product");
        System.out.print("是否存在: " + exists);
    }

    @Test
    public void testSave() {
        Product product = new Product();
        product.setId(2L);
        product.setTitle("苹果手机");
        product.setCategory("手机");
        product.setPrice(5999.0);
        product.setImages("www.baidu.com");
        productDao.save(product);
    }


    @Test
    public void testUpdate() {
        Product product = new Product();
        //id相同再次保存即是修改
        product.setId(2L);
        product.setTitle("小米手机");
        product.setCategory("手机");
        product.setPrice(2999.0);
        product.setImages("www.baidu.com");
        productDao.save(product);
    }

    @Test
    public void testGetById() {
        Product product = productDao.findById(2L).get();
        System.out.println(product);
    }

    @Test
    public void testGetAll() {
        Iterable<Product> products = productDao.findAll();
        for (Product product : products) {
            System.out.println(product);
        }
    }

    @Test
    public void testDelete() {
        Product product = new Product();
        product.setId(2L);
        productDao.delete(product);
    }

    @Test
    public void testSaveAll() {
        List<Product> productList = new ArrayList<>();
        Product product;
        for (int i = 0; i < 2; i++) {
            product = new Product();
            product.setId(i + 3L);
            product.setTitle("华为手机" + i);
            product.setCategory("手机");
            product.setPrice(3999.0);
            product.setImages("www.baidu.com");
            productList.add(product);
        }
        productDao.saveAll(productList);
    }

    @Test
    public void testPageable() {
        //分页查询
        //设置排序
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        //当前页,第一页从0开始,1表示第二页
        int from = 0;
        //每页显示多少条
        int size = 5;
        //设置查询分页
        PageRequest pageRequest = PageRequest.of(from, size, sort);
        //分页查询
        Page<Product> productPage = productDao.findAll(pageRequest);
        for (Product product : productPage.getContent()) {
            System.out.println(product);
        }
    }

    @Test
    public void termQuery() {
        //term查询
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", "小米");
        Iterable<Product> products = productDao.search(termQueryBuilder);
        for (Product product : products) {
            System.out.println(product);
        }
    }

    @Test
    public void termQueryByPage() {
        int from = 0;
        int size = 5;
        PageRequest pageRequest = PageRequest.of(from, size);
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", "小米");
        Page<Product> products = productDao.search(termQueryBuilder, pageRequest);
        for (Product product : products) {
            System.out.println(product);
        }
    }

}
