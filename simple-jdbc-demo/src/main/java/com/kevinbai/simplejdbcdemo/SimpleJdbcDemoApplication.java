package com.kevinbai.simplejdbcdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleJdbcDemoApplication implements CommandLineRunner {

    @Autowired
    private FooDao fooDao;

    public static void main(String[] args) {
        SpringApplication.run(SimpleJdbcDemoApplication.class, args);
    }

    @Override
    public void run(String[] args) {
        fooDao.createTable();

        fooDao.insert();
        fooDao.insertAndReturnId();
        fooDao.insertWithSimpleJdbcInsert();
        fooDao.insertWithNamedParameterJdbcTemplate();

        fooDao.listWithQuery();
        fooDao.listWithQueryForObject();
        fooDao.listWithQueryForList();

        fooDao.batchInsert();
        fooDao.batchInsertWithNamedParameterJdbcTemplate();
    }

}
