package com.util;

import org.springframework.stereotype.Service;

// ������ ó�� 

@Service("myUtil")
public class MyUtil {
	
	//��ü ������ �� ���ϱ� 
	// numperpage �ϳ��� �������� �Ѹ� ���� / datacount ��ü ������
	
	public int getPageCount(int numPerPage, int dataCount) {
		
		int pageCount=0; 
		pageCount = dataCount / numPerPage;
		
		if(dataCount % numPerPage != 0) {
			pageCount++;
		}
		
		return pageCount;
		
	}
	
	
	
	//������ ó�� 
	
	public String pageIndexList(int currentPage, int totalPage, String listUrl) {
		//current : ���� ������ , total: �� ������  listUrl: �����͸� ���� (�Ѹ�) �ּ� : list.jsp
		
		int numPerBlock=5; //������  6  7  8  9  10  ������ (6~10 �� ����=5) 
		int currentPageSetup; // ������ <-�� ���� �� (6-1 = 5) 
		int page; // 6,7,8,9,10 ���� ��
		
		StringBuffer sb= new StringBuffer(); // ���⿡ html �ִ´�.
		
		if(currentPage==0||totalPage==0) { // �����Ͱ� ����.
			return ""; //stop ( ��ȯ�� String �̴ϱ� "") 
			
		}
		
		//�˻� ���� ���� : list.jsp (+?)
		//�˻� �� ���� : list.jsp?searchKey=subject&searchValue=3 ( +&) 
		// ? �� ������ �˻��� �� ����
		// ? �� ������ �˻��� ���� ���� 
		
		if(listUrl.indexOf("?")!=-1) {
			listUrl= listUrl+"&";
		}else {
			listUrl=listUrl+"?";
		}
		
		//ǥ���� ù �������� -1�� �� (ex ������ư�� ������ ������ ��� ) [ ���� ]
		//currentPageSetup=>���� 
		
		currentPageSetup =(currentPage/numPerBlock)*numPerBlock; 

		if(currentPage % numPerBlock ==0) {
			currentPageSetup=currentPageSetup-numPerBlock;
		}
		
		//		  1  2  3  4  5  ������ 
		//������  6  7  8  9  10  ������ 
		//������  11  12  
		
		
		//������	
		if(totalPage>numPerBlock && currentPageSetup>0) {
			sb.append("<a href=\""+listUrl+"pageNum="+currentPageSetup+"\">������</a>&nbsp;");
						//<a href="list.jsp?pageNum=10">������</a>&nbsp;
			// /�����ô� ���� �νİ� ������ 
		}
		
		//�ٷΰ��� ������  ( ������ +1) 
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
		
		//������ 
		
		if(totalPage-currentPageSetup>numPerBlock) {
			sb.append("<a href=\""+listUrl+"pageNum="+page +"\">������ </a>&nbsp;");
				//<a href="list.jsp?pageNum=11">������</a>&nbsp;
		}
		
		return sb.toString();
		//sb�� �ִ� ������ �Ѱ��ش�
		
	}
	
	
	
	// �ڹ� ��ũ��Ʈ�� ����¡ ó�� 
	public String pageIndexList(int currentPage, int totalPage) {
		
		int numPerBlock=5;
		int currentPageSetup;
		
		int page; 
		String strList ="";
		
		if(currentPage==0) {
			return "";
		}
		
		//�ڹٽ�ũ��Ʈ�� ����(onclick) 
		
		//ǥ���� ù ������
		currentPageSetup= (currentPage/numPerBlock)*numPerBlock;
		if(currentPage % numPerBlock==0) {
			currentPageSetup= currentPageSetup-numPerBlock; 
		}
		
		//������
		if(totalPage>numPerBlock && currentPageSetup>0) {
			strList += "<a onclick='listPage(" + currentPageSetup + ");'>������</a>&nbsp;";
		}
		
		//�ٷΰ���������(1,2,3,4,5...) 
		page = currentPageSetup + 1;  //����
		
		while((page<=totalPage) && (page<=currentPageSetup+numPerBlock)) {
			
			if(page==currentPage) {
				strList += "<font color='Fuchsia'>" + page + "</font>&nbsp;";
				//���纸���ִ� ��������, �����������ΰ�� ��ũ������ ǥ���Ѵ�(1,2,3) 
			}else {
				strList += "<a onclick='listPage(" + page + ");'>" + page +"</a>&nbsp;";
			}
			
			page++; 
		
		}
		
		//������
		// ���� page ���� �����ͼ� �״�� ����. (������ �������°�� ���� 6�� �����ִ�) 
		
		if(totalPage-currentPageSetup>numPerBlock) {
			strList += "<a onclick='listPage(" + page + ");'>������</a>&nbsp;";
		}
		
		return strList;


	}
	

}
