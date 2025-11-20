package src.main;
import src.dao.AccountDao;
import src.entity.Account;
import src.service.AccountService;
import src.util.ValidationUtil;

import java.util.Scanner;
public class Main {
    private AccountService accountService;
    private Scanner scanner;
    private Account currentAccount;

    public Main(){
        this.accountService = new AccountService();
        this.scanner =new Scanner(System.in);
    }
    public void start(){
        System.out.println("=========银行账户管理系统========");
        while(true){
            if (currentAccount==null){
                showMainMenu();
            }else {
                showUserMenu();
            }

        }
    }
    public void showMainMenu(){
        System.out.println("\n请选择操作：");
        System.out.println("1.用户注册 ");
        System.out.println("2.用户登录");
        System.out.println("0.退出系统");
        System.out.println("请输入选择： ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                register();
                break;
            case "2":
                login();
                break;
            case "0":
                System.out.println("感谢使用银行账户管理系统，再见！ ");
                System.exit(0);
                break;
            default:
                System.out.println("无效的选择，请重新输入！！");
        }
    }
    private void showUserMenu(){
        System.out.println("\n===欢迎"+currentAccount.getOwnerName()+"===");
        System.out.println("请选择选择： ");
        System.out.println("1. 存款");
        System.out.println("2. 取款");
        System.out.println("3. 转账");
        System.out.println("4. 查询余额");
        System.out.println("5. 查询交易记录");
        System.out.println("6. 修改密码");
        System.out.println("7. 退出登录");
        System.out.println("0. 退出系统");
        System.out.print("请输入选择： ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                deposit();
                break;
            case "2":
                withdraw();
                break;
            case "3":
                transfer();
                break;
            case "4":
                queryBalance();
                break;
            case "5":
                queryTransactions();
                break;
            case "6":
                changePassword();
                break;
            case "7":
                logout();
                break;
            case "0":
                System.out.println("感谢使用银行账户系统，再见！");
                System.exit(0);
                break;
            default:
                System.out.println("无效的选择，请重新输入！");
        }
    }
    private void register(){
        System.out.println("\n----用户注册----");
        System.out.println("请输入账户ID (6-12位数字): ");
        String acconutId = scanner.nextLine().trim();

        System.out.println("请输入密码(至少6位，包含字母和数字)： ");
        String password = scanner.nextLine().trim();

        System.out.println("请输入户主姓名： ");
        String ownerName = scanner.nextLine().trim();

        System.out.println("请输入初始存款金额： ");
        String amountStr = scanner.nextLine().trim();

        if(!ValidationUtil.isNumeric(amountStr)){
            System.out.println("金额必须是数字！ ");
            return;
        }
        double amount = Double.parseDouble(amountStr);
        accountService.deposit(currentAccount.getAccountId(),amount);
    }
    private void login(){
        System.out.println("\n-----用户登录-----");

        System.out.println("请输入账户ID: ");
        String accountId =scanner.nextLine().trim();

        System.out.println("请输入密码： ");
        String password =scanner.nextLine().trim();

        Account account = accountService.login(accountId,password);
        if (account != null ) {
            currentAccount =account;
        }
    }

    private void deposit(){
        System.out.println("\n----存款----");
        System.out.println("请输入存款金额： ");
        String amountStr = scanner.nextLine().trim();
        if(!ValidationUtil.isNumeric(amountStr)){
            System.out.println("金额必须是数字！");
            return;
        }
        double amount = Double.parseDouble(amountStr);
        accountService.deposit(currentAccount.getAccountId(),amount);
    }
    private void withdraw() {
        System.out.println("\n----取款----");
        System.out.println("请输入取款金额： ");
        String amountStr= scanner.nextLine().trim();
        if(!ValidationUtil.isNumeric(amountStr)){
            return;
        }
        double amount = Double.parseDouble(amountStr);
        accountService.withdraw(currentAccount.getAccountId(),amount);
    }
    private void transfer(){
        System.out.println("\n----转账----");
        System.out.println("请输入对方账户ID: ");
        String toAccountId =scanner.nextLine().trim();

        System.out.println("请输入转账金额： ");
        String amountStr =scanner.nextLine().trim();

        if(!ValidationUtil.isNumeric(amountStr)){
            System.out.println("金额必须是数字！ ");
            return;
        }
        double amount =Double.parseDouble(amountStr);
        accountService.transfer(currentAccount.getAccountId(),toAccountId,amount);

    }

    private void queryBalance() {
        System.out.println("\n----查询余额----");
        accountService.queryBalance(currentAccount.getAccountId());
    }

    private void queryTransactions(){
        System.out.println("\n----查询交易记录----");
        accountService.queryTransactions(currentAccount.getAccountId());

    }
    private void changePassword() {
        System.out.println("\n----修改密码----");

        System.out.println("请输入原密码： ");
        String oldPassword = scanner.nextLine().trim();
        System.out.println("请输入新密码： ");
        String newPassword = scanner.nextLine().trim();
        System.out.println("请确认新密码： ");
        String confirmPassword = scanner.nextLine().trim();
        if(!newPassword.equals(confirmPassword)){
            System.out.println("两次输入的新密码不一致！ ");
            return;
        }
        accountService.changePassword(currentAccount.getAccountId(),oldPassword,newPassword);
    }
    private void logout(){
        currentAccount=null;
        System.out.println("已成功退出登录");
    }

    public static void main(String[] args){
        Main main =new Main();
        main.start();
    }
}
