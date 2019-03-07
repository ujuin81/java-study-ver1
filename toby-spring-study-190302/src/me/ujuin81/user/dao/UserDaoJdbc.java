package me.ujuin81.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import me.ujuin81.user.domain.User;

public class UserDaoJdbc implements UserDao {
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private JdbcTemplate jdbcTemplate;
	
	private RowMapper<User> userMapper = new RowMapper<User>() {
		
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			return user;
		}
	};
	
	public void add(User user) {
		this.jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)",
				user.getId(), user.getName(), user.getPassword());
	}
	
	public void deleteAll() {
		this.jdbcTemplate.update("delete from users");
	}
	
	public User get(String id) {		
		
		return this.jdbcTemplate.queryForObject("select * from users where id = ?", 
				new Object[] {id}, 
				this.userMapper);
	}
	
	public int getCount() {
		//책에 나와있는 queryForInt는 3.2.2 버전부터 deprecated -->  queryForObject
		return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
	}
	
	public List<User> getAll() {
		return this.jdbcTemplate.query("select * from users order by id", 
				this.userMapper);
	}
}
