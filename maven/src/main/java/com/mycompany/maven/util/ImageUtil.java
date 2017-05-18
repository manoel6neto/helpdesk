package com.mycompany.maven.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Manoel
 */
public class ImageUtil {

    public static void generateImages(UploadedFile uploadedFile, String highPath, String mediumPath, String thumbPath) throws IOException {
        BufferedImage originalImage = ImageIO.read(uploadedFile.getInputstream());
        int mediumHeight = (int) (originalImage.getHeight() * 0.5);
        int mediumWidth = (int) (originalImage.getHeight() * 0.5);
        int thumbHeight = (int) (originalImage.getHeight() * 0.2);
        int thumbWidth = (int) (originalImage.getHeight() * 0.2);

        File highImage = new File(highPath);
        highImage.getParentFile().mkdirs();
        highImage.createNewFile();
        ImageIO.write(originalImage, "JPG", highImage);

        File mediumImage = new File(mediumPath);
        mediumImage.getParentFile().mkdirs();
        mediumImage.createNewFile();
        BufferedImage resizeImage = ImageUtil.resizeImage(originalImage, mediumWidth, mediumHeight);
        ImageIO.write(resizeImage, "JPG", mediumImage);

        File thumbImage = new File(thumbPath);
        thumbImage.getParentFile().mkdirs();
        thumbImage.createNewFile();
        resizeImage = ImageUtil.resizeImage(resizeImage, thumbWidth, thumbHeight);
        ImageIO.write(resizeImage, "JPG", thumbImage);
    }

    public static void deleteImages(String highPath, String mediumPath, String thumbPath) {
        File fileToRemove = new File(highPath);
        if (fileToRemove.exists()) {
            fileToRemove.delete();
        }
        fileToRemove = new File(mediumPath);
        if (fileToRemove.exists()) {
            fileToRemove.delete();
        }
        fileToRemove = new File(thumbPath);
        if (fileToRemove.exists()) {
            fileToRemove.delete();
        }
    }

    public static BufferedImage progressiveResizeImage(BufferedImage originalImage, int destWidth, int destHeight) {

        BufferedImage resizeImage;

        int halfWidth = originalImage.getWidth(null) / 2;
        int halfHeight = originalImage.getHeight(null) / 2;

        int originalSize = halfWidth * halfHeight;
        int destSize = destWidth * destHeight;
        int proporcao = originalSize / destSize;

        while (proporcao > 2) {
            originalImage = resizeImage(originalImage, halfWidth, halfHeight);//reduz à metade do tamanho;

            halfWidth = originalImage.getWidth(null) / 2;
            halfHeight = originalImage.getHeight(null) / 2;

            originalSize = halfWidth * halfHeight;
            proporcao = originalSize / destSize;
        }

        resizeImage = resizeImage(originalImage, destWidth, destHeight);

        return resizeImage;
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int destWidth, int destHeight) {

        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        int imageWidth = originalImage.getWidth(null);
        int imageHeight = originalImage.getHeight(null);
        double aspectRatio = (double) imageWidth / (double) imageHeight;
        double thumbRatio = (double) destWidth / (double) destHeight;

        if (thumbRatio < aspectRatio) {
            destHeight = (int) (destWidth / aspectRatio);
        } else {
            destWidth = (int) (destHeight * aspectRatio);
        }

        BufferedImage resizedImage = new BufferedImage(destWidth, destHeight, type);
        Graphics2D g = resizedImage.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(originalImage, 0, 0, destWidth, destHeight, null);
        g.dispose();

        return resizedImage;
    }
}
