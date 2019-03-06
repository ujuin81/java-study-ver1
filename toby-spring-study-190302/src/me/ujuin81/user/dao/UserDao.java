package me.ujuin81.user.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import me.ujuin81.user.domain.User;

public class UserDao {
	
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
	
	public void add(final User user) throws SQLException {
		this.jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)",
				user.getId(), user.getName(), user.getPassword());
	}
	
	public void deleteAll() throws SQLException{
		this.jdbcTemplate.update("delete from users");
		
//		위와 동일 		
//		this.jdbcTemplate.update(new PreparedStatementCreator() {
//			
//			@Override
//			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//				return con.prepareStatement("delete from users");
//			}
//		};
	}
	
	public User get(String id) throws SQLException{		
		
		return this.jdbcTemplate.queryForObject("select * from users where id = ?", 
				new Object[] {id}, 
				this.userMapper);
	}
	
	public int getCount() throws SQLException{
		//책에 나와있는 queryForInt는 3.2.2 버전부터 deprecated -->  queryForObject
		return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
		
//		위와 동일 			
//		return this.jdbcTemplate.query(new PreparedStatementCreator() {
//
//			@Override
//			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//				return con.prepareStatement("select count(*) from users");
//			}
//		}, new ResultSetExtractor<Integer>() {
//
//			@Override
//			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
//				rs.next();
//				return rs.getInt(1);
//			}
//		});
	}
	
	public List<User> getAll() throws SQLException{
		return this.jdbcTemplate.query("select * from users order by id", 
				this.userMapper);
	}
	
}