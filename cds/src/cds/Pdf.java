package cds;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

public class Pdf {
	
	// PDF 파일 경로
	private String filePath;
	
	// 불러온 PDF 파일 객체
	private File pdfFile;
	
	private PDDocument doc;
	private PDFRenderer renderer;
	
	// PDF 페이지를 이미지로 렌더링해서 저장하기 위한 리스트
	private List<BufferedImage> pageImgs;
	
	// 현재 사용자가 보고 있는 페이지
	private int currPageNum;
	
	public Pdf(String filePath) {
		this.filePath = filePath;
		pageImgs = new ArrayList<>();
		currPageNum = 0;
		
		loadPDF();
	}
	
	public void loadPDF() {
		
		pdfFile = new File(filePath);
		
		try {
			
			doc = PDDocument.load(pdfFile);
			renderPdfToImage();
			
		} catch(IOException ex) {
			System.err.println("Failed to load pdf document");
			ex.printStackTrace();
		}
	}
	
	public void renderPdfToImage() {
		renderer = new PDFRenderer(doc);
		for (int i = 0; i < doc.getNumberOfPages(); i++) {
			try {
				// 각 PDF 페이지를 이미지로 렌더링해서 리스트에 추가
				pageImgs.add(renderer.renderImage(i));
			} catch (IOException e) {
				System.err.println("Failed to render pdf to image");
				e.printStackTrace();
			}
		}
	}
	
	public BufferedImage getCurrPageImage() {
		return pageImgs.get(currPageNum);
	}
	
	public void decrementPageNum() {
		if (currPageNum > 0) {
			currPageNum--;
		}
	}
	
	public void incrementPageNum() {
		if (currPageNum < doc.getNumberOfPages() - 1) {
			currPageNum++;
		}
	}
	
	public String getFileName() {
		return this.pdfFile.getName();
	}
	
	public int getCurrentPageNum() {
		return this.currPageNum;
	}
}
