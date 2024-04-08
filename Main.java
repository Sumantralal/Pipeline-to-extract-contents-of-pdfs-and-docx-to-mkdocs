package doc_extracter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select an option:");
        System.out.println("1. Convert DOCX file");
        System.out.println("2. Convert PDF file");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                String inputFilePath = "/Users/sumantralal/IdeaProjects/doc_extracter/src/source1.docx"; // Path to your DOCX file
                String imageFolderPath = "/Users/sumantralal/Desktop/storefront/myproject/myprojects/docs/imagesfordocx/"; // Folder to save images
                String outputMarkdownPath = "/Users/sumantralal/Desktop/storefront/myproject/myprojects/docs/index.md"; // Output Markdown file

                try {
                    docx.extractTextAndImages(inputFilePath, imageFolderPath, outputMarkdownPath);
                    System.out.println("Extraction complete.");
                } catch (IOException | InvalidFormatException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                String pdfFilePath = "/Users/sumantralal/IdeaProjects/doc_extracter/src/TSD.pdf";
                String outputFolderPath = "/Users/sumantralal/Desktop/storefront/myproject/myprojects/docs/";
                String outputImagePath = "/Users/sumantralal/Desktop/storefront/myproject/myprojects/docs/imagesforpdf/";

                try {
                    pdf.extractTextAndImages(pdfFilePath, outputFolderPath, outputImagePath);
                    System.out.println("Extraction complete.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Invalid option!");
        }

        scanner.close();
    }
}
