package id.kedukasi.core.utils;

import javax.servlet.http.HttpServletRequest;

public class PathGeneratorUtil {


    public static String generate(String fileCode, HttpServletRequest request) {

//        String fileType = fileCode.substring(0, 3);
        String hostName = request.getLocalName();
        int port = request.getLocalPort();
        String schema = request.getScheme();

//        boolean isDocument = fileType.equals("DOC");
//        boolean isImage = fileType.equals("IMG");

//        if (isImage) {
//            path = "/downloadFile/image/" + fileCode;
//        } else if (isDocument) {
//            path = "/downloadFile/document/" + fileCode;
//        } else {
//            path = "/downloadFile/other/" + fileCode;
//        }

        return schema + "://" + hostName + ":" + port + "/downloadFile/utility/" + fileCode;
    }

}
