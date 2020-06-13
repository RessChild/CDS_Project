package cds;

import java.io.File;
import java.util.HashMap;

public class ServerPDF {
	
	// PDF 파일 이름
	private String fileName;
	
	// PDF 파일 객체
	private File file;
	
	// PDF 총 페이지 수
	private int allPagesNum;
	
	// <유저 이름, 페이지 별 주석을 저장한 배열> 형태로 주석 저장
	private HashMap<String, String[]> comments;
	
	public ServerPDF(String fileName, File file, int pageNum) {
		this.fileName = fileName;
		this.file = file;
		this.allPagesNum = pageNum;
		this.comments = new HashMap<>();
	}
	
	public ServerPDF(String fileName, int allPagesNum) {
		this.fileName = fileName;
		this.allPagesNum = allPagesNum;
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	public void addComment(String userName, String comment, int page) {
		
		if (comments.containsKey(userName)) {
			String[] userComment = comments.get(userName);
			userComment[page] = comment;
		}
		else {
			comments.put(userName, new String[allPagesNum]);
			String[] userComment = comments.get(userName);
			userComment[page] = comment;
		}
	}
	
	
}

