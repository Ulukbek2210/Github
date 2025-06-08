package Week4.zoo;

public class CheckingAccount {
    public double balance;

    public CheckingAccount(double balance) {
        this.balance = balance;

    }

    public void deposit(double amount) {
        this.balance += amount;

    }

    public double withdraw(double amount) {
        if (this.balance >= amount) {
            this.balance -= amount;

        }
        return amount;
    }
}

