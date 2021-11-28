package com.javabom.definitiveguide.chap7.mapper;


import com.javabom.definitiveguide.chap7.model.CustomerJdbc;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerJdbcRowMapper implements RowMapper<CustomerJdbc> {
    @Override
    public CustomerJdbc mapRow(ResultSet rs, int rowNum) throws SQLException {
        return CustomerJdbc.builder()
                .id(rs.getLong("id"))
                .address(rs.getString("address"))
                .city(rs.getString("city"))
                .firstName(rs.getString("firstName"))
                .lastName(rs.getString("lastName"))
                .middleInitial(rs.getString("middleInitial"))
                .state(rs.getString("state"))
                .zipCode(rs.getString("zipCode"))
                .build();
    }
}
