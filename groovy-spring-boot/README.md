#### **Groovy**를 이용한 애플리케이션 

> 참고 : 스프링 부트 프로그래밍 입문 (길벗) 

1. Spring Boot CLI 다운로드 및 Path 설정 
2. 그루비 스크립트 작성 (*.groovy) 
3. 실행 `$ spring run 파일명.groovy ` 
   ==> 웹 애플리케이션 시작됨. http://localhost:8080/ 에서 확인 가능



* 템플릿 사용하기 
  * Thymeleaf 템플릿 사용하기 
    1. 템플릿 작성. (컨트롤러가 있는 곳의 templates 폴더 안에 두어야 동작)
    2. 템플릿 읽도록 컨트롤러 수정 
       * `@Grab("thymeleaf-spring5")`
       * `@ResponseBody`
       * `ModelAndView `
  * Groovy로 템플릿 작성 
    1. 템플릿 작성 : 그루비 코드로 HTML 구조를 기술 (*.tpl) 
    2. 템플릿 읽도록 컨트롤러 수정 
       * Thymeleaf 부분에서 `@Grab("thymeleaf-spring5")` 부분만 제거하면 사용방법 동일 
         (`@Grab("thymeleaf-spring5")` 부분 있는 상태에서 *.html, *.tpl  동일한 이름의 view template이 존재 할경우 *.tpl이 호출됨 )