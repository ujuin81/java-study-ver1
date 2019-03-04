package me.ujuin81.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import me.ujuin81.user.domain.User;

public class UserDaoTest {

	@Test
	public void addAndGet() throws SQLException {
		
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		User user1 = new User("uju", "快林", "pwd");
		User user2 = new User("jigu", "瘤备", "jigupwd");
		
		dao.deleteAll(); 		
		assertThat(dao.getCount(), is(0)); 
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
			
		User userget1 = dao.get(user1.getId());
		assertThat(user1.getName(), is(userget1.getName()));
		assertThat(user1.getPassword(), is(userget1.getPassword()));
		
		User userget2 = dao.get(user2.getId());
		assertThat(user2.getName(), is(userget2.getName()));
		assertThat(user2.getPassword(), is(userget2.getPassword()));
	}
	
	@Test
	public void count() throws SQLException{
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		User user1 = new User("uju1", "快林1", "pw1");
		User user2 = new User("uju2", "快林2", "pw2");
		User user3 = new User("uju3", "快林3", "pw3");
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);
		assertThat(dao.getCount(), is(1));
		
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		dao.add(user3);
		assertThat(dao.getCount(), is(3));
	}
	
	@Test(expected=EmptyResultDataAccessException.class) //角青 吝 抗寇 惯积 扁措 
	public void getUserFailure() throws SQLException{
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.get("unknown_id"); 
	}
	
	public static void main(String[] args) {
		JUnitCore.main("me.ujuin81.user.dao.UserDaoTest");						
	}
}
