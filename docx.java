package doc_extracter;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class docx {

    public static void extractTextAndImages(String inputFilePath, String imageFolderPath, String outputMarkdownPath)
            throws IOException, InvalidFormatException {
        File file = new File(inputFilePath);
        XWPFDocument docx = new XWPFDocument(OPCPackage.open(new FileInputStream(file)));

        File imagesFolder = new File(imageFolderPath);
        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs();
        }

        FileOutputStream outputStream = new FileOutputStream(outputMarkdownPath);

        int imageIndex = 1;

        for (IBodyElement element : docx.getBodyElements()) {
            if (element instanceof XWPFParagraph) {
                XWPFParagraph paragraph = (XWPFParagraph) element;
                for (XWPFRun run : paragraph.getRuns()) {
                    for (XWPFPicture picture : run.getEmbeddedPictures()) {
                        // Extract and save image
                        XWPFPictureData pictureData = picture.getPictureData();
                        String imageName = imageFolderPath + "image" + imageIndex + "." + pictureData.suggestFileExtension();
                        FileOutputStream imageOutputStream = new FileOutputStream(imageName);
                        imageOutputStream.write(pictureData.getData());
                        imageOutputStream.close();

                        // Write image link in the markdown file
                        outputStream.write(("![Image " + imageIndex + "](" + "imagesfordocx/image" + imageIndex + "." + pictureData.suggestFileExtension() + ")\n\n").getBytes());
                        imageIndex++;
                    }
                }
                // Write text content to Markdown file
                String text = paragraph.getText();
                outputStream.write((text + "\n\n").getBytes());
            } else if (element instanceof XWPFTable) {
                // Handle tables if necessary
            }
        }

        outputStream.close();
        docx.close();
    }
}
