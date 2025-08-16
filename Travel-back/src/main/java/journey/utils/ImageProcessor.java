package journey.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

/**
 * 圖片處理工具類
 * 提供圖片壓縮、尺寸調整等功能
 * 
 * @author Journey Team
 * @version 1.0
 * @since 2025-07-08
 */
@Component
public class ImageProcessor {

    /**
     * 圖片配置常數
     */
    public static class ImageConfig {
        // 評論圖片配置
        public static final int COMMENT_MAX_SIZE = 400; // 最大尺寸 (px)
        public static final float COMMENT_QUALITY = 0.6f; // 壓縮品質 (60%)
        public static final long COMMENT_MAX_SIZE_BYTES = 500 * 1024; // 最大檔案大小 (500KB)

        // 商家圖片配置
        public static final int VENDOR_MAX_SIZE = 800; // 最大尺寸 (px)
        public static final float VENDOR_QUALITY = 0.8f; // 壓縮品質 (80%)
        public static final long VENDOR_MAX_SIZE_BYTES = 2 * 1024 * 1024; // 最大檔案大小 (2MB)
    }

    /**
     * 處理圖片（壓縮和調整尺寸）
     * 
     * @param originalImage 原始圖片
     * @param targetType    目標類型 (comment, lodging, room_type)
     * @return 處理後的圖片位元組陣列
     * @throws IOException 處理失敗時拋出
     */
    public byte[] processImage(BufferedImage originalImage, String targetType) throws IOException {
        // 根據目標類型取得配置
        int maxSize = getMaxSize(targetType);
        float quality = getQuality(targetType);

        // 調整尺寸
        BufferedImage resizedImage = resizeImage(originalImage, maxSize);

        // 壓縮圖片
        return compressImage(resizedImage, quality);
    }

    /**
     * 調整圖片尺寸
     * 
     * @param originalImage 原始圖片
     * @param maxSize       最大尺寸
     * @return 調整後的圖片
     */
    private BufferedImage resizeImage(BufferedImage originalImage, int maxSize) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // 計算新的尺寸
        int newWidth, newHeight;
        if (originalWidth > originalHeight) {
            // 寬圖
            newWidth = Math.min(originalWidth, maxSize);
            newHeight = (int) ((double) originalHeight * newWidth / originalWidth);
        } else {
            // 高圖或正方形
            newHeight = Math.min(originalHeight, maxSize);
            newWidth = (int) ((double) originalWidth * newHeight / originalHeight);
        }

        // 如果尺寸沒有變化，直接返回原圖
        if (newWidth == originalWidth && newHeight == originalHeight) {
            return originalImage;
        }

        // 建立新圖片
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();

        // 設定渲染品質
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING,
                java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        // 繪製調整後的圖片
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        return resizedImage;
    }

    /**
     * 壓縮圖片
     * 
     * @param image   要壓縮的圖片
     * @param quality 壓縮品質 (0.0-1.0)
     * @return 壓縮後的圖片位元組陣列
     * @throws IOException 壓縮失敗時拋出
     */
    private byte[] compressImage(BufferedImage image, float quality) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 使用 JPEG 格式壓縮
        javax.imageio.ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
        javax.imageio.ImageWriteParam writeParam = writer.getDefaultWriteParam();
        writeParam.setCompressionMode(javax.imageio.ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionQuality(quality);

        javax.imageio.stream.ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
        writer.setOutput(imageOutputStream);

        writer.write(null, new javax.imageio.IIOImage(image, null, null), writeParam);

        writer.dispose();
        imageOutputStream.close();

        return outputStream.toByteArray();
    }

    /**
     * 根據目標類型取得最大尺寸
     * 
     * @param targetType 目標類型
     * @return 最大尺寸
     */
    private int getMaxSize(String targetType) {
        switch (targetType) {
            case "comment":
                return ImageConfig.COMMENT_MAX_SIZE;
            case "lodging":
            case "room_type":
                return ImageConfig.VENDOR_MAX_SIZE;
            default:
                return ImageConfig.VENDOR_MAX_SIZE; // 預設使用商家配置
        }
    }

    /**
     * 根據目標類型取得壓縮品質
     * 
     * @param targetType 目標類型
     * @return 壓縮品質
     */
    private float getQuality(String targetType) {
        switch (targetType) {
            case "comment":
                return ImageConfig.COMMENT_QUALITY;
            case "lodging":
            case "room_type":
                return ImageConfig.VENDOR_QUALITY;
            default:
                return ImageConfig.VENDOR_QUALITY; // 預設使用商家配置
        }
    }

    /**
     * 檢查圖片是否需要處理
     * 
     * @param image      圖片
     * @param targetType 目標類型
     * @return 是否需要處理
     */
    public boolean needsProcessing(BufferedImage image, String targetType) {
        int maxSize = getMaxSize(targetType);
        return image.getWidth() > maxSize || image.getHeight() > maxSize;
    }

    /**
     * 從位元組陣列建立 BufferedImage
     * 
     * @param imageBytes 圖片位元組陣列
     * @return BufferedImage
     * @throws IOException 建立失敗時拋出
     */
    public BufferedImage createBufferedImage(byte[] imageBytes) throws IOException {
        try (InputStream inputStream = new ByteArrayInputStream(imageBytes)) {
            return ImageIO.read(inputStream);
        }
    }

    /**
     * 取得圖片資訊
     * 
     * @param image 圖片
     * @return 圖片資訊字串
     */
    public String getImageInfo(BufferedImage image) {
        return String.format("尺寸: %dx%d, 類型: %s",
                image.getWidth(), image.getHeight(),
                image.getType() == BufferedImage.TYPE_INT_RGB ? "RGB" : "其他");
    }
}