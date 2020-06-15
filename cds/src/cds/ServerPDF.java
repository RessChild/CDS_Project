package cds;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

public class ServerPDF {

	private String fileName;
	private File file;	
	private int pageNum;
	private HashMap<String, HashMap<Integer, String>> comments;
	
	public ServerPDF(String fileName, File file) {
		this.fileName = fileName;
		this.file = file;
		this.comments = new HashMap<>();
	}
	
	public String getFilePath() {
		return this.file.getAbsolutePath();
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	public void addComment(String userName, String comment, int page) {
		
		if (comments.containsKey(userName)) {
			comments.get(userName).put(page, comment);
		}
		else {
			comments.put(userName, new HashMap<>());
			comments.get(userName).put(page, comment);
		}
		System.out.println("USER COMMENT " + userName + "---------");
		for(Entry<Integer, String> c : comments.get(userName).entrySet()) {
			System.out.println("page : " + c.getKey() + ", comment : " + c.getValue());
		}
	}
	
	public HashMap<Integer, String> getComment(String userName) {
		return comments.get(userName);
	}
	
	
}

