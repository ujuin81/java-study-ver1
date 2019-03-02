package me.ujuin81.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import me.ujuin81.user.domain.User;

public class DUserDao extends UserDao {
	
	public static void main(String[] args)  throws ClassNotFoundException, SQLException {
		UserDao dao = new DUserDao();
		
		User user = new User();
		user.setId("whiteship");
		user.setName("백기선");
		user.setPassword("married");
		
		dao.add(user);
		
		System.out.println(user.getId() + "등록 성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		
		System.out.println(user2.getId() + "조회 성공");
	}

	@Override
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost/springbook?verifyServerCertificate=false&useSSL=false", "spring", "book");
		return c;
	}

}
