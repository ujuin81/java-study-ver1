package me.ujuin81.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static me.ujuin81.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static me.ujuin81.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import me.ujuin81.user.dao.UserDao;
import me.ujuin81.user.domain.Level;
import me.ujuin81.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {
	
	@Autowired UserService userService;
	@Autowired UserDao userDao;
	@Autowired DataSource dataSource;
	List<User> users;
	
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
				new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
				new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1),
				new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
				new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
				);
	}
	
	@Test
	public void upgradeLevels() throws Exception {
		userDao.deleteAll();		
		for(User user : users) {
			userDao.add(user);
		}
		
		userService.upgradeLevels();
		
		checkLevelUpgrade(users.get(0), false);
		checkLevelUpgrade(users.get(1), true);
		checkLevelUpgrade(users.get(2), false);
		checkLevelUpgrade(users.get(3), true);
		checkLevelUpgrade(users.get(4), false);
	}
	
	private void checkLevelUpgrade(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		if(upgraded) {
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		}else {
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}
	
	@Test
	public void add() {
		userDao.deleteAll();
		
		User userWithLevel = users.get(4);
		User userwithoutLevel = users.get(0);
		userwithoutLevel.setLevel(null); 
		
		userService.add(userWithLevel);
		userService.add(userwithoutLevel);
		
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userwithoutLevelRead = userDao.get(userwithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userwithoutLevelRead.getLevel(), is(Level.BASIC));
	}
	
	@Test
	public void bean() {
		assertThat(this.userService, is(notNullValue()));
	}
	
	@Test
	public void upgradeAllOrNothing() throws Exception {
		UserService testUserService = new TestUserService(users.get(3).getId()); 
		testUserService.setUserDao(this.userDao);
		testUserService.setDataSource(this.dataSource);
		
		userDao.deleteAll();
		for(User user : users) {
			userDao.add(user);
		}
		
		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException e) {
		}
		
		checkLevelUpgrade(users.get(1), false);
	}
	
	static class TestUserService extends UserService{
		private String id;
		
		public TestUserService(String id) {
			this.id = id;		
		}

		@Override
		protected void upgradeLevel(User user) {
			if(user.getId().equals(this.id)) throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
		
	}
	
	static class TestUserServiceException extends RuntimeException{		
	}
	
}
