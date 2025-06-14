package Week4.chess;

public class Main {
    public static void main(String[] args) {
        Pawn p1 = new Pawn(2,2, "white");
        Pawn p2 = new Pawn(3,3,"black");

        p1.beat(p2.getX(), p2.getY());
        System.out.println(p1.toString());
    }
}
