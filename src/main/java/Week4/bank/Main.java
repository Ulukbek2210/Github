package Week4.bank;

public class Main {
    public static void main(String[] args) {
        CheckingAccount myCheckingAccount = new CheckingAccount(0);

        myCheckingAccount.deposit(50);

        myCheckingAccount.withdraw(100);
        System.out.println(myCheckingAccount.balance);

    }
    }
