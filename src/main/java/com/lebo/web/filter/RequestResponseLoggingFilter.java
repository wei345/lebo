package com.lebo.web.filter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * 记录日志：requestUrl, requestBody, request Cookie, responseText, response Cookie
 *
 * @author: Wei Liu
 * Date: 13-8-5
 * Time: PM12:31
 */
public class RequestResponseLoggingFilter extends AbstractRequestLoggingFilter {
    private Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);
    private int maxLoggingContentLength = 500; //为了显示较完整错误信息

    /**
     * response日志。
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response);

        //直接输出参数、或用反射替换request inputStream 属性
        //代理request，为了输出body
        //HttpServletRequest requestProxy =  createJdkProxy(request);
//        HttpServletRequest requestProxy = createJavassistDynamicProxy(request);

        super.doFilterInternal(request, responseWrapper, filterChain);

        try {
            responseWrapper.flushBuffer();
        } finally {
            logger.debug("{} {}", responseWrapper.statusCode, responseWrapper.statusMessage);
            if (responseWrapper.headers.size() > 0) {
                //header不是全部，不包含JSESSIONID
                for (String key : responseWrapper.headers.keySet()) {
                    for (String v : responseWrapper.headers.get(key)) {
                        logger.debug("       response header : {} = {}", key, v);
                    }
                }
            } else {
                logger.debug("       response header : 无");
            }

            byte[] copy = responseWrapper.getCopy();
            String responseText = new String(copy, response.getCharacterEncoding());
            logger.debug("  response contentType : {}", responseWrapper.getContentType());
            //响应文本
            if (StringUtils.indexOfAny(response.getContentType(), "json", "text") != -1) {
                logger.debug("  response body length : {} characters", FileUtils.byteCountToDisplaySize(responseText.length()));
                if (responseText.length() > 0) {
                    String trimmed = responseText.trim();
                    String text = StringUtils.substring(trimmed, 0, maxLoggingContentLength) + (trimmed.length() > maxLoggingContentLength ? "..." : "");
                    logger.debug("      response content{} : {}", (responseText.equals(text) ? "" : "(trimmed)"), text);
                }
            }
            //响应二进制
            else {
                logger.debug("  response body length : {}", FileUtils.byteCountToDisplaySize(responseWrapper.getCopy().length));
            }
        }
    }

    /**
     * request日志。
     */
    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        logger.debug("{} {}", request.getMethod(), request.getRequestURL().toString());

        //request查询字符串参数和post的表单参数
        Map parameterMap = request.getParameterMap();
        for (Object name : parameterMap.keySet()) {
            logger.debug("     request parameter : {} = {}", name, parameterMap.get(name));
        }
        if (parameterMap.size() == 0) {
            logger.debug("     request parameter : 无");
        }

        //request headers
        Enumeration names = request.getHeaderNames();
        while (names.hasMoreElements()){
            String name = names.nextElement().toString();
            Enumeration values = request.getHeaders(name);
            while (values.hasMoreElements()){
                logger.debug("        request header : {} = {}", name, values.nextElement());
            }
        }

        //request cookies
        if (request.getCookies() == null || request.getCookies().length == 0) {
            logger.debug("        request Cookie : 无");
        } else {
            for (Cookie cookie : request.getCookies()) {
                logger.debug("        request Cookie : {} = {}", cookie.getName(), cookie.getValue());
            }
        }
    }

    //TODO 在接口内记录非form url encode的请求参数，用于与客户端调试
    //记录request太困难了
    //logging request body
    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        /*try {
            if (request.getInputStream() instanceof RequestCachingInputStream) {
                logger.debug("request body: {}", new String(((RequestCachingInputStream) request.getInputStream()).toByteArray(),
                        request.getCharacterEncoding()));
            }
        } catch (IOException e) {
            logger.debug("打印request body日志时出错", e);
        }*/
    }
    /*
    private HttpServletRequest createJdkProxy(final HttpServletRequest delegate) {
        return Reflection.newProxy(HttpServletRequest.class, new AbstractInvocationHandler() {
            private ServletInputStream inputStream;

            @Override
            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getName().equals("getInputStream")) {
                    if (inputStream == null) {
                        inputStream = new RequestCachingInputStream((ServletInputStream) method.invoke(delegate, args));
                    }
                    return inputStream;
                } else {
                    return method.invoke(delegate, args);
                }
            }
        });
    }

    private HttpServletRequest createJavassistBytecodeDynamicProxy(HttpServletRequest delegate) throws Exception {
        ClassPool mPool = new ClassPool(true);
        CtClass mCtc = mPool.makeClass(HttpServletRequest.class.getName() + "JavaassistProxy");
        mCtc.addInterface(mPool.get(HttpServletRequest.class.getName()));
        mCtc.addConstructor(CtNewConstructor.defaultConstructor(mCtc));
        mCtc.addField(CtField.make("public " + HttpServletRequest.class.getName() + " delegate;", mCtc));
        mCtc.addMethod(CtNewMethod.make("public int count() { return delegate.count(); }", mCtc));
        Class pc = mCtc.toClass();
        HttpServletRequest bytecodeProxy = (HttpServletRequest) pc.newInstance();
        Field filed = bytecodeProxy.getClass().getField("delegate");
        filed.set(bytecodeProxy, delegate);
        return bytecodeProxy;
    }

    private HttpServletRequest createJavassistDynamicProxy(final HttpServletRequest delegate) {
        try {
            ProxyFactory proxyFactory = new ProxyFactory();
            proxyFactory.setInterfaces(new Class[]{HttpServletRequest.class});
            Class proxyClass = proxyFactory.createClass();
            HttpServletRequest javassistProxy = (HttpServletRequest) proxyClass.newInstance();
            ((ProxyObject) javassistProxy).setHandler(new MethodHandler() {
                private ServletInputStream inputStream;

                public Object invoke(Object self, Method method, Method proceed,
                                     Object[] args) throws Throwable {
                    if (method.getName().equals("getInputStream")) {
                        if (inputStream == null) {
                            inputStream = new RequestCachingInputStream((ServletInputStream) method.invoke(delegate, args));
                        }
                        return inputStream;
                    } else {
                        return method.invoke(delegate, args);
                    }
                }
            });
            return javassistProxy;
        } catch (Exception e) {
            throw new RuntimeException("创建javassist动态代理时发生异常", e);
        }
    }

    class RequestCachingInputStream extends ServletInputStream {
        private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

        private final ServletInputStream is;

        private RequestCachingInputStream(ServletInputStream is) {
            this.is = is;
        }

        @Override
        public int read() throws IOException {
            int ch = is.read();
            if (ch != -1) {
                bos.write(ch);
            }
            return ch;
        }

        private byte[] toByteArray() {
            return this.bos.toByteArray();
        }
    }*/

    /**
     * Logging http body and headers.
     */
    private static class HttpServletResponseWrapper extends javax.servlet.http.HttpServletResponseWrapper {

        private ServletOutputStream outputStream;
        private PrintWriter writer;
        private ServletOutputStreamWrapper wrapper;
        final Map<String, List<String>> headers = new HashMap<String, List<String>>();
        private String statusCode = "200[default]";
        private String statusMessage = "";

        public HttpServletResponseWrapper(HttpServletResponse response) throws IOException {
            super(response);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            if (writer != null) {
                throw new IllegalStateException("getWriter() has already been called on this response.");
            }

            if (outputStream == null) {
                outputStream = getResponse().getOutputStream();
                wrapper = new ServletOutputStreamWrapper(outputStream);
            }

            return wrapper;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            if (outputStream != null) {
                throw new IllegalStateException("getOutputStream() has already been called on this response.");
            }

            if (writer == null) {
                wrapper = new ServletOutputStreamWrapper(getResponse().getOutputStream());
                writer = new PrintWriter(new OutputStreamWriter(wrapper, getResponse().getCharacterEncoding()), true);
            }

            return writer;
        }

        @Override
        public void flushBuffer() throws IOException {
            if (writer != null) {
                writer.flush();
            } else if (outputStream != null) {
                wrapper.flush();
            }
        }

        public byte[] getCopy() {
            if (wrapper != null) {
                return wrapper.getCopy();
            } else {
                return new byte[0];
            }
        }

        @Override
        public void setHeader(String name, String value) {
            List<String> values = new ArrayList<String>();
            values.add(value);
            headers.put(name, values);
            super.setHeader(name, value);
        }

        @Override
        public void addHeader(String name, String value) {
            List<String> values = headers.get(name);
            if (values == null) {
                values = new ArrayList<String>();
                headers.put(name, values);
            }
            values.add(value);
            super.addHeader(name, value);
        }

        @Override
        public void setStatus(int sc) {
            super.setStatus(sc);
            this.statusCode = Integer.toString(sc);
        }

        @Override
        public void setStatus(int sc, String sm) {
            super.setStatus(sc, sm);
            this.statusCode = Integer.toString(sc);
            this.statusMessage = sm;
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            super.sendError(sc, msg);
            this.statusCode = Integer.toString(sc);
            this.statusMessage = msg;
        }

        @Override
        public void sendError(int sc) throws IOException {
            super.sendError(sc);
            this.statusCode = Integer.toString(sc);
        }

        @Override
        public void sendRedirect(String location) throws IOException {
            super.sendRedirect(location);
            this.statusCode = "302";
            this.headers.put("Location", Arrays.asList(location));
        }

        static class ServletOutputStreamWrapper extends ServletOutputStream {

            private OutputStream outputStream;
            private ByteArrayOutputStream copy;

            public ServletOutputStreamWrapper(OutputStream outputStream) {
                this.outputStream = outputStream;
                this.copy = new ByteArrayOutputStream(1024);
            }

            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
                copy.write(b);
            }

            public byte[] getCopy() {
                return copy.toByteArray();
            }
        }
    }
}
