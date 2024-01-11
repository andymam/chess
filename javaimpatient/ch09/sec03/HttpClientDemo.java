package ch09.sec03;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientDemo {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        System.out.println("GET demo");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://horstmann.com/index.html"))
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        
        System.out.println("\n\n\nPOST demo");        
        client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();     
        // Don't build JSON like I do--use a library!
        var body = """
{ "repo": "ext",
  "problem": "2207191457ckrh81vmbfbcgvbm7rvmmdrc0",
  "Input": "",
  "Main.java": \"""" + escape(prog) + "\"\n}";        		
        request = HttpRequest.newBuilder()
                .uri(new URI("https://codecheck.io/checkNJS"))
                .header("Accept-Charset", "UTF-8")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
    
    // CAUTION: Toy method 
    public static String escape(String s) {
    	return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }    
    
    public static String prog = """
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
""";
}
