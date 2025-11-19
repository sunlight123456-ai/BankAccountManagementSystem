package src.dao;
import src.entity.Account;
import src.entity.Transaction;
import src.util.FileUtil;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * 数据访问层
 */
public class AccountDao {
    private static final String ACCOUNT_FILE ="data/account.txt";
    private static final String TRANSACTION_FILE="data/transaction.txt";
    private List<Account> accounts;
    private List<Transaction> transactions;

    public AccountDao() {
        this.accounts = new ArrayList<>();
        this.transactions = new ArrayList<>();
        loadAccountsFromFile();
        loadTransactionFromFile();
    }
    /**
     * 从文件加载账户数据
     */
    private void loadAccountsFromFile(){
        List<String> lines = FileUtil.readFromFile(ACCOUNT_FILE);
        for(String line : lines){
            try{
                Account account = Account.fromFileString(line);
                accounts.add(account);
            } catch (Exception e){
                System.err.println("加载数据失败： " +line+", 错误："+e.getMessage());
            }
        }
        System.out.println("成功加载 " + accounts.size() +"个账户数据");
    }
    /**
     * 从文件加载交易数据
     */
    private void loadTransactionFromFile(){
        List<String> lines = FileUtil.readFromFile(TRANSACTION_FILE);
        for(String line:lines){
            try{
                Transaction transaction = Transaction.fromFileString(line);
                transactions.add(transaction);
            }catch (Exception e){
                System.err.println("加载数据失败： " +line+", 错误："+e.getMessage());
            }
        }
        System.out.println("成功加载 " + transactions.size() +"条交易数据");
    }
    /**
     * 将交易记录关联到对应的账户
     */
    private void associateTransactionsWithAccounts() {
        for (Transaction transaction : transactions) {
            String fromAccountId = transaction.getFromAccountId();
            String toAccountId = transaction.getToAccountId();

            if(fromAccountId != null && !fromAccountId.isEmpty()){
                Account fromAccount = finaAccountById(fromAccountId);
                if (fromAccount != null) {
                    fromAccount.addTransaction(transaction);
                }
            }
            if(toAccountId != null && !toAccountId.isEmpty()){
                Account toAccount = finaAccountById(toAccountId);
                if (toAccount != null) {
                    toAccount.addTransaction(transaction);
                }
            }
        }


    }
    /**
     *保存账号数据到文件
     */
    private void saveAccountsToFile(){
        List<String> lines = accounts.stream()
                .map(Account::toFileString)
                .collect(Collectors.toList());
        FileUtil.writeToFile(ACCOUNT_FILE,lines);
    }
    /**
     * 保存交易数据到文件
     */
    private void saveTransactionsToFile(){
        List<String> lines = transactions.stream()
                .map(Transaction::toFileString)
                .collect(Collectors.toList());
        FileUtil.writeToFile(TRANSACTION_FILE,lines);
    }
    /**
     * 根据账号ID查找账户
     */
    public Account finaAccountById(String accountId){
        for(Account account: accounts){
            if (account.getAccountId().equals(accountId)){
                return account;
            }
        }
        return null;
    }

    /**
     * 添加账户
     */
    public boolean addAccount(Account account){
        if (finaAccountById(account.getAccountId()) == null){
            return false;
        }
        accounts.add(account);
        saveAccountsToFile();
        return true;
    }
    /**
     * 更新账户信息
     */
    public boolean updateAccount(Account updateAccount){
        for (int i=0;i<accounts.size();i++){
            Account account = accounts.get(i);
            if(account.getAccountId().equals(updateAccount.getAccountId())){
                accounts.set(i,updateAccount);
                saveAccountsToFile();
                return true;
            }
        }
        return false;
    }
    /**
     * 添加交易记录
     */
    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
        String fromAccountId = transaction.getFromAccountId();
        String toAccountId  = transaction.getToAccountId();
        if(fromAccountId != null && !fromAccountId.isEmpty()){
            Account fromAccount = finaAccountById(fromAccountId);
            if(fromAccount != null) {
                fromAccount.addTransaction(transaction);
            }
        }

        if(toAccountId !=null && !toAccountId.isEmpty()){
            Account toAccount = finaAccountById(toAccountId);
            if (toAccount != null){
                toAccount.addTransaction(transaction);
            }
        }
        saveTransactionsToFile();
    }
    /**
     * 获取所有账户
     */
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }
    /**
     * 根据账户ID获取交易记录
     */
    public List<Transaction> getTransactionsByAccountId(String accountId){
        List<Transaction> result =new ArrayList<>();
        for(Transaction transaction:transactions){
            if(transaction.getFromAccountId().equals(accountId) || transaction.getToAccountId().equals(accountId)){
                result.add(transaction);
            }
        }

        return result;
    }

}
