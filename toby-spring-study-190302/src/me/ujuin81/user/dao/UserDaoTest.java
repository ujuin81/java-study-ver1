package me.ujuin81.user.dao;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import me.ujuin81.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml") 
public class UserDaoTest {
	@Autowired UserDao dao;
	@Autowired DataSource dataSource;
	User user1;
	User user2;
	User user3;
	
	@Before
	public void setUp() {		
		this.user1 = new User("uju2", "우주2", "pw2");
		this.user2 = new User("uju3", "우주3", "pw3");
		this.user3 = new User("uju1", "우주1", "pw1");
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
	
	@Test(expected=EmptyResultDataAccessException.class) //실행 중 예외 발생 기대 
	public void getUserFailure() throws SQLException{
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.get("unknown_id"); 
	}
	
	@Test
	public void getAll() throws SQLException{
		dao.deleteAll();
		
		List<User> users0 = dao.getAll();
		assertThat(users0.size(), is(0));		
		
		dao.add(user1);
		List<User> users1 = dao.getAll();
		assertThat(users1.size(), is(1));
		checkSameUser(user1, users1.get(0));
		
		dao.add(user2);
		List<User> users2 = dao.getAll();
		assertThat(users2.size(), is(2));
		checkSameUser(user1, users2.get(0));
		checkSameUser(user2, users2.get(1));
		
		dao.add(user3);
		List<User> users3 = dao.getAll();
		assertThat(users3.size(), is(3));
		checkSameUser(user3, users3.get(0));
		checkSameUser(user1, users3.get(1));
		checkSameUser(user2, users3.get(2));
		
	}
	
	private void checkSameUser(User user1, User user2) {
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
	}
	
	@Test(expected=DuplicateKeyException.class)
	public void duplicateKey() {
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user1);
	}
	
	@Test
	public void sqlExceptionTranslate() {
		dao.deleteAll();
		
		try {
			dao.add(user1);
			dao.add(user1);
		}
		catch(DuplicateKeyException ex) {
			SQLException sqlEx = (SQLException)ex.getCause();
			SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);			

			//assertThat(set.translate(null, null, sqlEx), is(DuplicateKeyException.class));
			//책에서 나오는 부분 compile error - instanceOf 추가 하여 실행
			assertThat(set.translate(null, null, sqlEx), is(instanceOf(DuplicateKeyException.class)));
		}
	}
	
	
	public static void main(String[] args) {
		JUnitCore.main("me.ujuin81.user.dao.UserDaoTest");						
	}
}
