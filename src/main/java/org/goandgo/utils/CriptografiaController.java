package org.goandgo.utils;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CriptografiaController {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String CHAVE = "12345678901234567890123456789012"; // 32 bytes para AES-256
    private static final String IV = "1234567890123456"; // 16 bytes para o IV
    // Criptografar senha
    public static String criptografar(String texto) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec chaveSecreta = new SecretKeySpec(CHAVE.getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, chaveSecreta, iv);
        byte[] textoCriptografado = cipher.doFinal(texto.getBytes());
        return Base64.getEncoder().encodeToString(textoCriptografado);
    }
}