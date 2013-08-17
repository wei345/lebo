package com.lebo.util;

import com.lebo.SpringContextTestCase;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author: Wei Liu
 * Date: 13-8-14
 * Time: PM1:47
 */
public class ImageUtilsTest extends SpringContextTestCase {

    @Test
    public void resizeImageWithHint() throws IOException {
        String baseLocation = "classpath:" + ImageUtilsTest.class.getName().replace(".", "/");
        BufferedImage originalImage = ImageIO.read(ResourceUtils.getFile(baseLocation + "-0.jpeg"));

        String basePath = StringUtils.substringBeforeLast(ResourceUtils.getFile(baseLocation + "-0.jpeg").getAbsolutePath(), ".");

        BufferedImage resizeImageJpg = ImageUtils.resizeImage(48, originalImage);
        ImageIO.write(resizeImageJpg, "jpg", new File(basePath + "-48.jpg"));

        BufferedImage resizeImagePng = ImageUtils.resizeImage(73, originalImage);
        ImageIO.write(resizeImagePng, "png", new File(basePath + "-73.png"));
    }
}
