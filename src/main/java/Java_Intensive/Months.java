package Java_Intensive;

public class Months {

    public static void main(String[] args){
        String[] month = new String[]{ "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        //for(int i=0; i < month.length; i++){   prints months
            //System.out.println(month[i]);

        for(int i = month.length - 1; i >= 0; i-- ) { // prints Months in reverse order
            System.out.println(month[i]);
        }

    }
}
