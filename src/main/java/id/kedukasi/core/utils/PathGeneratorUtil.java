package id.kedukasi.core.utils;

import javax.servlet.http.HttpServletRequest;

public class PathGeneratorUtil {


    public static String generate(String fileCode, HttpServletRequest request) {

        String hostName = request.getServerName();
        int port = request.getServerPort();
        String schema = request.getScheme();

        return schema + "://" + hostName + ":" + port + "/downloadFile/utility/" + fileCode;
    }

}
