package com.lebo.util;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;
import org.junit.Test;

/**
 * @author: Wei Liu
 * Date: 13-10-7
 * Time: PM7:17
 */
public class VideoUtilsTest {


    @Test
    public void convertVideo2mpeg4() throws Exception {
        VideoUtils.convertVideo2mpeg4("/Users/liuwei/Downloads/5253c70d0cf2afcb04d5d47b-video-621825.mp4", "target/convert.mp4");
    }

    @Test
    public void convertVideo() {
        VideoUtils.convertVideo("http://file.lebooo.com/post/2013-10-08/5253ae270cf21c0e09f49bd6-video-656865.mp4", "target/convert.mp4");
    }

    @Test
    public void printVideoInfo() {
        printVideoInfo("/Users/liuwei/Downloads/5253c70d0cf2afcb04d5d47b-video-621825.mp4", "android不可播放");
        printVideoInfo("/Users/liuwei/Downloads/52542e660cf29782abefb958-video-973351.mp4", "android可播放");
        printVideoInfo("target/convert.mp4", "android可播放");
    }

    @Test
    public void getVideoDuration(){
        long duration = VideoUtils.getVideoDurationInSeconds("/Users/liuwei/lebo/video/2013-07-03_11-30-29.mp4");
        System.out.println(duration);
    }

    private void printVideoInfo(String videoUrl, String message) {
        System.out.printf("\n---- %s ----\n", message);
        System.out.printf("File    : %s\n", videoUrl);
        IMediaReader reader = ToolFactory.makeReader(videoUrl);
        reader.open();

        System.out.printf("fileSize: %s\n", reader.getContainer().getFileSize());
        System.out.printf("duration: %s\n", reader.getContainer().getDuration());
        System.out.printf("bitRate : %s\n", reader.getContainer().getBitRate());
        System.out.printf("format  : %s\n", reader.getContainer().getContainerFormat().getInputFormatLongName());
        System.out.printf("type    : %s\n", reader.getContainer().getType().toString());
        System.out.printf("containerFormat    : %s\n", reader.getContainer().getContainerFormat());
        System.out.printf("flags    : %s\n", reader.getContainer().getFlags());
        System.out.printf("maxDelay    : %s\n", reader.getContainer().getMaxDelay());
        System.out.printf("metaData    : %s\n", reader.getContainer().getMetaData());
        System.out.printf("myCPtr    : %s\n", reader.getContainer().getMyCPtr());
        System.out.printf("inputBufferLength    : %s\n", reader.getContainer().getInputBufferLength());
        System.out.printf("propertyNames    : %s\n", reader.getContainer().getPropertyNames());
        System.out.printf("readRetryCount    : %s\n", reader.getContainer().getReadRetryCount());
        System.out.printf("startTime    : %s\n", reader.getContainer().getStartTime());
        System.out.printf("outputDefaultVideoCodec    : %s\n", reader.getContainer().getContainerFormat().getOutputDefaultVideoCodec());

        reader.close();
    }
}
