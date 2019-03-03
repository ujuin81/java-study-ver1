package me.ujuin81.user.dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import me.ujuin81.user.domain.User;

public class CountingUserDaoTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		User user = new User();
		user.setId("ujuin81");
		user.setName("�̸�");
		user.setPassword("password!");
		
		dao.add(user);
		
		System.out.println(user.getId() + "��� ����");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		
		System.out.println(user2.getId() + "��ȸ ����");
		
		CountingConnectionMaker ccm = context.getBean("makeConnection", CountingConnectionMaker.class);
		System.out.println("Connection counting : " + ccm.getCounter());
	}
}
