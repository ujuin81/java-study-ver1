package me.ujuin81;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
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
import me.ujuin81.user.dao.UserDaoJdbc;
import me.ujuin81.user.service.DummyMailSender;
import me.ujuin81.user.service.UserService;
import me.ujuin81.user.service.UserServiceImpl;
import me.ujuin81.user.service.UserServiceTest.TestUserService;
import me.ujuin81.user.sqlservice.OxmSqlService;
import me.ujuin81.user.sqlservice.SqlRegistry;
import me.ujuin81.user.sqlservice.SqlService;
import me.ujuin81.user.sqlservice.updatable.EmbeddedDbSqlRegistry;

@Configuration
@EnableTransactionManagement /* ←←← <tx:annotation-driven />*/
public class TestApplicationContext {
	
	/** 
	 * DB 연결과 트랜잭션
	 */

	/*
	<bean id="dataSource"
		class="org.springframework.jdbc.datasource.SimpleDriverDataSource">
		<property name="driverClass" value="com.mysql.jdbc.Driver" />
		<property name="url"
			value="jdbc:mysql://localhost/testdb?verifyServerCertificate=false&amp;useSSL=false&amp;characterEncoding=UTF-8" />
		<property name="username" value="spring" />
		<property name="password" value="book" />
	</bean> 
	↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/testdb?verifyServerCertificate=false&useSSL=false&characterEncoding=UTF-8");
		dataSource.setUsername("spring");
		dataSource.setPassword("book");
		
		return dataSource;
	}
	
	/*
	<bean id="transactionManager"
		class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource" />
	</bean>
	↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager tm = new DataSourceTransactionManager();
		tm.setDataSource(dataSource());
		return tm;
	}
	
	/**
	 * 애플리케이션 로직 & 테스트
	 */
	
	@Autowired SqlService sqlService;
		
	/*
	<bean id="userDao" class="me.ujuin81.user.dao.UserDaoJdbc">
		<property name="dataSource" ref="dataSource" />
		<property name="sqlService" ref="sqlService" />
	</bean>
	↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
	@Bean
	public UserDao userDao() {
		UserDaoJdbc dao = new UserDaoJdbc();
		dao.setDataSource(dataSource());
		dao.setSqlService(this.sqlService);
		return dao;
	}
	
	/*	
	<bean id="userService"
		class="me.ujuin81.user.service.UserServiceImpl">
		<property name="userDao" ref="userDao" />
		<property name="mailSender" ref="mailSender" />
	</bean>
	↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
	@Bean
	public UserService userService() {
		UserServiceImpl service = new UserServiceImpl();
		service.setUserDao(userDao());
		service.setMailSender(mailSender());
		return service;
	}

	/*
	<bean id="testUserService"
		class="me.ujuin81.user.service.UserServiceTest$TestUserService"
		parent="userService" />
	↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
	@Bean
	public UserService testUserService() {
		TestUserService service = new TestUserService();
		service.setUserDao(userDao());
		service.setMailSender(mailSender());
		return service;
	}
	
	/*
	<bean id="mailSender"
		class="me.ujuin81.user.service.DummyMailSender" />
	↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
	@Bean
	public MailSender mailSender() {
		return new DummyMailSender();
	}
	
	/**
	 * SQL 서비스
	 */
	
	/*
	<bean id="sqlService"
		class="me.ujuin81.user.sqlservice.OxmSqlService">
		<property name="unmarshaller" ref="unmarshaller" />
		<property name="sqlRegistry" ref="sqlRegistry" />
	</bean>
	↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
	@Bean
	public SqlService sqlService() {
		OxmSqlService sqlService = new OxmSqlService();
		sqlService.setUnmarshaller(unmarshaller());
		sqlService.setSqlRegistry(sqlRegistry());
		return sqlService;
	}
	

	//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 해당 부분 오류 
	//: EmbeddedDatabase 가 아닌 SimpleDriverDataSource로 가져온다.
	//: 원인은 아직 모르겠음. DataSource 타입의 빈 하나 더 추가하여 @Resource로 빈 생성시는 문제 없음. 
	//: <jdbc:embedded-database> 의 문제로 추정... ㅠㅠ
	//@Resource
	//private EmbeddedDatabase embeddedDatabase;


	/*
	<bean id="sqlRegistry"
		class="me.ujuin81.user.sqlservice.updatable.EmbeddedDbSqlRegistry">
		<property name="dataSource" ref="embeddedDatabase"></property>
	</bean>
	↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
	@Bean
	public SqlRegistry sqlRegistry() {
		EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
		sqlRegistry.setDataSource(embeddedDatabase());
		return sqlRegistry;
	}

	/*
	<bean id="unmarshaller"
		class="org.springframework.oxm.jaxb.Jaxb2Marshaller">
		<property name="contextPath"
			value="me.ujuin81.user.sqlservice.jaxb" />
	</bean>
	↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
	@Bean
	public Unmarshaller unmarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("me.ujuin81.user.sqlservice.jaxb");
		return marshaller;
	}
	
	/*
	<jdbc:embedded-database id="embeddedDatabase" type="HSQL">
		<jdbc:script location="classpath:/me/ujuin81/user/sqlservice/updatable/sqlRegistrySchema.sql" />
	</jdbc:embedded-database>
	↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
	public DataSource embeddedDatabase() {
		return new EmbeddedDatabaseBuilder()
				.setType(HSQL)
				.addScript("classpath:/me/ujuin81/user/sqlservice/updatable/sqlRegistrySchema.sql")
				.build();
	}
}
