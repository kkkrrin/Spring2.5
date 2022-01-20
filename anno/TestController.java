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
		//����ڰ� �Է��� �����͸� ó���ؼ�.. result.jsp�� �ѱ��
		
		String message= "�̸�:" + command.getUserName();
		message += ", ���̵�:"+ command.getUserId();
		
		request.setAttribute("message", message);
		
		return "anno/result";
		
		
	}
	
	
	//���� �ΰ� �޼ҵ� �Ѱ��� ��ġ�� 
	
	@RequestMapping(value="/demo/save.action", method= {RequestMethod.POST,RequestMethod.GET})
	public String save(TestCommand command, HttpServletRequest request) throws Exception{
		
		//ó�� ����
		if(command==null||command.getMode()==null||command.getMode().equals("")) { //�����ٲ�� �ȵ�.
			return "anno/created";
		}
		
		//�ƴѰ�� 
		String message= "�̸�:" + command.getUserName();
		message += ", ���̵�:"+ command.getUserId();
		
		request.setAttribute("message", message);
		
		return "anno/result";
		
	}


	//command �����ϱ�(dto) �Ϲ������� ����������� 
	@RequestMapping(value="/demo/demo.action", method= {RequestMethod.POST,RequestMethod.GET})
	public String demo(String userId, String userName, String mode, HttpServletRequest request) throws Exception{
		
		if(mode==null|| mode.equals("")) {
			return "anno/demo";
		}
		
		String message= "�̸�:" + userName;
		message += ", ���̵�:"+ userId;
		
		request.setAttribute("message", message);
		
		return "anno/result";
		
		
	}
		
	
	
	

}
