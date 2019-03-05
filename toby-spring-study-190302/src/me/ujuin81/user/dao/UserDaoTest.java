package me.ujuin81.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import me.ujuin81.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml") 
public class UserDaoTest {
	@Autowired	
	UserDao dao;
	User user1;
	User user2;
	User user3;
	
	@Before
	public void setUp() {		
		this.user1 = new User("uju1", "����1", "pw1");
		this.user2 = new User("uju2", "����2", "pw2");
		this.user3 = new User("uju3", "����3", "pw3");
	}

	@Test
	public void addAndGet() throws SQLException {
		
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
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);
		assertThat(dao.getCount(), is(1));
		
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		dao.add(user3);
		assertThat(dao.getCount(), is(3));
	}
	
	@Test(expected=EmptyResultDataAccessException.class) //���� �� ���� �߻� ��� 
	public void getUserFailure() throws SQLException{
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.get("unknown_id"); 
	}
	
	public static void main(String[] args) {
		JUnitCore.main("me.ujuin81.user.dao.UserDaoTest");						
	}
}
