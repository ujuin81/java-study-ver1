package me.ujuin81.user.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import me.ujuin81.user.domain.User;

public class UserDao {
	
	//DB 커넥션 생성 기능 -> DataSource 로 변경 
	private DataSource dataSource;
		
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException{
		Connection c = null;
		PreparedStatement ps = null;
		
		try {
			c = dataSource.getConnection();
			
			ps = stmt.makePreparedStatement(c); //<--PreparedStatement 생성이 필요한 시점에 호출되어 사용됨.  
			
			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
		finally {
			if(ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			
			if(c !=null) {
				try {
					c.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	public void add(final User user) throws SQLException {
		//로컬 클래스
		class AddStatement implements StatementStrategy {
			
			@Override
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
				
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
				
				return ps;
			}

		}
		
		StatementStrategy st = new AddStatement();
		jdbcContextWithStatementStrategy(st);
	}
	
	public User get(String id) throws SQLException{		
				
		Connection c = null;		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			c = dataSource.getConnection();
			
			ps = c.prepareStatement("select * from users where id = ?");
			ps.setString(1, id);
			
			rs = ps.executeQuery();
			User user = null;
			if(rs.next())
			{
				user = new User();
				user.setId(rs.getString("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
			}
			
			if(user == null) throw new EmptyResultDataAccessException(1);
			
			return user;
		} catch (SQLException e) {
			throw e;
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	public void deleteAll() throws SQLException{ 
		class DeleteAllStatement implements StatementStrategy {

			@Override
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				PreparedStatement ps = c.prepareStatement("delete from users");
				return ps;
			}

		}
		
		//전략 패턴의 Client로서 해당 메소드에 적절한 전략(로직)을 제공
		StatementStrategy st = new DeleteAllStatement(); 
		jdbcContextWithStatementStrategy(st);
	}
	
	public int getCount() throws SQLException{
		Connection c = null;		
		PreparedStatement ps = null;		
		ResultSet rs = null;
		
		try {
			c = dataSource.getConnection();
			
			ps = c.prepareStatement("select count(*) from users");
			
			rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			throw e;
		}finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
}