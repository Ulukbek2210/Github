package Week4.bank;

public class CheckingAccount {

    public double balance;

    public CheckingAccount(double balance){
        this.balance = balance;
    }
    public void deposit(double amount){
        this.balance += amount;

    }

    public void withdraw(double amount) {
        if(balance >= amount) {
            this.balance -= amount;
        }
        else{
            System.out.println(" You have less amount of money than you want to withdraw,\n Please try again. Your balance is: " + this.balance);

        }
    }
}


