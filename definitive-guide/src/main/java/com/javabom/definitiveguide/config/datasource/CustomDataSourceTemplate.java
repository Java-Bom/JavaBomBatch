package com.javabom.definitiveguide.config.datasource;

import javax.sql.DataSource;

public class CustomDataSourceTemplate extends DefaultDataSourceTemplate {
    public CustomDataSourceTemplate(DataSource dataSource) {
        super(dataSource);
    }
}

