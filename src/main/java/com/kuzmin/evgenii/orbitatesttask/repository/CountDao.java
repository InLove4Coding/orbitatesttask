package com.kuzmin.evgenii.orbitatesttask.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CountDao {
    private final JdbcTemplate jdbcTemplate;

    public CountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long getCount() {
        return jdbcTemplate.queryForObject("select counter from count", (resultSet, i) -> resultSet.getLong("counter"));
    }

    @Transactional
    public Long incrementCount() {
        jdbcTemplate.update("update count set counter = counter+1");
        return getCount();
    }

    @Transactional
    public void clearState() {
        jdbcTemplate.update("update count set counter = 0");
    }
}
