package doc_extracter;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class pdf {


    public static void extractTextAndImages(String pdfFilePath, String outputFolderPath, String outputImagePath) throws IOException {
        List<String> textList = new ArrayList<>();
        List<String> imageList = new ArrayList<>();

        try (PDDocument document = PDDocument.load(new File(pdfFilePath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            for (int i = 1; i <= document.getNumberOfPages(); i++) {
                stripper.setStartPage(i);
                stripper.setEndPage(i);
                String text = stripper.getText(document);
                // Append "<br>" at the end of each line
                text = text.replaceAll("\\r?\\n", "<br>\n");
                textList.add(text);
            }

            // Extract images
            PDPageTree pages = document.getPages();
            int imageIndex = 1;
            for (PDPage page : pages) {
                PDResources pdResources = page.getResources();
                for (COSName name : pdResources.getXObjectNames()) {
                    PDXObject xobject = pdResources.getXObject(name);
                    if (xobject instanceof PDImageXObject) {
                        PDImageXObject image = (PDImageXObject) xobject;
                        String imageName = "image" + imageIndex + ".png";
                        imageList.add(imageName);
                        File imageFile = new File(outputImagePath + imageName);
                        BufferedImage bImage = image.getImage();
                        ImageIO.write(bImage, "png", imageFile);
                        imageIndex++;
                    }
                }
            }
        }

        // Write extracted text and image links to index.md
        String indexMdFilePath = outputFolderPath + "index.md";
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(indexMdFilePath)))) {
            for (int i = 0; i < textList.size(); i++) {
                writer.write(textList.get(i));
                if (i < imageList.size()) {
                    writer.write("![Image " + (i + 1) + "](" + "imagesforpdf/" + imageList.get(i) + ")\n");
                    writer.write("<br>\n"); // Insert "<br>" after each image
                }
            }
        }
    }
}
