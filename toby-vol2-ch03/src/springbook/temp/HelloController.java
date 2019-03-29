package springbook.temp;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class HelloController implements Controller {
	@Autowired HelloSpring helloSpring;
 
	// (Controller타입 핸들러를 담당하는)SimpleControllerHandlerAdapter를 통해 DispatcherServlet으로 부터 호출됨
	@Override
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String name = req.getParameter("name"); //1. 사용자 요청 해석
		String message = this.helloSpring.sayHello(name); //2. 비즈니스 로직 호출
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("message", message); //3. 모델 정보 생성 
		
		return new ModelAndView("/WEB-INF/view/hello.jsp", model); //4. 뷰 지정
	}

}
