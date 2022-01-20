package com.anno;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("guest.mainController")
@RequestMapping(value="/main.action") //사용자가 이 주소 입력하면
public class MainController { // 이 메소드 실행해 
	
	@RequestMapping(method=RequestMethod.GET)
	public String method() { // 메소드 이름은 의미없음 Maincontroller는 바로 밑에 있는 메소드를 실행시키기때문.
		
		return "/main";
		//main.jsp로 보낸다.
	}


}
