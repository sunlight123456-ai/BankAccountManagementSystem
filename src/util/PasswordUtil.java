package src.util;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 密码加密工具类
 */
public class PasswordUtil {
    /**
     * 加密密码
     * @param password
     * @return
     */
    public static String encrypt(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("加密密码失败",e);
        }
    }
    /**
     * 验证密码
     */
    public static boolean verify(String inputPassword,String storedPassword){
        return encrypt(inputPassword).equals(storedPassword);
    }
}
