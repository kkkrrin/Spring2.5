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


/* 어노테이션 종류 
@Repository : 저장소 컴포넌트(DAO 등).
@Service : 상태없는 서비스 컴포넌트.
@Autowired 어노테이션 : Spring에서 의존관계를 자동으로 설정
@Resource 어노테이션 : 의존하는 빈 객체를 전달 할 때 사용

@Resource 는 이름을 지정할 수 있으나 @Autowired는 이름을 지정할 수 없고 id 값과 일치해야한다.
*/
@Controller("bbs.boardController")
public class BoardController {
	
	//어노테이션으로 설정한 dao 가져오기(객체생성?)
	@Resource(name="dao")
	private CommonDAO dao; 
	
	@Resource(name="myUtil")
	private MyUtil myUtil;
	
	
	@RequestMapping(value="/bbs/created.action",method= {RequestMethod.GET,RequestMethod.POST})
	public String created(BoardCommand command, HttpServletRequest request) throws Exception{
		
		if(command==null|| command.getMode()==null||command.getMode().equals("")) {
			
			request.setAttribute("mode", "insert");//mode에 insert 라는 문장 넣는다
			
			return "board/created";
			
		}
		
		//dao 객체 생성 (어노테이션) @Repository("dao") = CommonDAOImpl 에서 메소드 위에 선언해준다.
		int boardNumMax = dao.getIntValue("bbs.boardNumMax"); 
		
		command.setBoardNum(boardNumMax+1);
		command.setIpAddr(request.getRemoteAddr());
		
		//System.out.println(boardNumMax); 검증
		
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
		
		//세션으로 pageNum을 받는다. (updateSubmit()에서 보낸 pageNum 업데이트할때 사용 )
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
		
		if(request.getMethod().equalsIgnoreCase("GET")) { //get방식이면 한글로 들어옴?(검색?)
			searchValue=URLDecoder.decode(searchValue,"UTF-8"); 
		}
		
		Map<String, Object> hMap = new HashMap<String, Object>();
		hMap.put("searchKey", searchKey);
		hMap.put("searchValue", searchValue);
		
		
		totalDataCount = dao.getIntValue("bbs.dataCount",hMap); //전체데이터갯수 
		
		//전체페이지
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
		
		//일렬번호 
		
		int listNum,n=0;
		ListIterator<Object> it= lists.listIterator();
		
		while(it.hasNext()) {
			
			BoardCommand data = (BoardCommand)it.next();
			
			listNum = totalDataCount-(start+n-1);
			
			data.setListNum(listNum);
			
			n++;
			
		}
		
		
		//주소정리 (사용자정의)
		
		String params="";
		String urlList="";
		String urlArticle="";
		
		if(!searchValue.equals("")) {
			searchValue= URLEncoder.encode(searchValue,"UTF-8");
			params="searchKey="+searchKey + "&searchValue="+searchValue;
		}
		
		//검색안함
		if(params.equals("")) {
			urlList = cp+"/bbs/list.action";
			urlArticle= cp+"/bbs/article.action?pageNum="+currentPage;
		} else {//검색함
			urlList = cp+"/bbs/list.action?"+params;
			urlArticle= cp+"/bbs/article.action?pageNum="+currentPage+"&"+params;
			
		}
		
		request.setAttribute("lists", lists);
		request.setAttribute("urlArticle", urlArticle);
		request.setAttribute("pageNum", currentPage);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("totalDataCount", totalDataCount);
		request.setAttribute("pageIndexList", myUtil.pageIndexList(currentPage, totalPage, urlList));
		
		//위 데이터를 가지고 return으로 간다. 
		
		return "board/list";
		
		
	}
		
	
	
	
	@RequestMapping(value="/bbs/article.action",method= {RequestMethod.GET,RequestMethod.POST})
	public String article(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		int boardNum= Integer.parseInt(request.getParameter("boardNum"));
		String pageNum= request.getParameter("pageNum");
		
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		if(searchValue==null) { //검색안함
			searchKey="subject";
			searchValue="";
		}
		
		if(request.getMethod().equalsIgnoreCase("GET")) {
			searchValue = URLDecoder.decode(searchValue,"UTF-8");
		}
		
		//조회수증가
		dao.updateData("bbs.updateHitCount",boardNum);
		
		//하나의 데이터 읽어오기
		// 여기서 dto를 선언해서 request에 보내주고, jsp에서 dto.로 받아서 사용한다. 
		BoardCommand dto=(BoardCommand)dao.getReadData("bbs.readData", boardNum);
		
		if(dto==null) {
			return "redirect:/bbs/list.action";
		}
		
		int lineSu=dto.getContent().split("\r\n").length;
		
		dto.setContent(dto.getContent().replaceAll("\r\n", "<br/>"));
		
		//넘기기
		Map<String,Object> hMap= new HashMap<>();
		hMap.put("searchKey", searchKey);
		hMap.put("searchvalue",searchValue);
		hMap.put("boardNum", boardNum);
		
		
		//sqlMap으로 보내기 (이전글/다음글)
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
		
		//주소정리
		String params ="pageNum=" +pageNum;
		
		if(!searchValue.equals("")) {
			searchValue= URLEncoder.encode("searchValue","UTF-8");
			params +="&searchKey="+ searchKey; 
			params +="&searchValue"+ searchValue;
		}
		
		
		//데이터 넘기기
		
		request.setAttribute("dto", dto);
		request.setAttribute("preBoardNum", preBoardNum);
		request.setAttribute("preSubject", preSubject);
		request.setAttribute("nextBoardNum", nextBoardNum);
		request.setAttribute("nextSubject", nextSubject);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("params", params);
		request.setAttribute("lineSu", lineSu);
		
		//위 데이터 가지고 article.jsp로 간다. 
		
		return "board/article";
		
	}
	
	
	
	@RequestMapping(value="/bbs/updated.action",method= {RequestMethod.GET})
	public String updated(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//창띄우기 
		int boardNum= Integer.parseInt(request.getParameter("boardNum"));
		String pageNum= request.getParameter("pageNum");
		
		BoardCommand dto= (BoardCommand)dao.getReadData("bbs.readData", boardNum);
		
		if(dto==null) {
			return "redirect:/bbs/list.action?pageNum"+pageNum; //원래페이지로 돌아가기 
			//return "redirect:/bbs/list.action"; 
		}
		
		request.setAttribute("mode", "update"); // created.jsp 밑에 hidden에 mode로 넘어간다. 
		request.setAttribute("pageNum",pageNum);
		request.setAttribute("dto", dto);

		
		return "board/created";
	}
	
	
	@RequestMapping(value="/bbs/updated.action",method= {RequestMethod.POST})
	public String updatedSubmit(BoardCommand command, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//실제 수정 
		dao.updateData("bbs.updateData", command);
		
		//클래스 -> jsp 로 페이지 넘겨주기
		
		//HttpSession session = request.getSession();
		
		//list 메소드로 넘긴다.
		//session.setAttribute("pageNum", command.getPageNum());
		
		return "redirect:/bbs/list.action?pageNum=" + command.getPageNum();
		//세션으로 넘기기 싫어 
		
		
	}
	
	
	
	
	@RequestMapping(value="/bbs/deleted.action",method= {RequestMethod.GET})
	public String deleted(BoardCommand command, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		int boardNum= Integer.parseInt(request.getParameter("boardNum"));
		String pageNum= request.getParameter("pageNum");
		
		dao.deleteData("bbs.deleteData", boardNum);// deleteData에 boardNum 넘겨줌 (넘겨줘야삭제가능) 
		
		
		//list 메소드로 넘긴다.
		//session.setAttribute("pageNum", command.getPageNum());
		
		return "redirect:/bbs/list.action?pageNum="+ command.getPageNum();
		//세션으로 넘기기 싫어 
		
		
		
		
		
	}


	
}
