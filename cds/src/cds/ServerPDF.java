package cds;

import java.io.File;
import java.util.HashMap;

public class ServerPDF {
	
	// PDF ���� �̸�
	private String fileName;
	
	// PDF ���� ��ü
	private File file;
	
	// PDF �� ������ ��
	private int allPagesNum;
	
	// <���� �̸�, ������ �� �ּ��� ������ �迭> ���·� �ּ� ����
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

