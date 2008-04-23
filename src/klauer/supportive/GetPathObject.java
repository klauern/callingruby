/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package klauer.supportive;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;

/**
 * This source was stolen directly from the Java Support Forums.  Since it was
 * published in a semi-public domain, I'm not sure if there are any copyright
 * restrictions.  In any case, the soure is:
 * 
 * @see http://forum.java.sun.com/thread.jspa?threadID=328939&messageID=1363760
 * 
 * @author amishslayer
 */
public class GetPathObject {

    public static String getPathForObject(Object obj) {
        URL url = getURLForObject(obj);
        if (url.getProtocol().equals("jar")) {
            try {
                JarURLConnection jarCon = (JarURLConnection) url.openConnection();
                url = jarCon.getJarFileURL();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            File file = new File(URLDecoder.decode(url.getPath(), "UTF-8"));
            if (file.isFile()) {
                return file.getParent();
            }
            return file.getPath();
        } catch (UnsupportedEncodingException e) {
            System.out.println("Urldecoding error: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    private static URL getURLForObject(Object obj) {
        String name = obj.getClass().getName();
        int index = name.lastIndexOf('.');
        name = new String(name.substring(index + 1) + ".class");
        return obj.getClass().getResource(name);
    }
}
