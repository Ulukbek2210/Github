package Week4.zoo;

public class Main {
    public static void main(String[] args){
        CheckingAccount myAccount = new CheckingAccount(0);

        myAccount.deposit((50));
        //...
        myAccount.withdraw(20);
        //...
        System.out.println(myAccount.balance);
    }
}
