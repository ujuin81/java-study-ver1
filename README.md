# java-study-ver1
[toby-spring-study-190302] 토비의 스프링 3.1 Study ...ing

<hr/>
* Spring Tool Suite 
* java version "1.8.0_201"

##### [ch1.5] spring library 추가 

1. spring library download (5.1.5.RELEASE)
2. 프로젝트 내 lib 폴더 생성 및 해당 폴더에 복사 
3. Project > Properties > Java Build Path > [Add JARS...] > lib 내 파일 선택 > [Apply and Close]
4. .classpath 파일에 라이브러리들이 추가된 것을 확인할 수 있음

##### [ch2.2] JUnit library 추가 

1. Project > Properties > Java Build Path > [Add Library...] > JUnit 선택 
	: JUnit 5 버전으로 콘솔 실행시 initializationError, No runnable methods 오류 발생 >> JUnit 4 사용 

##### [ch6.2.4] Mockito library 추가 (mockito-all-1.10.19.jar)

##### [ch6.5.3] library 추가 (aspectjweaver-1.6.2.jar) 
	: java.lang.NoClassDefFoundError: org/aspectj/weaver/reflect/ReflectionWorld$ReflectionWorldException 해결 

##### [ch7.2.1]	JAXB 
	: cmd > xjc -p me.ujuin81.user.sqlservice.jaxb sqlmap.xsd -d src

##### [ch7.3.1]	Castor library 추가 (castor-1.2.jar)
	: java.lang.ClassNotFoundException: org.exolab.castor.xml.XMLException 해결 