package ch04.sec05.impl;

import ch04.sec05.Cipher;

public class AESCipherFactory {
    public static Cipher provider() { return new AESCipher(); }
}
