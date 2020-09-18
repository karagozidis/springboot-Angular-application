package crm.cloudApp.backend.utils.encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RsaEncryptionUtil {

  protected static final String ALGORITHM = "RSA";
  private PublicKey publicKey;

  public RsaEncryptionUtil() {
    init();
  }

  public static String generatePublicKeyString() throws NoSuchAlgorithmException {
    PublicKey publicKey = generatePublicKey();
    return java.util.Base64.getEncoder().encodeToString(publicKey.getEncoded());
  }


  public static PublicKey generatePublicKey() throws NoSuchAlgorithmException {
    KeyPair keyPair = generateKey();
    return keyPair.getPublic();

  }

  public static KeyPair generateKey() throws NoSuchAlgorithmException {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
    keyGen.initialize(2048);
    return keyGen.generateKeyPair();
  }


  public void setKey(String encodedKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
    PublicKey curentPublicKey = getPublicKeyFromString(encodedKey);
    this.publicKey = curentPublicKey;
  }

  /**
   * Init java security to add BouncyCastle as an RSA provider
   */
  public static void init() {
    Security.addProvider(new BouncyCastleProvider());
  }


  /**
   * Encrypt a text using public key.
   *
   * @param text The original unencrypted text
   * @return Encrypted text
   */
  public byte[] encrypt(byte[] text)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    byte[] cipherText = null;
    //
    // get an RSA cipher object and print the provider
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

    // encrypt the plaintext using the public key
    cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
    cipherText = cipher.doFinal(text);
    return cipherText;
  }

  /**
   * Encrypt a text using public key. The result is enctypted BASE64 encoded text
   *
   * @param text The original unencrypted text
   * @return Encrypted text encoded as BASE64
   */
  public String encrypt(String text)
      throws UnsupportedEncodingException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
    String encryptedText;
    byte[] cipherText = encrypt(text.getBytes("UTF8"));
    encryptedText = encodeBASE64(cipherText);
    return encryptedText;
  }

  /**
   * Decrypt text using private key
   *
   * @param text The encrypted text
   * @param key The private key
   * @return The unencrypted text
   */
  public byte[] decrypt(byte[] text, PrivateKey key)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    byte[] dectyptedText = null;
    // decrypt the text using the private key
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.DECRYPT_MODE, key);
    dectyptedText = cipher.doFinal(text);
    return dectyptedText;

  }

  /**
   * Decrypt BASE64 encoded text using private key
   *
   * @param text The encrypted text, encoded as BASE64
   * @param key The private key
   * @return The unencrypted text encoded as UTF8
   */
  public String decrypt(String text, PrivateKey key)
      throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
    String result;
    // decrypt the text using the private key
    byte[] dectyptedText = decrypt(decodeBASE64(text), key);
    result = new String(dectyptedText, "UTF8");
    return result;

  }

  /**
   * Convert a Key to string encoded as BASE64
   *
   * @param key The key (private or public)
   * @return A string representation of the key
   */
  public static String getKeyAsString(Key key) {
    // Get the bytes of the key
    byte[] keyBytes = key.getEncoded();
    return encodeBASE64(keyBytes);
  }

  /**
   * Generates Private Key from BASE64 encoded string
   *
   * @param key BASE64 encoded string which represents the key
   * @return The PrivateKey
   */
  public static PrivateKey getPrivateKeyFromString(String key)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
    EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decodeBASE64(key));
    return keyFactory.generatePrivate(privateKeySpec);
  }

  /**
   * Generates Public Key from BASE64 encoded string
   *
   * @param key BASE64 encoded string which represents the key
   * @return The PublicKey
   */
  public static PublicKey getPublicKeyFromString(String key)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
    EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decodeBASE64(key));
    return keyFactory.generatePublic(publicKeySpec);
  }

  /**
   * Encode bytes array to BASE64 string
   *
   * @return Encoded string
   */
  private static String encodeBASE64(byte[] bytes) {
    return Base64.encodeBase64String(bytes);
  }

  /**
   * Decode BASE64 encoded string to bytes array
   *
   * @param text The string
   * @return Bytes array
   */
  private static byte[] decodeBASE64(String text) {
    return Base64.decodeBase64(text);
  }

  public static byte[] copyBytes(byte[] arr, int length) {
    byte[] newArr = null;
    if (arr.length == length) {
      newArr = arr;
    } else {
      newArr = new byte[length];
      for (int i = 0; i < length; i++) {
        newArr[i] = arr[i];
      }
    }
    return newArr;
  }


}
