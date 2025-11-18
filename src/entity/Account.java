package src.entity;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * 账户实体类
 */
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private String accountId;
    private String password;
    private String ownerName;
    private double balance;
    private List<Transaction> transactions;
    public Account(){
        this.transactions=new ArrayList<>();
    }
    public Account(String accountId,String password,String ownerName,double balance){
        this.accountId=accountId;
        this.password=password;
        this.ownerName=ownerName;
        this.balance=balance;
        this.transactions=new ArrayList<>();
    }

    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password=password;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    /**
     * 添加交易记录
     */
    public void addTransaction(Transaction transaction){
        this.transactions.add(transaction);
    }
    @Override
    public String toString(){
        return String.format("账户ID: %s， 户主：%s， 余额： %.2f",accountId,ownerName,balance);
    }

    /**
     * 用于文件存储的格式
     */
    public String toFileString(){
        return String.format("%s，%s，%s，%.2f",accountId,password,ownerName,balance);
    }
    /**
     * 从字符串创建账户对象
     */
    public static Account fromFileString(String fileString){
        String[] parts=fileString.split("，");
        if(parts.length!=4){
            throw new IllegalArgumentException("无效的账户数据格式: "+fileString);
        }
        return new Account(
                parts[0],
                parts[1],
                parts[2],
                Double.parseDouble(parts[3])
        );
    }
}
