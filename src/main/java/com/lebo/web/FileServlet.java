package com.lebo.web;

import com.lebo.entity.Post;
import com.lebo.service.FileStorageService;
import com.lebo.service.GridFsService;
import com.lebo.service.StatusService;
import com.lebo.service.account.AccountService;
import com.lebo.entity.FileInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 本地静态内容展示与下载的Servlet.
 * <p/>
 * 演示文件高效读取,客户端缓存控制及Gzip压缩传输.
 * 可使用org.springside.examples.showcase.cache包下的Ehcache或本地Map缓存静态内容基本信息(未演示).
 * <p/>
 * 演示访问地址为：
 * files/51da628f1a8899f9665a6778
 * files/51da628f1a8899f9665a6778?download=true
 *
 * @author Wei Liu
 */
public class FileServlet extends HttpServlet {

    private static final long serialVersionUID = -1855617048198368534L;

    private Logger logger = LoggerFactory.getLogger(FileServlet.class);

    public static String POST_ID_KEY = "postId";

    /**
     * 需要被Gzip压缩的Mime类型.
     */
    private static final String[] GZIP_MIME_TYPES = {"text/html", "application/xhtml+xml", "text/plain", "text/css",
            "text/javascript", "application/x-javascript", "application/json"};

    /**
     * 需要被Gzip压缩的最小文件大小.
     */
    private static final int GZIP_MINI_LENGTH = 512;

    private ApplicationContext applicationContext;

    private FileStorageService fileStorageService;
    private AccountService accountService;
    private StatusService statusService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //取得参数
        Assert.isTrue(request.getPathInfo().length() > 1, "Incorrect Request Path");

        String fileId = request.getPathInfo().substring(1);
        String postId = request.getParameter(POST_ID_KEY);

        //获取请求内容的基本信息.
        FileInfo contentInfo;
        try {
            contentInfo = fileStorageService.get(fileId);
        } catch (Exception e) {
            logger.info("从MongoDB读取文件失败 - fileId : " + fileId + " - " + e.getMessage());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //根据Etag或ModifiedSince Header判断客户端的缓存文件是否有效, 如仍有效则设置返回码为304,直接返回.
        if (!Servlets.checkIfModifiedSince(request, response, contentInfo.getLastModified())
                || !Servlets.checkIfNoneMatchEtag(request, response, contentInfo.geteTag())) {
            return;
        }

        //设置Etag/过期时间
        Servlets.setExpiresHeader(response, Servlets.ONE_YEAR_SECONDS);
        Servlets.setLastModifiedHeader(response, contentInfo.getLastModified());
        Servlets.setEtag(response, contentInfo.geteTag());

        //设置MIME类型
        response.setContentType(contentInfo.getContentType());

        //设置弹出下载文件请求窗口的Header
        if (request.getParameter("download") != null) {
            Servlets.setFileDownloadHeader(response, contentInfo.getFilename());
        }

        boolean needGzip;
        if (contentInfo.getLength() >= GZIP_MINI_LENGTH && ArrayUtils.contains(GZIP_MIME_TYPES, contentInfo.getContentType())) {
            needGzip = true;
        } else {
            needGzip = false;
        }

        //构造OutputStream
        OutputStream output;
        if (checkAccetptGzip(request) && needGzip) {
            //使用压缩传输的outputstream, 使用http1.1 trunked编码不设置content-length.
            output = buildGzipOutputStream(response);
        } else {
            //使用普通outputstream, 设置content-length.
            response.setContentLength(((Long) contentInfo.getLength()).intValue());
            output = response.getOutputStream();
        }

        //高效读取文件内容并输出,然后关闭input file
        IOUtils.copy(contentInfo.getContent(), output);
        output.flush();
        IOUtils.closeQuietly(contentInfo.getContent());

        //增长用户视频被播放次数
        if (StringUtils.startsWith(contentInfo.getContentType(), "video/") && postId != null) {
            Post post = statusService.getPost(postId);
            if (post != null && post.getFiles().contains(fileId)) {
                statusService.increaseViewCount(postId);
                accountService.increasePlaysCount(post.getUserId());
            }
        }
    }

    /**
     * 检查浏览器客户端是否支持gzip编码.
     */
    private static boolean checkAccetptGzip(HttpServletRequest request) {
        //Http1.1 header
        String acceptEncoding = request.getHeader("Accept-Encoding");

        return StringUtils.contains(acceptEncoding, "gzip");
    }

    /**
     * 设置Gzip Header并返回GZIPOutputStream.
     */
    private OutputStream buildGzipOutputStream(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Encoding", "gzip");
        response.setHeader("Vary", "Accept-Encoding");
        return new GZIPOutputStream(response.getOutputStream());
    }

    /**
     * 初始化.
     */
    @Override
    public void init() throws ServletException {
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());

        fileStorageService = applicationContext.getBean(GridFsService.class);
        accountService = applicationContext.getBean(AccountService.class);
        statusService = applicationContext.getBean(StatusService.class);
    }
}
