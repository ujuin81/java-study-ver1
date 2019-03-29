package springbook.learningtest.spring.web.hello;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.servlet.ModelAndView;

import springbook.learningtest.spring.web.ConfigurableDispatcherServlet;

public class HelloControllerTest {

	@Test
	public void helloController() throws ServletException, IOException {
		ConfigurableDispatcherServlet servlet = new ConfigurableDispatcherServlet();
		servlet.setRelativeLocations(getClass(), "spring-servlet.xml");
		servlet.setClasses(HelloSpring.class);
		servlet.init(new MockServletConfig("spring")); //서블릿 컨택스트를 생성하고 초기화한다. 
		//↑ 초기화까지 마치면 DispatcherServlet과 서블릿 컨택스트의 준비는 끝. 
		
		// 사용자(Http) 요청
		MockHttpServletRequest req = new MockHttpServletRequest("GET", "/hello");
		req.addParameter("name", "Spring");
		MockHttpServletResponse res = new MockHttpServletResponse();
		
		servlet.service(req, res);
		
		// 검증 코드
		ModelAndView mav = servlet.getModelAndView();
		assertThat(mav.getViewName(), is("/WEB-INF/view/hello.jsp"));
		assertThat((String)mav.getModel().get("message"), is("Hello Spring"));
	}
}
