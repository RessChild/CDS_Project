package cds;

import java.io.File;
import java.util.HashMap;

public class ServerPDF {

	private String fileName;
	private File file;	
	private int pageNum;
	private HashMap<String, String[]> comments;
	
	public ServerPDF(String fileName, File file, int pageNum) {
		this.fileName = fileName;
		this.file = file;
		this.pageNum = pageNum;
		this.comments = new HashMap<>();
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
			comments.put(userName, new String[pageNum]);
			String[] userComment = comments.get(userName);
			userComment[page] = comment;
		}
	}
	
	public String[] getComment(String userName) {
		return comments.get(userName);
	}
	
	
}

