package Week4.bank;

public class Main {
    public static void main(String[] args) {
        CheckingAccount myAccount = new CheckingAccount(0);

        myAccount.deposit(500);

        myAccount.withdraw(100);

        System.out.println(myAccount.getBalance());
    }
    }
