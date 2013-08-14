package com.lebo.util;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.ResampleOp;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {

    public static BufferedImage resizeImageJdk(int width, int height, BufferedImage originalImage) {
        int imageType = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        BufferedImage resizedImage = new BufferedImage(width, height, imageType);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        //Hints，似乎没有什么效果
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        return resizedImage;
    }

    /**
     * 按比例缩放图片。
     *
     * @param range         缩放后的图片宽和高不会超过这个值
     * @param originalImage 原始图片
     * @return 如果原始图片宽和高小于等于 range，则返回原始图片，否则返回缩放后的图片
     */
    public static BufferedImage resizeImage(int range, BufferedImage originalImage) {
        //原始图片宽和高不大于指定范围，返回原始图片
        if (range >= originalImage.getHeight() && originalImage.getHeight() >= originalImage.getWidth()) {
            return originalImage;
        }

        //按比例缩放图片
        int width;
        int height;
        if (originalImage.getHeight() > originalImage.getWidth()) {
            height = range;
            width = (height * originalImage.getWidth()) / originalImage.getHeight();
        } else {
            width = range;
            height = (width * originalImage.getHeight()) / originalImage.getWidth();
        }

        return resizeImage(width, height, originalImage);
    }

    public static BufferedImage resizeImage(int width, int height, BufferedImage originalImage) {
        ResampleOp resampleOp = new ResampleOp(width, height);
        resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.None);
        return resampleOp.filter(originalImage, null);
    }
}