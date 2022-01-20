package com.util;

import org.springframework.stereotype.Service;

// 페이지 처리 

@Service("myUtil")
public class MyUtil {
	
	//전체 페이지 수 구하기 
	// numperpage 하나의 페이지에 뿌릴 갯수 / datacount 전체 데이터
	
	public int getPageCount(int numPerPage, int dataCount) {
		
		int pageCount=0; 
		pageCount = dataCount / numPerPage;
		
		if(dataCount % numPerPage != 0) {
			pageCount++;
		}
		
		return pageCount;
		
	}
	
	
	
	//페이지 처리 
	
	public String pageIndexList(int currentPage, int totalPage, String listUrl) {
		//current : 현재 페이지 , total: 총 페이지  listUrl: 데이터를 보낼 (뿌릴) 주소 : list.jsp
		
		int numPerBlock=5; //◀이전  6  7  8  9  10  ▶다음 (6~10 의 갯수=5) 
		int currentPageSetup; // ◀이전 <-에 들어가는 값 (6-1 = 5) 
		int page; // 6,7,8,9,10 숫자 값
		
		StringBuffer sb= new StringBuffer(); // 여기에 html 넣는다.
		
		if(currentPage==0||totalPage==0) { // 데이터가 없다.
			return ""; //stop ( 반환값 String 이니까 "") 
			
		}
		
		//검색 안한 상태 : list.jsp (+?)
		//검색 한 상태 : list.jsp?searchKey=subject&searchValue=3 ( +&) 
		// ? 가 잇으면 검색을 한 상태
		// ? 가 없으면 검색을 안한 상태 
		
		if(listUrl.indexOf("?")!=-1) {
			listUrl= listUrl+"&";
		}else {
			listUrl=listUrl+"?";
		}
		
		//표시할 첫 페이지의 -1한 값 (ex 이전버튼을 누르면 나오는 모습 ) [ 공식 ]
		//currentPageSetup=>이전 
		
		currentPageSetup =(currentPage/numPerBlock)*numPerBlock; 

		if(currentPage % numPerBlock ==0) {
			currentPageSetup=currentPageSetup-numPerBlock;
		}
		
		//		  1  2  3  4  5  ▶다음 
		//◀이전  6  7  8  9  10  ▶다음 
		//◀이전  11  12  
		
		
		//◀이전	
		if(totalPage>numPerBlock && currentPageSetup>0) {
			sb.append("<a href=\""+listUrl+"pageNum="+currentPageSetup+"\">◀이전</a>&nbsp;");
						//<a href="list.jsp?pageNum=10">◀이전</a>&nbsp;
			// /슬러시는 문자 인식과 과관련 
		}
		
		//바로가기 페이지  ( 이전값 +1) 
		page = currentPageSetup +1;
		
		while(page<=totalPage && page<=(currentPageSetup+numPerBlock)){
			if(page==currentPage) {
				sb.append("<font color=\"Fuchsia\">"+page+"</font>&nbsp;");
				//<font color="Fuchsia">1</font>&nbsp;
			}else {
				sb.append("<a href=\""+listUrl+"pageNum="+ page +"\">"+page + "</a>&nbsp;");
				//<a href="list.jsp?pageNum=7">7</a>&nbsp;
			}
			
			page++;
			
		}
		
		//다음▶ 
		
		if(totalPage-currentPageSetup>numPerBlock) {
			sb.append("<a href=\""+listUrl+"pageNum="+page +"\">다음▶ </a>&nbsp;");
				//<a href="list.jsp?pageNum=11">▶다음</a>&nbsp;
		}
		
		return sb.toString();
		//sb에 있는 내용을 넘겨준다
		
	}
	
	
	
	// 자바 스크립트로 페이징 처리 
	public String pageIndexList(int currentPage, int totalPage) {
		
		int numPerBlock=5;
		int currentPageSetup;
		
		int page; 
		String strList ="";
		
		if(currentPage==0) {
			return "";
		}
		
		//자바스크립트를 쓴다(onclick) 
		
		//표시할 첫 페이지
		currentPageSetup= (currentPage/numPerBlock)*numPerBlock;
		if(currentPage % numPerBlock==0) {
			currentPageSetup= currentPageSetup-numPerBlock; 
		}
		
		//◀이전
		if(totalPage>numPerBlock && currentPageSetup>0) {
			strList += "<a onclick='listPage(" + currentPageSetup + ");'>◀이전</a>&nbsp;";
		}
		
		//바로가기페이지(1,2,3,4,5...) 
		page = currentPageSetup + 1;  //시작
		
		while((page<=totalPage) && (page<=currentPageSetup+numPerBlock)) {
			
			if(page==currentPage) {
				strList += "<font color='Fuchsia'>" + page + "</font>&nbsp;";
				//현재보고있는 페이지가, 현재페이지인경우 핑크색으로 표시한다(1,2,3) 
			}else {
				strList += "<a onclick='listPage(" + page + ");'>" + page +"</a>&nbsp;";
			}
			
			page++; 
		
		}
		
		//다음▶
		// 위에 page 값을 가져와서 그대로 쓴다. (위에서 빠져나온경우 값이 6을 갖고있다) 
		
		if(totalPage-currentPageSetup>numPerBlock) {
			strList += "<a onclick='listPage(" + page + ");'>다음▶</a>&nbsp;";
		}
		
		return strList;


	}
	

}
