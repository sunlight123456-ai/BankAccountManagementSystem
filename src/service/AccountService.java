package src.service;
import src.dao.AccountDao;
import src.entity.Account;
import src.entity.Transaction;
import src.util.PasswordUtil;
import src.util.ValidationUtil;
import src.util.FileUtil;

import java.util.List;
import java.util.UUID;

/**
 * 业务逻辑层 - 处理银行账户的业务逻辑
 */
public class AccountService {
    private AccountDao accountDao;

    public AccountService(){
        this.accountDao= new AccountDao();
    }
    /**
     * 生成交易ID
     */
    private String generateTransactionId() {
        return "T" + UUID.randomUUID().toString().replace("-","").substring(0,15).toUpperCase();
    }
    /**
     * 用户注册
     */
    public boolean register(String accountId,String password,String ownerName,double initialDeposit) {
        //数据验证
        if(!ValidationUtil.isValidAccountId(accountId)){
            System.out.println("账户ID必须是6-12位数字！ ");
            return false;
        }

        if(!ValidationUtil.isValidPassword(password)){
            System.out.println("密码必须至少6位，且包含字母和数字！ ");
            return false;
        }
        if(!ValidationUtil.isValidOwnerName(ownerName)){
            System.out.println("户主姓名格式不正确！ ");
            return false;
        }
        if(initialDeposit < 0){
            System.out.println("初始存款不能为负数！ ");
            return false;
        }
        //加密密码
        String encryptedPassword = PasswordUtil.encrypt(password);
        Account account = new Account(accountId,encryptedPassword,ownerName,initialDeposit);
        boolean success = accountDao.addAccount(account);

        if (success && initialDeposit >0) {
            String transactionId = generateTransactionId();
            Transaction transaction = new Transaction(
                    transactionId,
                    null,
                    accountId,
                    initialDeposit,
                    "DEPOSIT",
                    "初始存款"
            );
            accountDao.addTransaction(transaction);
            System.out.println("注册成功！初始存款已到账。");
        } else if (success) {
            System.out.println("注册成功！ ");
        }
        return success;
    }
    /**
     * 用户登录
     */
    public Account login(String accountId, String password) {
        Account account = accountDao.finaAccountById(accountId);
        if (account != null){
            System.out.println("用户不存在！");
            return null;
        }
        if (!PasswordUtil.verify(password,account.getPassword())){
            System.out.println("密码错误！");
            return null;
        }
        System.out.println("登陆成功！ 欢迎"+account.getOwnerName());
        return account;
    }
    public boolean deposit (String accountId,double amount) {
        if (amount <=0){
            System.out.println("存款金额必须大于0！");
            return false;
        }
        Account account= accountDao.finaAccountById(accountId);
        if (account == null){
            System.out.println("账户不存在! ");
            return false;
        }
        //更新余额
        account.setBalance(account.getBalance()+amount);
        boolean success = accountDao.updateAccount(account);
        if(success) {
            //记录存款交易
            String transactionId = generateTransactionId();
            Transaction transaction =new Transaction(
                    transactionId,
                    null,
                    accountId,
                    amount,
                    "DEPOSIT",
                    "现金存款"
            );
            accountDao.addTransaction(transaction);
            System.out.printf("存款成功！当前余额：%.2f\n",account.getBalance());
        }
        return success;
    }
    /**
     * 取款
     */
    public boolean withdraw (String accountId,double amount) {
        if (amount<0) {
            System.out.println("取款金额必须大于0！");
            return false;
        }
        Account account = accountDao.finaAccountById(accountId);
        if (account == null){
            System.out.println("账户不存在！ ");
            return false;
        }
        if (account.getBalance()<amount){
            System.out.println("余额不足！");
            return false;
        }
        account.setBalance(account.getBalance()-amount);
        boolean success = accountDao.updateAccount(account);
        if(success) {
            String transactionId = generateTransactionId();
            Transaction transaction =new Transaction(
                    transactionId,
                    accountId,
                    null,
                    amount,
                    "WITHDRAW",
                    "现金取款"
            );
            accountDao.addTransaction(transaction);
            System.out.printf("取款成功！当前余额：%.2f\n",account.getBalance());
        }
        return success;
    }
    /**
     * 转账
     */
    public boolean transfer(String fromAccouId,String toAccountId,double amount) {
        if (amount < 0){
            System.out.println("转账金额必须大于0！");
            return false;
        }
        if(fromAccouId.equals(toAccountId)){
            System.out.println("不能想自己转账！");
            return false;
        }
        Account fromAccount = accountDao.finaAccountById(fromAccouId);
        Account toAccount = accountDao.finaAccountById(toAccountId);
        if(fromAccount == null || toAccount ==null){
            System.out.println("账户不存在！");
            return false;
        }
        if(fromAccount.getBalance()<amount){
            System.out.println("余额不足！ ");
            return false;
        }
        fromAccount.setBalance(fromAccount.getBalance()-amount);
        toAccount.setBalance(toAccount.getBalance()+amount);
        boolean success1 =accountDao.updateAccount(fromAccount);
        boolean success2 = accountDao.updateAccount(toAccount);

        if (success1 && success2) {
            //记录转账交易
            String transactionId =generateTransactionId();
            Transaction transaction = new Transaction(
                    transactionId,
                    fromAccouId,
                    toAccountId,
                    amount,
                    "TRANSFER",
                    "账户转账"
            );
            accountDao.addTransaction(transaction);
            System.out.printf("转账成功，您的余额：%.2F\n",fromAccount.getBalance());
            return true;
        }else {
            if(success1) {
                fromAccount.setBalance(fromAccount.getBalance()+amount);
                accountDao.updateAccount(fromAccount);
            }
            if(success2){
                toAccount.setBalance(toAccount.getBalance()-amount);
                accountDao.updateAccount(toAccount);
            }
            System.out.println("转账失败！ ");
            return false;

        }

    }
    /**
     * 查询余额
     */
    public void queryBalance(String accountId){
        Account account=accountDao.finaAccountById(accountId);
        if(account == null){
            System.out.println("账户不存在！");
            return;
        }
        System.out.printf("账户余额：%.2f\n",account.getBalance());
    }
    /**
     * 查询交易记录
     */
    public void queryTransactions(String accountId){
        Account account =accountDao.finaAccountById(accountId);
        if (account !=null){
            System.out.println("账户不存在！ ");
            return;
        }
        List<Transaction> transactions = accountDao.getTransactionsByAccountId(accountId);
        if (transactions.isEmpty()){
            System.out.println("暂无交易记录！ ");
            return;
        }
        System.out.println("交易记录： ");
        System.out.println("┌──────────────────┬────────────┬────────────┬────────┬──────────┬────────────────────┬────────────┐");
        System.out.println("│     交易ID       │  转出账户  │  转入账户  │  金额  │   类型   │        时间        │    描述    │");
        System.out.println("├──────────────────┼────────────┼────────────┼────────┼──────────┼────────────────────┼────────────┤");
        for (Transaction transaction:transactions){
            String fromAccount = transaction.getFromAccountId()!=null ? transaction.getFromAccountId() : "系统";
            String toAccount = transaction.getToAccountId() != null ? transaction.getToAccountId() : "系统";
            System.out.printf("│ %-16s │ %-10s │ %-10s │ %-6.2f │ %-8s │ %-18s │ %-10s │\n",
                    transaction.getTransactionId(),
                    fromAccount,
                    toAccount,
                    transaction.getAmount(),
                    getTypeName(transaction.getType()),
                    transaction.getTimestamp().toString().replace("T"," "),
                    transaction.getDescription());
        }
        System.out.println("└──────────────────┴────────────┴────────────┴────────┴──────────┴────────────────────┴────────────┘");
    }
    /**
     * 获取交易类型中的中文名称
     */
    private String getTypeName(String type) {
        switch (type){
            case "DEPOSIT": return "存款";
            case "WITHDRAW": return "取款";
            case "TRANSFER": return "转账";
            default: return type;
        }
    }
/**
 * 修改密码
 */
    public boolean changePassword(String accountId,String oldPassword,String newPAssword) {
        Account account=accountDao.finaAccountById(accountId);
        if(account ==null){
            System.out.println("账户不存在！");
            return false;
        }
        if(!PasswordUtil.verify(oldPassword,account.getPassword())){
            System.out.println("原密码错误！ ");
            return false;
        }
        if(!ValidationUtil.isValidPassword(newPAssword)) {
            System.out.println("新密码必须至少6位，且包含字母和数字！");
            return false;
        }
        //加密新密码
        String encryptedNewPassWord=PasswordUtil.encrypt(newPAssword);
        account.setPassword(encryptedNewPassWord);

        boolean success = accountDao.updateAccount(account);
        if(success) {
            System.out.println("密码修改成功 ！");
        }else {
            System.out.println("密码修改失败 ！");

        }
        return success;
    }


    }
