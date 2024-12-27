package com.backend.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class ThumbnailService {

    private final SftpService sftpService;

    private void ensureDirectoryExists(String directoryPath) {


        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                log.info("Directory created: {}", directoryPath);
            } else {
                log.error("Failed to create directory: {}", directoryPath);
            }
        }
    }

    public String generateThumbnailIfNotExists(String remoteFilePath, String savedName) {
        ensureDirectoryExists("thumbnails");
        ensureDirectoryExists("tmp");

        try {
            String thumbnailDir = "thumbnails/";
            String thumbnailPath = thumbnailDir + savedName + ".jpg";

            // Check if the thumbnail already exists locally
            File thumbnailFile = new File(thumbnailPath);
            if (!thumbnailFile.exists()) {
                // Download the file from SFTP
                String localFilePath = "tmp/" + savedName;
                sftpService.downloadFile(remoteFilePath, savedName, localFilePath);

                // Generate a thumbnail based on the file extension
                String extension = getFileExtension(savedName);

                switch (extension) {
                    case "pdf":
                        generatePdfThumbnail(localFilePath, thumbnailPath);
                        break;
                    case "txt":
                        return "/icons/text-icon.png"; // Default icon for text files
                    case "docx":
                    case "doc":
                        generateWordThumbnail(localFilePath, thumbnailPath);
                        break;
                    case "xlsx":
                    case "xls":
                        generateExcelThumbnail(localFilePath, thumbnailPath);
                        break;
                    case "pptx":
                    case "ppt":
                        generatePptThumbnail(localFilePath, thumbnailPath);
                        break;
                    case "jpg":
                    case "jpeg":
                    case "png":
                    case "gif":
                        generateImageThumbnail(localFilePath, thumbnailPath); // Generate image thumbnail
                        break;
                    default:
                        return ""; // Default icon for unsupported file types
                }

                // Upload the generated thumbnail to the SFTP server
                boolean uploadSuccess = sftpService.thumbnailFileUploads(thumbnailPath, savedName + ".jpg");
                if (!uploadSuccess) {
                    log.error("Failed to upload thumbnail to SFTP: {}", savedName);
                    return "/icons/default-thumbnail.jpg"; // Return a default thumbnail in case of failure
                }

                // Delete the local temporary file
                deleteLocalFile(thumbnailPath);
                deleteLocalFile(localFilePath);
            }

            // Return the SFTP URL for the uploaded thumbnail
            return "/uploads/thumbnails/" + savedName + ".jpg";
        } catch (Exception e) {
            log.error("Failed to generate thumbnail for file: " + savedName, e);
            return "/icons/default-thumbnail.jpg"; // Return a default thumbnail in case of an exception
        }
    }


    private void deleteLocalFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.delete()) {
            log.info("임시 파일 삭제 완료: " + filePath);
        } else {
            log.warn("임시 파일 삭제 실패: " + filePath);
        }
    }

    // 파일 확장자 추출 메서드
    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf(".");
        return lastIndexOfDot == -1 ? "" : fileName.substring(lastIndexOfDot + 1).toLowerCase();
    }

    // PDF 썸네일 생성
    private void generatePdfThumbnail(String pdfPath, String thumbnailPath) throws Exception {
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImage(0); // 첫 페이지 렌더링
            ImageIO.write(image, "JPEG", new File(thumbnailPath));
        }
    }

    private String getTextFilePreview(String filePath, int maxLines) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder preview = new StringBuilder();
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null && count < maxLines) {
                preview.append(line).append("\n");
                count++;
            }
            return preview.toString();
        } catch (Exception e) {
            log.error("Failed to read text file for preview: " + filePath, e);
            return "Preview not available";
        }
    }

    private void generateWordThumbnail(String docPath, String thumbnailPath) throws Exception {
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(docPath))) {
            String previewText = doc.getParagraphs().stream()
                    .limit(3) // 첫 3개의 문단 가져오기
                    .map(XWPFParagraph::getText)
                    .reduce("", (a, b) -> a + "\n" + b);

            // 썸네일 생성 대신 텍스트 미리보기 저장
            BufferedImage thumbnail = createTextThumbnail(previewText);
            ImageIO.write(thumbnail, "JPEG", new File(thumbnailPath));
        }
    }

    private BufferedImage createTextThumbnail(String text) {
        BufferedImage image = new BufferedImage(400, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 400, 200);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 12));

        // 줄 바꿈 처리
        String[] lines = text.split("\n");
        int y = 20;
        for (String line : lines) {
            g.drawString(line, 20, y);
            y += 20; // 줄 간격
            if (y > 180) break; // 썸네일 크기 제한
        }

        g.dispose();
        return image;
    }


    private void generateExcelThumbnail(String excelPath, String thumbnailPath) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(excelPath))) {
            XSSFSheet sheet = workbook.getSheetAt(0); // 첫 번째 시트
            StringBuilder previewText = new StringBuilder();

            sheet.forEach(row -> {
                row.forEach(cell -> previewText.append(cell.toString()).append(" "));
                previewText.append("\n");
            });

            BufferedImage thumbnail = createTextThumbnail(previewText.toString());
            ImageIO.write(thumbnail, "JPEG", new File(thumbnailPath));
        }
    }

    private void generatePptThumbnail(String pptPath, String thumbnailPath) throws Exception {
        try (XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(pptPath))) {
            XSLFSlide slide = ppt.getSlides().get(0); // 첫 번째 슬라이드
            Dimension dimension = ppt.getPageSize();

            BufferedImage img = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = img.createGraphics();

            graphics.setPaint(Color.WHITE);
            graphics.fillRect(0, 0, dimension.width, dimension.height);
            slide.draw(graphics);

            ImageIO.write(img, "JPEG", new File(thumbnailPath));
        }
    }

    private void generateImageThumbnail(String imagePath, String thumbnailPath) throws Exception {
        File inputFile = new File(imagePath);

        // 파일 존재 여부 확인
        if (!inputFile.exists()) {
            log.error("File does not exist: {}", imagePath);
            throw new IOException("File not found: " + imagePath);
        }

        // 파일이 유효한 이미지인지 확인
        BufferedImage originalImage = ImageIO.read(inputFile);
        if (originalImage == null) {
            log.error("Invalid image file: {}", imagePath);
            throw new IOException("Invalid image file: " + imagePath);
        }

        // 썸네일 크기 설정
        int targetWidth = 200;
        int targetHeight = 200;

        BufferedImage thumbnail = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = thumbnail.createGraphics();
        graphics.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics.dispose();

        // 썸네일 저장
        ImageIO.write(thumbnail, "JPEG", new File(thumbnailPath));
        log.info("Thumbnail generated: {}", thumbnailPath);
    }

}
