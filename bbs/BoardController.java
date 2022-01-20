package com.bbs;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.util.MyUtil;
import com.util.dao.CommonDAO;


/* ������̼� ���� 
@Repository : ����� ������Ʈ(DAO ��).
@Service : ���¾��� ���� ������Ʈ.
@Autowired ������̼� : Spring���� �������踦 �ڵ����� ����
@Resource ������̼� : �����ϴ� �� ��ü�� ���� �� �� ���

@Resource �� �̸��� ������ �� ������ @Autowired�� �̸��� ������ �� ���� id ���� ��ġ�ؾ��Ѵ�.
*/
@Controller("bbs.boardController")
public class BoardController {
	
	//������̼����� ������ dao ��������(��ü����?)
	@Resource(name="dao")
	private CommonDAO dao; 
	
	@Resource(name="myUtil")
	private MyUtil myUtil;
	
	
	@RequestMapping(value="/bbs/created.action",method= {RequestMethod.GET,RequestMethod.POST})
	public String created(BoardCommand command, HttpServletRequest request) throws Exception{
		
		if(command==null|| command.getMode()==null||command.getMode().equals("")) {
			
			request.setAttribute("mode", "insert");//mode�� insert ��� ���� �ִ´�
			
			return "board/created";
			
		}
		
		//dao ��ü ���� (������̼�) @Repository("dao") = CommonDAOImpl ���� �޼ҵ� ���� �������ش�.
		int boardNumMax = dao.getIntValue("bbs.boardNumMax"); 
		
		command.setBoardNum(boardNumMax+1);
		command.setIpAddr(request.getRemoteAddr());
		
		//System.out.println(boardNumMax); ����
		
		dao.insertData("bbs.insertData", command);
		
		return "redirect:/bbs/list.action"; 
				

	}
	
	
	
	@RequestMapping(value="/bbs/list.action",method= {RequestMethod.GET,RequestMethod.POST})
	public String list(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		HttpSession session  = request.getSession(); 
		String cp = request.getContextPath();
		
		int numPerPage= 5;
		int totalPage =0;
		int totalDataCount=0;
		
		String pageNum= request.getParameter("pageNum");
		
		//�������� pageNum�� �޴´�. (updateSubmit()���� ���� pageNum ������Ʈ�Ҷ� ��� )
		if(pageNum==null) {
			pageNum = (String)session.getAttribute("pageNum"); 
		}
		
		session.removeAttribute("pageNum");
		
		int currentPage=1;
		
		if(pageNum!=null && !pageNum.equals("")) {
			currentPage=Integer.parseInt(pageNum);
		}
		
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		if(searchValue==null) {
			searchKey="subject";
			searchValue="";
		}
		
		if(request.getMethod().equalsIgnoreCase("GET")) { //get����̸� �ѱ۷� ����?(�˻�?)
			searchValue=URLDecoder.decode(searchValue,"UTF-8"); 
		}
		
		Map<String, Object> hMap = new HashMap<String, Object>();
		hMap.put("searchKey", searchKey);
		hMap.put("searchValue", searchValue);
		
		
		totalDataCount = dao.getIntValue("bbs.dataCount",hMap); //��ü�����Ͱ��� 
		
		//��ü������
		if(totalDataCount!=0) {
			totalPage =myUtil.getPageCount(numPerPage, totalDataCount);
		}
		
		if(currentPage>totalPage) {
			currentPage = totalPage;
		}
		
		int start =(currentPage-1) *numPerPage+1;
		int end= currentPage*numPerPage;
		
		hMap.put("start", start);
		hMap.put("end", end);
		
		List<Object> lists= (List<Object>)dao.getListData("bbs.listData", hMap);
		
		//�ϷĹ�ȣ 
		
		int listNum,n=0;
		ListIterator<Object> it= lists.listIterator();
		
		while(it.hasNext()) {
			
			BoardCommand data = (BoardCommand)it.next();
			
			listNum = totalDataCount-(start+n-1);
			
			data.setListNum(listNum);
			
			n++;
			
		}
		
		
		//�ּ����� (���������)
		
		String params="";
		String urlList="";
		String urlArticle="";
		
		if(!searchValue.equals("")) {
			searchValue= URLEncoder.encode(searchValue,"UTF-8");
			params="searchKey="+searchKey + "&searchValue="+searchValue;
		}
		
		//�˻�����
		if(params.equals("")) {
			urlList = cp+"/bbs/list.action";
			urlArticle= cp+"/bbs/article.action?pageNum="+currentPage;
		} else {//�˻���
			urlList = cp+"/bbs/list.action?"+params;
			urlArticle= cp+"/bbs/article.action?pageNum="+currentPage+"&"+params;
			
		}
		
		request.setAttribute("lists", lists);
		request.setAttribute("urlArticle", urlArticle);
		request.setAttribute("pageNum", currentPage);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("totalDataCount", totalDataCount);
		request.setAttribute("pageIndexList", myUtil.pageIndexList(currentPage, totalPage, urlList));
		
		//�� �����͸� ������ return���� ����. 
		
		return "board/list";
		
		
	}
		
	
	
	
	@RequestMapping(value="/bbs/article.action",method= {RequestMethod.GET,RequestMethod.POST})
	public String article(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		int boardNum= Integer.parseInt(request.getParameter("boardNum"));
		String pageNum= request.getParameter("pageNum");
		
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		if(searchValue==null) { //�˻�����
			searchKey="subject";
			searchValue="";
		}
		
		if(request.getMethod().equalsIgnoreCase("GET")) {
			searchValue = URLDecoder.decode(searchValue,"UTF-8");
		}
		
		//��ȸ������
		dao.updateData("bbs.updateHitCount",boardNum);
		
		//�ϳ��� ������ �о����
		// ���⼭ dto�� �����ؼ� request�� �����ְ�, jsp���� dto.�� �޾Ƽ� ����Ѵ�. 
		BoardCommand dto=(BoardCommand)dao.getReadData("bbs.readData", boardNum);
		
		if(dto==null) {
			return "redirect:/bbs/list.action";
		}
		
		int lineSu=dto.getContent().split("\r\n").length;
		
		dto.setContent(dto.getContent().replaceAll("\r\n", "<br/>"));
		
		//�ѱ��
		Map<String,Object> hMap= new HashMap<>();
		hMap.put("searchKey", searchKey);
		hMap.put("searchvalue",searchValue);
		hMap.put("boardNum", boardNum);
		
		
		//sqlMap���� ������ (������/������)
		BoardCommand preReadData = (BoardCommand)dao.getReadData("bbs.preReadData",hMap);
		
		int preBoardNum=0;
		String preSubject="";
		
		if(preReadData!=null) {
			preBoardNum = preReadData.getBoardNum();
			preSubject = preReadData.getSubject(); 
		}
		
		BoardCommand nextReadData = (BoardCommand)dao.getReadData("bbs.nextReadData",hMap);
		
		int nextBoardNum=0;
		String nextSubject="";
		
		if(nextReadData!=null) {
			nextBoardNum = nextReadData.getBoardNum();
			nextSubject = nextReadData.getSubject(); 
		}
		
		//�ּ�����
		String params ="pageNum=" +pageNum;
		
		if(!searchValue.equals("")) {
			searchValue= URLEncoder.encode("searchValue","UTF-8");
			params +="&searchKey="+ searchKey; 
			params +="&searchValue"+ searchValue;
		}
		
		
		//������ �ѱ��
		
		request.setAttribute("dto", dto);
		request.setAttribute("preBoardNum", preBoardNum);
		request.setAttribute("preSubject", preSubject);
		request.setAttribute("nextBoardNum", nextBoardNum);
		request.setAttribute("nextSubject", nextSubject);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("params", params);
		request.setAttribute("lineSu", lineSu);
		
		//�� ������ ������ article.jsp�� ����. 
		
		return "board/article";
		
	}
	
	
	
	@RequestMapping(value="/bbs/updated.action",method= {RequestMethod.GET})
	public String updated(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//â���� 
		int boardNum= Integer.parseInt(request.getParameter("boardNum"));
		String pageNum= request.getParameter("pageNum");
		
		BoardCommand dto= (BoardCommand)dao.getReadData("bbs.readData", boardNum);
		
		if(dto==null) {
			return "redirect:/bbs/list.action?pageNum"+pageNum; //������������ ���ư��� 
			//return "redirect:/bbs/list.action"; 
		}
		
		request.setAttribute("mode", "update"); // created.jsp �ؿ� hidden�� mode�� �Ѿ��. 
		request.setAttribute("pageNum",pageNum);
		request.setAttribute("dto", dto);

		
		return "board/created";
	}
	
	
	@RequestMapping(value="/bbs/updated.action",method= {RequestMethod.POST})
	public String updatedSubmit(BoardCommand command, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//���� ���� 
		dao.updateData("bbs.updateData", command);
		
		//Ŭ���� -> jsp �� ������ �Ѱ��ֱ�
		
		//HttpSession session = request.getSession();
		
		//list �޼ҵ�� �ѱ��.
		//session.setAttribute("pageNum", command.getPageNum());
		
		return "redirect:/bbs/list.action?pageNum=" + command.getPageNum();
		//�������� �ѱ�� �Ⱦ� 
		
		
	}
	
	
	
	
	@RequestMapping(value="/bbs/deleted.action",method= {RequestMethod.GET})
	public String deleted(BoardCommand command, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		int boardNum= Integer.parseInt(request.getParameter("boardNum"));
		String pageNum= request.getParameter("pageNum");
		
		dao.deleteData("bbs.deleteData", boardNum);// deleteData�� boardNum �Ѱ��� (�Ѱ���߻�������) 
		
		
		//list �޼ҵ�� �ѱ��.
		//session.setAttribute("pageNum", command.getPageNum());
		
		return "redirect:/bbs/list.action?pageNum="+ command.getPageNum();
		//�������� �ѱ�� �Ⱦ� 
		
		
		
		
		
	}


	
}
