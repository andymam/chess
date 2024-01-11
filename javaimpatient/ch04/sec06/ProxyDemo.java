package ch04.sec06;


import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class ProxyDemo {
    public static void main(String[] args) {
        var values = new Object[1000];
        
        for (int i = 0; i < values.length; i++) {
            Object value = Integer.valueOf(i);
            values[i] = Proxy.newProxyInstance(
                null, value.getClass().getInterfaces(), 
                (Object proxy, Method m, Object[] margs) -> {
                    System.out.println(value + "." + m.getName() + Arrays.toString(margs));
                    return m.invoke(value, margs);
                });
        }
        
        int position = Arrays.binarySearch(values, Integer.valueOf(500));
        System.out.println(values[position]);
    }
}
