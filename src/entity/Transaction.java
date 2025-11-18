package src.entity;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 交易记录实体类
 */
public class Transaction {
    public static final long serialVersionUID=1L;

    private String transactionId;    //交易ID
    private String fromAccountId;   //转出金额
    private String toAccountId;     //转入金额
    private double amount;      //交易金额
    private String type;       //交易类型：DEPOSIT,WITHDRAW,TRANSFER
    private LocalDateTime timestamp; //交易时间
    private String description;      //描述

    public Transaction(){}

    public Transaction(String transactionId, String fromAccountId, String toAccountId, double amount, String type, String description) {
        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.type = type;
        this.timestamp=LocalDateTime.now();
        this.description = description;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("交易ID：%s，类型：%s，金额：%.2f，时间: %s,描述：%s",transactionId,type,amount,timestamp.format(formatter),description);
    }
    public String toFileString(){
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("%s，%s，%s，%.2f，%s，%s，%s",transactionId,fromAccountId,toAccountId,amount,type,timestamp.format(formatter),description);
    }

    public static Transaction fromFileString(String fileString){
        String[] parts=fileString.split("，");
        if(parts.length!=7){
            throw new IllegalArgumentException("无效的交易数据格式: "+ fileString);
        }
        Transaction transaction = new Transaction(
              parts[0],
              parts[1],
              parts[2],
              Double.parseDouble(parts[3]),
              parts[4],
              parts[6]
        );
        //设置时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        transaction.setTimestamp(LocalDateTime.parse(parts[5],formatter));
        return transaction;
    }
}
