@Grab("thymeleaf-spring5") //thymeleaf가 아닌 groovy template 사용시 주석 처리 

@Controller
class App {
	@RequestMapping("/")
	@ResponseBody
	def home(ModelAndView mav) {
		mav.setViewName("home") //homeGroovy 
		mav
	}
}