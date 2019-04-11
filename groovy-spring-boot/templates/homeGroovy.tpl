html {
	head {
		title('index page')
	}
	body {
		h1('Hello')
		p('this is Groovy template!')
		div(){
			a(href:'http://google.com'){
				yield 'google link'
			}
		}
	}
}