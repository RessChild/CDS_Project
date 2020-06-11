package cds;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

public class Pdf {
	
	private String filePath;
	private File pdfFile;
	private PDDocument doc;
	private PDFRenderer renderer;
	
	private List<BufferedImage> pageImgs;
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
	
	
	
}
