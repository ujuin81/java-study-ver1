package me.ujuin81.user.sqlservice.updatable;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import me.ujuin81.user.sqlservice.SqlNotFoundException;
import me.ujuin81.user.sqlservice.SqlUpdateFailureException;
import me.ujuin81.user.sqlservice.UpdatableSqlRegistry;

public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {
	JdbcTemplate jdbc;
	
	public void setDataSource(DataSource dataSource) {
		jdbc = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void registerSql(String key, String sql) {
		jdbc.update("insert into sqlmap(key_, sql_) values(?, ?)", key, sql);

	}

	@Override
	public String findSql(String key) throws SqlNotFoundException {
		try {
			return jdbc.queryForObject("select sql_ from sqlmap where key_ = ?", String.class, key);
		}catch(EmptyResultDataAccessException e) {
			throw new SqlNotFoundException(key + "�� �ش��ϴ� SQL�� ã�� �� �����ϴ�.");
		}
	}

	@Override
	public void updateSql(String key, String sql) throws SqlUpdateFailureException {
		int affected = jdbc.update("update sqlmap set sql_ = ? where key_ = ?", sql, key);
		if(affected == 0) {
			throw new SqlUpdateFailureException(key + "�� �ش��ϴ� SQL�� ã�� �� �����ϴ�."); 
		}
	}

	@Override
	public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
		for(Map.Entry<String, String> entity : sqlmap.entrySet()) {
			updateSql(entity.getKey(), entity.getValue());
		}
	}

}
