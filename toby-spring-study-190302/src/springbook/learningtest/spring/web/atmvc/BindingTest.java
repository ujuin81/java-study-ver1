package springbook.learningtest.spring.web.atmvc;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.beans.PropertyEditorSupport;
import java.nio.charset.Charset;

import org.junit.Test;
import org.springframework.beans.propertyeditors.CharsetEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import springbook.learningtest.spring.web.AbstractDispatcherServletTest;
import springbook.user.domain.Level;

//vol2-ch4.3 : �𵨹��ε��� ����
public class BindingTest extends AbstractDispatcherServletTest {

	//ch4.3.1 : PropertyEditor (���ε��� Ÿ�Ժ�ȯ API) 
	@Test
	public void charsetEditor() {
		CharsetEditor charsetEditor = new CharsetEditor(); //�������� �����ϴ� �⺻ ������Ƽ ������ �� �ϳ� 
		charsetEditor.setAsText("UTF-8");
		assertThat(charsetEditor.getValue(), is(instanceOf(Charset.class)));
		assertThat((Charset)charsetEditor.getValue(), is(Charset.forName("UTF-8")));
	}
	
	//Ŀ���� ������Ƽ ������ 
	static class LevelPropertyEditor extends PropertyEditorSupport {
		public String getAsText() {
			return String.valueOf(((Level)this.getValue()).intValue());
		}

		public void setAsText(String text) throws IllegalArgumentException {
			this.setValue(Level.valueOf(Integer.parseInt(text.trim())));
		}
	}
	
	//Ŀ���� ������Ƽ ������ Test
	@Test
	public void levelPropertyEditor() {
		LevelPropertyEditor levelEditor = new LevelPropertyEditor();
		
		levelEditor.setValue(Level.BASIC);
		assertThat(levelEditor.getAsText(), is("1"));
		
		levelEditor.setAsText("3");
		assertThat((Level)levelEditor.getValue(), is(Level.GOLD));
	}
	
	//WebDataBinder�� Ŀ���� ������Ƽ ������ ��� 
	@Controller static class SearchController {
		@InitBinder //<--- !! 
		public void initBinder(WebDataBinder dataBinder) {
			dataBinder.registerCustomEditor(Level.class, new LevelPropertyEditor());
		}
		
		@RequestMapping("/user/search") public void search(@RequestParam Level level, Model model) {
			model.addAttribute("level", level);
		}
	}
	
	//WebDataBinder�� Ŀ���� ������Ƽ ������ ��� Test
	@Test
	public void dataBinder() {
		WebDataBinder dataBinder = new WebDataBinder(null);
		dataBinder.registerCustomEditor(Level.class, new LevelPropertyEditor());
		assertThat(dataBinder.convertIfNecessary("1", Level.class), is(Level.BASIC));
	}
	
	//Ŀ���� ������Ƽ ������ : int�� Ÿ�Ժ�ȯ�� ���� ������ int�� Ÿ�Ժ�ȯ �ǵ���  
	static class MinMaxPropertyEditor extends PropertyEditorSupport {
		int min = Integer.MIN_VALUE;
		int max = Integer.MAX_VALUE;
		
		public MinMaxPropertyEditor(int min, int max) {
			this.min = min;
			this.max = max;
		}
		
		public MinMaxPropertyEditor() { }

		public String getAsText() {
			return String.valueOf((Integer)this.getValue());
		}

		public void setAsText(String text) throws IllegalArgumentException {
			Integer val = Integer.parseInt(text);
			if (val < min) val = min;
			else if (val > max) val = max;
			
			setValue(val);
		}
	}
	
	static class Member {
		int id;
		int age;
		public int getId() { return id; }
		public void setId(int id) { this.id = id; }
		public int getAge() { return age; }
		public void setAge(int age) { this.age = age; }
		public String toString() { return "Member [age=" + age + ", id=" + id + "]"; }		
	}
	
	//WebDataBinder�� Ư�� �̸��� ������Ƽ���� ����Ǵ� ������Ƽ ������ ���(�켱����:Ŀ���� ������Ƽ ������ >> �⺻ ������Ƽ ������) 
	@Controller static class MemberController {
		@InitBinder
		public void initBinder(WebDataBinder dataBinder) {
			//int Ÿ���� �Ķ���� �� ������Ƽ���� 'age'�� �Ķ���Ϳ��� MinMaxPropertyEditor Ŀ���� ������Ƽ ������ ���� 
			//������Ƽ���� 'id'�� �Ķ���Ϳ��� ����Ʈ ������Ƽ �����Ͱ� �����
			dataBinder.registerCustomEditor(int.class, "age", new MinMaxPropertyEditor(0, 200));
		}
		@RequestMapping("/add") public void add(@ModelAttribute Member member) {
		}
	}

	//fake property editor 
}
