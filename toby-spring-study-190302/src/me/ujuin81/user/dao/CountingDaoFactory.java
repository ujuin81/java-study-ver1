package me.ujuin81.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountingDaoFactory {
	
	@Bean
	public UserDao userDao() {
		return new UserDao(makeConnection());
	}
	
	@Bean
	public ConnectionMaker makeConnection() {
		return new CountingConnectionMaker(realConnectionMaker());
	}
	
	@Bean
	public ConnectionMaker realConnectionMaker() {
		return new DConnectionMaker();
	}
}
