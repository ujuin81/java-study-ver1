package springbook.learningtest.spring.web.atmvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

//ch4.3.4 : Validator와 BindingResult, Error
public class ValidatorTest extends AbstractDispatcherServletTest {
	@Test
	public void atrvalid() throws ServletException, IOException {
		setClasses(UserValidator.class, UserController.class);
		initRequest("/add.do").addParameter("id", "1").addParameter("age", "-2");
		runService();
	}
	
	@Test
	public void jsr303() throws ServletException, IOException {
		setClasses(UserController2.class, LocalValidatorFactoryBean.class);
		initRequest("/add.do").addParameter("id", "1").addParameter("age", "-1");
		runService();
	}
	
	//컨트롤러 메소드 내의 코드 검증
	@Controller 
	static class UserController {
		@Autowired UserValidator validator;
		@InitBinder  
		public void init(WebDataBinder dataBinder) {
			dataBinder.setValidator(this.validator);
		}
		
		@RequestMapping("/add") 
		public void add(@ModelAttribute User user, BindingResult result) {
			validator.validate(user, result);
			if (result.hasErrors()) {
				//오류가 발견된 경우의 작업
			}
			else {
				//오류가 없을 때의 작업
			}
			System.out.println(user);
			System.out.println(result);
		}
	}
	
	//@Valid를 이용한 자동검증 
	@Controller 
	static class UserController2 {
		@Autowired Validator validator;		
		@InitBinder  
		public void init(WebDataBinder dataBinder) {
			dataBinder.setValidator(this.validator);
		}
		
		@RequestMapping("/add") 
		public void add(@ModelAttribute @Valid User user, BindingResult result) {
			System.out.println(user);
			System.out.println(result);
		}
	}
	
	static class UserValidator implements Validator {
		public boolean supports(Class<?> clazz) {
			return (User.class.isAssignableFrom(clazz));
		}

		public void validate(Object target, Errors errors) {
			User user = (User)target;
			ValidationUtils.rejectIfEmpty(errors, "name", "field.required"); //"field.required" <- messages.properties 의 키 값
			ValidationUtils.rejectIfEmpty(errors, "age", "field.required");
			if (errors.getFieldError("age") == null)
				if (user.getAge() < 0) errors.rejectValue("age", "field.min", new Object[] {0}, "min default");
		}
	}
	
	public static class User {
		int id;
		@NotNull //빈 문자열은 검증안됨. 빈 문자열 확인하려면 @Size(min=1) //JSR-303 빈 검증 기능
		String name;
		@Min(0) 
		Integer age;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		public Integer getAge() {
			return age;
		}
		public void setAge(Integer age) {
			this.age = age;
		}
		@Override
		public String toString() {
			return "User [age=" + age + ", id=" + id + ", name=" + name + "]";
		}
	}
}
