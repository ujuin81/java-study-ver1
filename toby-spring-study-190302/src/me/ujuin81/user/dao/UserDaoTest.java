package me.ujuin81.user.dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import me.ujuin81.user.domain.User;

public class UserDaoTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		User user = new User();
		user.setId("ujuin81");
		user.setName("�̸�");
		user.setPassword("password!");
		
		dao.add(user);
		
		System.out.println(user.getId() + " ��� ����");
		
		User user2 = dao.get(user.getId());
		
		//�׽�Ʈ ����
		if(!user.getName().equals(user2.getName())) {
			System.out.println("�׽�Ʈ ���� (name)");
		} else if(!user.getPassword().equals(user2.getPassword())) {
			System.out.println("�׽�Ʈ ���� (password)");
		}else {
			System.out.println(user.getId() + " ��ȸ �׽�Ʈ ����");
		}		
	}
}
