package com.anno;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("guest.mainController")
@RequestMapping(value="/main.action") //����ڰ� �� �ּ� �Է��ϸ�
public class MainController { // �� �޼ҵ� ������ 
	
	@RequestMapping(method=RequestMethod.GET)
	public String method() { // �޼ҵ� �̸��� �ǹ̾��� Maincontroller�� �ٷ� �ؿ� �ִ� �޼ҵ带 �����Ű�⶧��.
		
		return "/main";
		//main.jsp�� ������.
	}


}
