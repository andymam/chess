package ch09.sec02;

import java.nio.file.Path;

public class PathDemo {
    public static void main(String[] args) {
        Path absolute = Path.of("/", "home", "cay");
        Path relative = Path.of("myprog", "conf", "user.properties");
        System.out.println(absolute);
        System.out.println(relative);
        Path homeDirectory = Path.of("/home/cay");
        System.out.println(homeDirectory);
        
        Path workPath = homeDirectory.resolve("myprog/work");
        System.out.println(workPath);
        Path tempPath = workPath.resolveSibling("temp");
        System.out.println(tempPath);
        
        relative = Path.of("/home/cay").relativize(Path.of("/home/fred/myprog"));
        System.out.println(relative);
        
        Path normalized = Path.of("/home/cay/../fred/./myprog").normalize();
        System.out.println("normalized: " + normalized);
        
        absolute = Path.of("config").toAbsolutePath();
        System.out.println("absolute: " + absolute);
        
        Path p = Path.of("/home", "cay", "myprog.properties");
        System.out.println("p: " + p);
        Path parent = p.getParent(); 
        System.out.println("parent of p: " + parent);
        Path file = p.getFileName(); 
        System.out.println("file of p: " + file);
        Path root = p.getRoot(); 
        System.out.println("root of p: " + root);
        Path first = p.getName(0);
        System.out.println("first of p: " + first);
        Path dir = p.subpath(0, p.getNameCount() - 1);
        System.out.println("dir of p: " + dir);
        
        System.out.println("Components of p");
        for (Path component : p) {
            System.out.println(component);
        }
    }
}
