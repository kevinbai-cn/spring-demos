package com.kevinbai.simplejdbcdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class FooDao {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public FooDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("foo").usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void createTable() {
        jdbcTemplate.execute("CREATE TABLE foo (id INT NOT NULL AUTO_INCREMENT, bar VARCHAR(64))");
        log.info("Table foo created");
    }

    public void insert() {
        String bar = "a";
        jdbcTemplate.update("INSERT INTO foo (bar) VALUES (?)", bar);
        log.info("bar a inserted");
    }

    public void insertAndReturnId() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO foo (bar) VALUES (?)", new String[] { "id" });
            ps.setString(1, "b");
            return ps;
        }, keyHolder);
        log.info("bar b inserted with id {}", keyHolder.getKey());
    }

    public void insertWithSimpleJdbcInsert() {
        Map<String, String> row = new HashMap<>();
        row.put("bar", "c");
        Number id = simpleJdbcInsert.executeAndReturnKey(row);
        log.info("bar c inserted with id {}", id);
    }

    public void insertWithNamedParameterJdbcTemplate() {
        Map<String, String> row = new HashMap<>();
        row.put("bar", "d");
        namedParameterJdbcTemplate.update("INSERT INTO foo (BAR) VALUES (:bar)", row);
        log.info("bar d inserted");
    }

    public void listWithQuery() {
        List<Foo> fooList = jdbcTemplate.query("SELECT * FROM foo WHERE id >= ?", new RowMapper<Foo>() {
            @Override
            public Foo mapRow(ResultSet rs, int rowNum) throws SQLException {
                return Foo.builder()
                        .id(rs.getInt(1))
                        .bar(rs.getString(2))
                        .build();
            }
        }, 1);
        log.info("List with query:");
        fooList.forEach(f -> log.info(f.toString()));
    }

    public void listWithQueryForObject() {
        int count = jdbcTemplate.queryForObject("SELECT count(*) FROM foo", Integer.class);
        log.info("List with queryForObject:");
        log.info("{}", count);
    }

    public void listWithQueryForList() {
        List<String> barList = jdbcTemplate.queryForList("SELECT bar FROM foo", String.class);
        log.info("List with queryForList:");
        barList.forEach(b -> log.info(b));
    }

}
