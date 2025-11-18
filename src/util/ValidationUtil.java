package src.util;
import java.util.regex.Pattern;

/**
 * 数据验证工具类
 */
public class ValidationUtil {
    //账户ID格式：数字，长度6-12位
    private static final Pattern ACCOUNT_ID_PATTERN = Pattern.compile("^\\d{6,12}$");
    //密码格式：至少6位，包含字母和数字
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$");
    //户主姓名格式:中文或英文，长度2-20位
    private static final Pattern OWNER_NAME_PATTERN= Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z]{2,20}$");
    private static final Pattern NAME_PATTERN=Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z]{2,20}$");
    public static boolean isValidAccountId(String accountId){
        return accountId !=null &&  ACCOUNT_ID_PATTERN.matcher(accountId).matches();
    }
    public static boolean isValidPassword(String password){
        return password !=null && PASSWORD_PATTERN.matcher(password).matches();
    }
    public static boolean isValidOwnerName(String ownerName){
        return ownerName !=null && OWNER_NAME_PATTERN.matcher(ownerName).matches();
    }

    public static boolean isNumeric(String str){
        if (str==null) return false;
        try{
            Double.parseDouble(str);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
    public static boolean isValidName(String name){
        return name!=null && NAME_PATTERN.matcher(name).matches();
    }
    public static boolean isValidAge(int age){
        return age>=16 && age<=60;
    }

}
