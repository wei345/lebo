package com.lebo.util;

import org.junit.Test;

/**
 * @author: Wei Liu
 * Date: 13-10-7
 * Time: PM7:17
 */
public class VideoUtilsTest {


    @Test
    public void convert2mp4() throws Exception {
        VideoUtils.convertVideo("http://file.lebooo.com/post/2013-10-08/5253ae270cf21c0e09f49bd6-video-656865.mp4", "target/convert.mp4");
    }
}
