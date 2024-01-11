package ch09.sec03;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDemo {
    public static void main(String[] args) throws IOException {
        var url = new URL("https://codecheck.io/check");
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setDoOutput(true);
        try (var out = new OutputStreamWriter(
                connection.getOutputStream(), "UTF-8")) {
            var postData = new HashMap<String, String>();
            
            postData.put("repo", "ext");
            postData.put("problem", "2207191457ckrh81vmbfbcgvbm7rvmmdrc0");
            postData.put("Main.java", prog);
            postData.put("Input", "");
            boolean first = true;
            for (Map.Entry<String, String> entry : postData.entrySet()) {
                if (first) first = false;
                else out.write("&");
                out.write(URLEncoder.encode(entry.getKey(), "UTF-8"));
                out.write("=");
                out.write(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        }
        
        Map<String, List<String>> headers = connection.getHeaderFields();
        System.out.println("Response headers: " + headers);
        try (InputStream in = connection.getInputStream()) {
            var contents = new String(readAllBytes(in));
            System.out.println(contents);
        }
    }
    
    public static byte[] readAllBytes(InputStream in) throws IOException {
        var out = new ByteArrayOutputStream();
        copy(in, out);
        return out.toByteArray();
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        final int BLOCKSIZE = 1024;
        byte[] bytes = new byte[BLOCKSIZE];
        int len;
        while ((len = in.read(bytes)) != -1) out.write(bytes, 0, len);
        in.close();
        out.close();
    }
    
    public static String prog = """
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
""";
}
