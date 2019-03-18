package me.ujuin81;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import me.ujuin81.user.dao.UserDao;
import me.ujuin81.user.service.DummyMailSender;
import me.ujuin81.user.service.UserService;
import me.ujuin81.user.service.UserServiceTest.TestUserService;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "me.ujuin81")
@EnableSqlService //<--@Import({ SqlServiceContext.class })

@PropertySource("/database.properties")
public class AppContext implements SqlMapConfig {
	//@Autowired Environment env;
	//↓↓↓↓↓↓↓↓↓↓↓↓ 둘 중 하나 선택해서 사용 
	@Value("${db.driverClass}") Class<? extends java.sql.Driver> driverClass;
	@Value("${db.url}") String url;
	@Value("${db.username}") String username;
	@Value("${db.password}") String password;
		
	@Override
	public Resource getSqlMapResource() {
		return new ClassPathResource("sqlmap.xml", UserDao.class);
	}
	
	//@Value 주입 사용하려면 필요 
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	/**
	 * DB 연결과 트랜잭션
	 */

	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

		//try {
		//	dataSource.setDriverClass(
		//			(Class<? extends java.sql.Driver>)Class.forName(env.getProperty("db.driverClass")));
		//} catch (ClassNotFoundException e) {
		//	throw new RuntimeException(e);
		//}
		//dataSource.setUrl(env.getProperty("db.url"));
		//dataSource.setUsername(env.getProperty("db.username"));
		//dataSource.setPassword(env.getProperty("db.password"));
		//↓↓↓↓↓↓↓↓↓↓↓↓ 둘 중 하나 선택해서 사용 
		dataSource.setDriverClass(this.driverClass);
		dataSource.setUrl(this.url);
		dataSource.setUsername(this.username);
		dataSource.setPassword(this.password);		

		return dataSource;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager tm = new DataSourceTransactionManager();
		tm.setDataSource(dataSource());
		return tm;
	}

	@Configuration
	@Profile("production")
	public static class ProductionAppContext {
		@Bean
		public MailSender mailSender() {
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			mailSender.setHost("localhost");
			return mailSender;
		}
	}

	@Configuration
	@Profile("test")
	public static class TestAppContext {
		@Bean
		public UserService testUserService() {
			return new TestUserService();
		}

		@Bean
		public MailSender mailSender() {
			return new DummyMailSender();
		}
	}
}
