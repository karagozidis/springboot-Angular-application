package crm.cloudApp.backend.utils.encryption;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

@Slf4j
public class AesEncryptionUtil {

    private Cipher cipherEncryption;
    private Cipher cipherDecryption;

    //  public AesEncryptionUtil() {
    //  }

    public static String generateStringKey(String uuid)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        SecretKey secretKey = generateKey(uuid);
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static SecretKey stringToKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public static SecretKey generateKey(String uuid)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] randomSalt = new byte[100];
        new Random().nextBytes(randomSalt);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(uuid.toCharArray(), randomSalt, 65536, 256);
        return factory.generateSecret(spec);
    }

    public void setKey(String encodedKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKey secretKey = stringToKey(encodedKey);
        this.setKey(secretKey);
    }

    public void setKey(SecretKey secretKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
        cipherEncryption = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipherEncryption.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        cipherDecryption = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipherDecryption.init(Cipher.DECRYPT_MODE, secretKeySpec);
    }

    public byte[] encrypt(byte[] dataToEncrypt) {
        try {
            return cipherEncryption.doFinal(dataToEncrypt);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new byte[0];
    }

    public void encrypt(ByteBuffer dataToEncrypt, ByteBuffer encryptedData) {
        try {
            cipherEncryption.doFinal(dataToEncrypt, encryptedData);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }


    public byte[] decrypt(byte[] dataToDecrypt) {
        try {
            return cipherDecryption.doFinal(dataToDecrypt);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new byte[0];
    }

    public void decrypt(ByteBuffer encryptedData, ByteBuffer decryptedData) {
        try {
            cipherDecryption.doFinal(encryptedData, decryptedData);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }


}
