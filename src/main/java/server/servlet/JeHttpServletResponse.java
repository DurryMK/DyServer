package server.servlet;

import java.io.*;


import java.util.Map;

import server.entity.Cookie;
import server.entity.CookieContainer;
import server.config.ConfigFactory;
import server.config.Constants;
import server.servlet.base.ServletResponse;

public class JeHttpServletResponse implements ServletResponse {
    private OutputStream oos;
    private JeHttpServletRequest request;
    private String contentType;
    private String encode = "utf-8";

    public JeHttpServletResponse(OutputStream oos, JeHttpServletRequest request) {
        this.oos = oos;
        this.request = request;
    }

    //静态资源转发
    public void sendRedirect() {
        //资源路径
        String uri = request.getRequestURI();
        //创建输出文件
        File f = new File(request.getRealPath(), uri);
        if (!f.exists()) {
            //资源不存在 定位至404界面
            outWrite(404, Constants.p404);
        } else {
            //输出资源
            outWrite(200, uri);
        }
    }

    public void outWrite(int code, String filePath) {
        String responseHead = null;// 请求头
        byte[] fileContent = readFile(new File(request.getRealPath(), filePath));// 文件内容
        switch (code) {
            case 200:
                responseHead = to200(fileContent.length);
                break;
            case 500:
                responseHead = to500(fileContent.length);
                break;
            case 404:
                responseHead = to404(fileContent.length);
                break;
        }
        try {
            oos.write(responseHead.getBytes());
            oos.flush();
            oos.write(fileContent);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    private String to500(int length) {
        String t500 = "HTTP/1.1 500 Internal Server Error\r\nContent-Type: text/html;charset=" + this.encode + "\r\nContent-Length:" + length
                + "\r\n\r\n";
        return t500;
    }

    private String to404(int length) {
        String t404 = "HTTP/1.1 404 File Not Found\r\nContent-Type: text/html;charset=" + this.encode + "\r\nContent-Length:" + length + "\r\n\r\n";
        return t404;
    }

    private String to200(int length) {
        String uri = this.request.getRequestURI();
        int index = uri.lastIndexOf(".");
        if (index >= 0) {
            index = index + 1;
        }
        //截取文件路径
        String fileExtension = uri.substring(index);
        //从Constants中获取所有的文件类型
        Map<String, String> types = ConfigFactory.webMapping;
        String t200;
        if (types.get(fileExtension) != null) {
            //找到对应的响应类型
            contentType = types.get(fileExtension);
            t200 = "HTTP/1.1 200 OK\r\nContent-Type: " + contentType + ";charset=" + this.encode + "\r\nContent-Length: " + length
                    + "\r\n" + getLastResponse();
        } else {
            //如果没有找到文件类型则当作text/html输出
            contentType = "text/html";
            t200 = "HTTP/1.1 200 OK\r\nContent-Type: text/html;charset=" + this.encode + "\r\nContent-Length: " + length
                    + "\r\n" + getLastResponse();
        }
        return t200;
    }

    private byte[] readFile(File file) {
        FileInputStream fis = null;
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        try {
            fis = new FileInputStream(file);
            byte[] bs = new byte[1024];
            int length = -1;
            while ((length = fis.read(bs, 0, bs.length)) != -1) {
                boas.write(bs, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return boas.toByteArray();
    }

    public void textWriter(String result) {
        try {
            String responseHead = "HTTP/1.1 200 OK\r\nContent-Type: text/html;charset=" + this.encode + "\r\nContent-Length:" + result.getBytes().length + "\r\n\r\n";
            oos.write(responseHead.getBytes());
            oos.flush();
            oos.write(result.getBytes());
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PrintWriter getWriter() {
        return new PrintWriter(this.oos);
    }

    public String getContentType() {
        return this.contentType;
    }

    public void addCookie(Cookie c) {
        CookieContainer.getInstance().addCookies(c);
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public String getLastResponse() {
        String lastResponse = "";
        Cookie[] cookies = CookieContainer.getInstance().getCookies();
        for (Cookie item : cookies) {
            lastResponse += "Set-Cookie: " + item.getName() + "=" + item.getValue() + "\r\n";
        }
        return lastResponse += "\r\n\r\n";
    }

}
