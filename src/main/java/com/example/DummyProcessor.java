package com.example;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class DummyProcessor {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public void printResult() {
		log.info("user_profile count: {} ",jdbcTemplate.queryForObject("select count(*) from user_profile", BigDecimal.class));
		log.info("profile_details count: {} ",jdbcTemplate.queryForObject("select count(*) from profile_details", BigDecimal.class));
	}
}
