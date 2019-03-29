package springbook.learningtest.spring.web.simple;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class SimpleServletTest {

	@Test
	public void SimpleGetServlet() throws ServletException, IOException {
		MockHttpServletRequest req = new MockHttpServletRequest("GET", "/hello");
		req.addParameter("name", "Spring");
		
		MockHttpServletResponse res = new MockHttpServletResponse();
		
		SimpleGetServlet servlet = new SimpleGetServlet();
		servlet.service(req, res);
		
		assertThat(res.getContentAsString().contains("Hello Spring"), is(true));
	}
}
