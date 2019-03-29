package springbook.learningtest.spring.web.hello;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

public class SimpleHelloControllerTest extends AbstractDispatcherServletTest {
	@Test
	public void helloController() throws ServletException, IOException {
		ModelAndView mav = setRelativeLocations("spring-servlet.xml")
							.setClasses(HelloSpring.class)
							.initRequest("/hello", RequestMethod.GET)
							.addParameter("name", "Spring")
							.runService()
							.getModelAndView();
		
		assertThat(mav.getViewName(), is("/WEB-INF/view/hello.jsp"));
		assertThat((String)mav.getModel().get("message"), is("Hello Spring"));
		
		// ↓ 뷰 이름, 모델 값만 검증한다면 아래와 같이 사용하는 것이 편리
		//setRelativeLocations("spring-servlet.xml")
		//.setClasses(HelloSpring.class)
		//.initRequest("/hello", RequestMethod.GET)
		//.addParameter("name", "Spring")
		//.runService()
		//.assertModel("message", "Hello Spring")
		//.assertViewName("/WEB-INF/view/hello.jsp");
	}
}
