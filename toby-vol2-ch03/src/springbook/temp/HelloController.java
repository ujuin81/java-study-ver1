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
 
	// (ControllerŸ�� �ڵ鷯�� ����ϴ�)SimpleControllerHandlerAdapter�� ���� DispatcherServlet���� ���� ȣ���
	@Override
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String name = req.getParameter("name"); //1. ����� ��û �ؼ�
		String message = this.helloSpring.sayHello(name); //2. ����Ͻ� ���� ȣ��
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("message", message); //3. �� ���� ���� 
		
		return new ModelAndView("/WEB-INF/view/hello.jsp", model); //4. �� ����
	}

}
