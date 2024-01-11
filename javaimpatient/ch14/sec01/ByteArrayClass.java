package ch14.sec01;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class ByteArrayClass extends SimpleJavaFileObject {
    private ByteArrayOutputStream out;

    ByteArrayClass(String name) {
        super(URI.create("bytes:///" + name.replace('.','/') + ".class"),
            Kind.CLASS);
    }
    
    public byte[] getCode() {
        return out.toByteArray();
    }
    
    public OutputStream openOutputStream() throws IOException {
        return new ByteArrayOutputStream();
    }        
}
