package springbook.learningtest.spring.web.atmvc;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;
import springbook.user.domain.Level;

public class ConverterTest extends AbstractDispatcherServletTest {
	//ch4.3.2 : Converter와 Formatter 
	//Converter - PropertyEditor를 대신할 수 있는 새로운 타입 변환 API
	//			- PropertyEditor와 달리 변환과정에서 메소드가 한 번만 호출됨. (싱글톤 가능) 
	//			- 소스 타입에서 타깃 타입으로의 단방향 변환만 지원 
	
	
	//소스타입 : Level ==> 타깃타입 : String  
	public static class LevelToStringConverter implements Converter<Level, String> {
		public String convert(Level level) {
			return String.valueOf(level.intValue());
		}
	}
	
	//소스타입 : String ==> 타깃타입 : Level  
	public static class StringToLevelConverter implements Converter<String, Level> {
		public Level convert(String text) {
			return Level.valueOf(Integer.parseInt(text));
		}
	}
	
	
	@Controller public static class SearchController {
		@Autowired ConversionService conversionService; //(conversionservice.xml) 
		
		@InitBinder
		public void initBinder(WebDataBinder dataBinder) {
			dataBinder.setConversionService(this.conversionService); //<-- !!!
		}
		
		@RequestMapping("/user/search") public void search(@RequestParam Level level, Model model) {
			model.addAttribute("level", level);
		}
	}
	
	@Test 
	public void compositeGenericConversionService() throws ServletException, IOException {
		setRelativeLocations("conversionservice.xml");
		setClasses(SearchController.class);
		initRequest("/user/search.do").addParameter("level", "1");
		runService();
		assertModel("level", Level.BASIC);
	}
	
}
