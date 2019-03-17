package me.ujuin81;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mysql.jdbc.Driver;

import me.ujuin81.user.dao.UserDao;
import me.ujuin81.user.service.DummyMailSender;
import me.ujuin81.user.service.UserService;
import me.ujuin81.user.service.UserServiceTest.TestUserService;
import me.ujuin81.user.sqlservice.OxmSqlService;
import me.ujuin81.user.sqlservice.SqlRegistry;
import me.ujuin81.user.sqlservice.SqlService;
import me.ujuin81.user.sqlservice.updatable.EmbeddedDbSqlRegistry;

@Configuration
@EnableTransactionManagement 
@ComponentScan(basePackages="me.ujuin81.user")
public class TestApplicationContext {
	
	/** 
	 * DB 연결과 트랜잭션
	 */

	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/testdb?verifyServerCertificate=false&useSSL=false&characterEncoding=UTF-8");
		dataSource.setUsername("spring");
		dataSource.setPassword("book");
		
		return dataSource;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager tm = new DataSourceTransactionManager();
		tm.setDataSource(dataSource());
		return tm;
	}
	
	/**
	 * 애플리케이션 로직 & 테스트
	 */
	@Autowired UserDao userDao;

	@Autowired UserService userService;

	@Bean
	public UserService testUserService() {
		TestUserService service = new TestUserService();
		service.setUserDao(this.userDao);
		service.setMailSender(mailSender());
		return service;
	}

	@Bean
	public MailSender mailSender() {
		return new DummyMailSender();
	}
	
	/**
	 * SQL 서비스
	 */

	@Bean
	public SqlService sqlService() {
		OxmSqlService sqlService = new OxmSqlService();
		sqlService.setUnmarshaller(unmarshaller());
		sqlService.setSqlRegistry(sqlRegistry());
		return sqlService;
	}
	
	@Bean
	public SqlRegistry sqlRegistry() {
		EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
		sqlRegistry.setDataSource(embeddedDatabase());
		return sqlRegistry;
	}

	@Bean
	public Unmarshaller unmarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("me.ujuin81.user.sqlservice.jaxb");
		return marshaller;
	}

	@Bean
	public DataSource embeddedDatabase() {
		return new EmbeddedDatabaseBuilder()
				.setType(HSQL)
				.addScript("classpath:/me/ujuin81/user/sqlservice/updatable/sqlRegistrySchema.sql")
				.build();
	}
}
