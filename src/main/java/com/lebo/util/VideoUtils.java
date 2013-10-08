package com.lebo.util;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author: Wei Liu
 * Date: 13-5-24
 * Time: PM7:54
 */
public class VideoUtils {
    private static Logger logger = LoggerFactory.getLogger(VideoUtils.class);

    //-- 获取视频首帧图 --//
    public static final double SECONDS_BETWEEN_FRAMES = 10;

    public static final long MICRO_SECONDS_BETWEEN_FRAMES =
            (long) (Global.DEFAULT_PTS_PER_SECOND * SECONDS_BETWEEN_FRAMES);

    public static void writeVideoFirstFrame(File video, File firstFrame) {
        IMediaReader mediaReader = ToolFactory.makeReader(video.getAbsolutePath());
        // stipulate that we want BufferedImages created in BGR 24bit color space
        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
        mediaReader.addListener(new ImageSnapListener(firstFrame));
        // read out the contents of the media file and
        // dispatch events to the attached listener
        try {
            while (mediaReader.readPacket() == null) ;
        } catch (Done e) {
            logger.info("获取视频第一帧图片完成");
        }

    }

    private static class ImageSnapListener extends MediaListenerAdapter {
        private File outputFile;
        // The video stream index, used to ensure we display frames from one and
        // only one video stream from the media container.
        private int mVideoStreamIndex = -1;
        // Time of last frame write
        private long mLastPtsWrite = Global.NO_PTS;

        ImageSnapListener(File outputFile) {
            this.outputFile = outputFile;
        }

        public void onVideoPicture(IVideoPictureEvent event) {
            if (event.getStreamIndex() != mVideoStreamIndex) {
                // if the selected video stream id is not yet set, go ahead an
                // select this lucky video stream
                if (mVideoStreamIndex == -1)
                    mVideoStreamIndex = event.getStreamIndex();
                    // no need to show frames from this video stream
                else
                    return;
            }

            // if uninitialized, back date mLastPtsWrite to get the very first frame
            if (mLastPtsWrite == Global.NO_PTS)
                mLastPtsWrite = event.getTimeStamp() - MICRO_SECONDS_BETWEEN_FRAMES;
            // if it's time to write the next frame
            if (event.getTimeStamp() - mLastPtsWrite >=
                    MICRO_SECONDS_BETWEEN_FRAMES) {
                String outputFilename = dumpImageToFile(event.getImage());

                // indicate file written
                double seconds = ((double) event.getTimeStamp()) /
                        Global.DEFAULT_PTS_PER_SECOND;

                logger.info(
                        "at elapsed time of {} seconds wrote: {}\n",
                        seconds, outputFilename);

                // update last write time
                mLastPtsWrite += MICRO_SECONDS_BETWEEN_FRAMES;

                // 只截取一张图
                throw new Done();
            }
        }


        private String dumpImageToFile(BufferedImage image) {
            try {
                if (!outputFile.getParentFile().exists()) {
                    outputFile.getParentFile().mkdirs();
                }
                ImageIO.write(image, FilenameUtils.getExtension(outputFile.getName()), outputFile);

                return outputFile.getAbsolutePath();
            } catch (IOException e) {
                logger.warn("从视频中提取图片，写出文件时发生异常", e);
                return null;
            }
        }
    }

    static class Done extends RuntimeException {

    }

    //-- 视频转码 --//

    /**
     * 解决ios客户端发布的视频android无法播放的问题
     */
    public static void convertVideo(String url, String target) {
        long begin = System.currentTimeMillis();
        logger.debug("转换视频 : 开始 {} -> {}", url, target);
        IMediaReader reader = ToolFactory.makeReader(url);
        reader.addListener(ToolFactory.makeWriter(target, reader));
        while (reader.readPacket() == null) ;
        logger.debug("转换视频 : 结束 用时 : {} ms", System.currentTimeMillis() - begin);
    }

    public static void main(String[] args) {
        writeVideoFirstFrame(new File("/Users/liuwei/Movies/刘伟-2012.mov"), new File("/Users/liuwei/Movies/刘伟-2012.jpg"));
    }
}