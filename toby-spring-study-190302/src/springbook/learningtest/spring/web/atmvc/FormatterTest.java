package springbook.learningtest.spring.web.atmvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

public class FormatterTest extends AbstractDispatcherServletTest {
	//ch4.3.2 : Converter와 Formatter 
	//Formatter - Converter와 같이 스프링이 기본적으로 지원하는 범용적인 타입변환 API가 아니다. 
	//			- print() : 오브젝트를 문자열로, parse() : 문자열을 오브젝트로
	//			- Locale 타입의 현재 지역정보도 함께 제공됨 (다국어 서비스에 유용)
	
	@Test
	public void dateTimeFormat() {
		System.out.println("KOREA : " + org.joda.time.format.DateTimeFormat.patternForStyle("SS", Locale.KOREA));
		System.out.println("US : " + org.joda.time.format.DateTimeFormat.patternForStyle("SS", Locale.US));
	}
	
	@Test
	public void numberFormat() throws ServletException, IOException {
		setRelativeLocations("mvc-annotation.xml");
		setClasses(UserController.class);
		initRequest("/hello.do").addParameter("money", "$1,234.56");
		runService();
	}
	
	@Test
	public void dateFormat() throws ServletException, IOException {
		setRelativeLocations("mvc-annotation.xml");
		setClasses(UserController.class);
		initRequest("/hello.do").addParameter("date", "01/02/1999");
		runService();
	}
	
	static class User {
		@DateTimeFormat(pattern="dd/MM/yyyy")
		Date date;
		public Date getDate() { return date; }
		public void setDate(Date date) { this.date = date; }
		
		@NumberFormat(pattern="$###,##0.00")
		BigDecimal money;
		public BigDecimal getMoney() { return money; }
		public void setMoney(BigDecimal money) { this.money = money; }
		
	}
	@Controller static class UserController {
		@RequestMapping("/hello") void hello(User user) {
			System.out.println(user.date);
			System.out.println(user.money);
		}
	}
}
