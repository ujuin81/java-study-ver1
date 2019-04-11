@Grab("thymeleaf-spring5") //thymeleaf가 아닌 groovy template 사용시 주석 처리 

@Controller
class App {
	@RequestMapping(value="/", method=RequestMethod.GET)
	@ResponseBody
	def home(ModelAndView mav) {
		mav.setViewName("home")
		mav.addObject("msg","please write your name...")
		mav
	}

	@RequestMapping(value="/send", method=RequestMethod.POST)
	@ResponseBody
	def send(@RequestParam("text1")String str, ModelAndView mav) {
		mav.setViewName("home")
		mav.addObject("msg","Hello, " + str + "!!")
		mav.addObject("value",str)
		mav
	}
}