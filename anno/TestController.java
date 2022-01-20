package com.anno;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("test.TestController")
public class TestController {
	
	@RequestMapping(value="/demo/write.action",method= {RequestMethod.GET})
	public String created() throws Exception{
		return "anno/write";
	}
	
	@RequestMapping(value="/demo/write.action",method= {RequestMethod.POST})
	public String write_ok(TestCommand command , HttpServletRequest request) throws Exception{
		//사용자가 입력한 데이터를 처리해서.. result.jsp로 넘기기
		
		String message= "이름:" + command.getUserName();
		message += ", 아이디:"+ command.getUserId();
		
		request.setAttribute("message", message);
		
		return "anno/result";
		
		
	}
	
	
	//위에 두개 메소드 한개로 합치기 
	
	@RequestMapping(value="/demo/save.action", method= {RequestMethod.POST,RequestMethod.GET})
	public String save(TestCommand command, HttpServletRequest request) throws Exception{
		
		//처음 실행
		if(command==null||command.getMode()==null||command.getMode().equals("")) { //순서바뀌면 안됨.
			return "anno/created";
		}
		
		//아닌경우 
		String message= "이름:" + command.getUserName();
		message += ", 아이디:"+ command.getUserId();
		
		request.setAttribute("message", message);
		
		return "anno/result";
		
	}


	//command 사용안하기(dto) 일반적으로 사용하진않음 
	@RequestMapping(value="/demo/demo.action", method= {RequestMethod.POST,RequestMethod.GET})
	public String demo(String userId, String userName, String mode, HttpServletRequest request) throws Exception{
		
		if(mode==null|| mode.equals("")) {
			return "anno/demo";
		}
		
		String message= "이름:" + userName;
		message += ", 아이디:"+ userId;
		
		request.setAttribute("message", message);
		
		return "anno/result";
		
		
	}
		
	
	
	

}
