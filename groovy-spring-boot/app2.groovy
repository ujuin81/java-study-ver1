@Grab("thymeleaf-spring5") //thymeleaf\uac00 \uc544\ub2cc groovy template \uc0ac\uc6a9\uc2dc \uc8fc\uc11d \ucc98\ub9ac 

@Controller
class App {
	@RequestMapping("/")
	@ResponseBody
	def home(ModelAndView mav) {
		mav.setViewName("home") //homeGroovy 
		mav.addObject("msg", "\uc548\ub155\ud558\uc138\uc694?")
		mav
	}
}
