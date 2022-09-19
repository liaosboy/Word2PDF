package word2pdf;

import com.spire.doc.Document;
import com.spire.doc.ToPdfParameterList;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ConvertPDF {
    private HomePage homePage;
    private String inputPath = "";
    private String savePath = "";
    public ConvertPDF(HomePage homePage){
        this.homePage = homePage;
    }

    public void setInputPath(String inputPath){
        this.inputPath = inputPath;
    }
    public void setSavePath(String savePath){
        this.savePath = savePath;
    }

    public void batchConvert(ArrayList<String> files){
        if(this.savePath.isEmpty()){
            this.savePath = this.inputPath;
        }

        int totalCnt = files.size();
        int current = 0;
        float progress = 0;
        for(String file : files){
            String progressHint = String.format("%d/%d",current,totalCnt);
            homePage.setProgressBarText(progressHint);
            this.convert2PDF(file);
            current++;
            progress = (float) current / totalCnt * 100;
            homePage.setProcess((int)progress);


        }
        homePage.setProgressBarText("");

        homePage.setProcess(0);
        homePage.showMessage("Done!");
        homePage.setAllBtnEnable(true);

    }
    public void convert2PDF(String file) {
        try {
            String savePath = this.savePath + "/" + file.replace("docx", "pdf");
            String inputPath = this.inputPath + "/" + file;

            Document doc = new Document();
            doc.loadFromFile(inputPath);

            ToPdfParameterList ppl = new ToPdfParameterList();
            ppl.isEmbeddedAllFonts(true);
            ppl.setDisableLink(false);

            doc.setJPEGQuality(80);
            doc.saveToFile(savePath, ppl);
            doc.close();
            this.postProcess(savePath);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postProcess(String PdfFile) throws Exception {
        PDDocument document = PDDocument.load(new File(PdfFile));

        document = this.removeBlandPages(document);
        document.save(PdfFile);
        document.close();

    }

    public PDDocument removeBlandPages(PDDocument document) throws Exception{
        PDFRenderer renderedDoc = new PDFRenderer(document);
        for (int pageNumber = 0; pageNumber < document.getNumberOfPages(); pageNumber++) {
            if(isBlank(renderedDoc.renderImage(pageNumber))) {
                document.removePage(pageNumber);
            }
        }
        return document;
    }

    private static Boolean isBlank(BufferedImage pageImage) throws IOException {
        BufferedImage bufferedImage = pageImage;
        long count = 0;
        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();
        Double areaFactor = (width * height) * 0.99;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color c = new Color(bufferedImage.getRGB(x, y));
                if (c.getRed() == c.getGreen() && c.getRed() == c.getBlue() && c.getRed() >= 248) {
                    count++;
                }
            }
        }
        return count >= areaFactor;
    }
}


