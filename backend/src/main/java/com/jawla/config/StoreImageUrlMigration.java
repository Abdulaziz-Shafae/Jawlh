package com.jawla.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class StoreImageUrlMigration implements ApplicationRunner {
    private final JdbcTemplate jdbcTemplate;

    public StoreImageUrlMigration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        Integer needsMigration = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'store' AND COLUMN_NAME = 'image_url' AND DATA_TYPE = 'varchar' AND CHARACTER_MAXIMUM_LENGTH < 8192", Integer.class);
        if (needsMigration != null && needsMigration > 0) {
            jdbcTemplate.execute("ALTER TABLE store MODIFY COLUMN image_url TEXT NULL");
        }
    }
}
