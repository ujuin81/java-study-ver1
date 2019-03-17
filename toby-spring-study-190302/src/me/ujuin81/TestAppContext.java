package me.ujuin81;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

import me.ujuin81.user.service.DummyMailSender;
import me.ujuin81.user.service.UserService;
import me.ujuin81.user.service.UserServiceTest.TestUserService;

@Configuration
public class TestAppContext {	
	@Bean
	public UserService testUserService() {
		return new TestUserService();
	}

	@Bean
	public MailSender mailSender() {
		return new DummyMailSender();
	}
}
