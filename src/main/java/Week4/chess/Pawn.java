package Week4.chess;

public class Pawn  extends  Piece{
    public Pawn(int x, int y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
    @Override
    public String toString() {
        return "x = " + this.x + "; " + "y = " + this.y;
    }

    public boolean canMove(int nx, int ny) {
        if (nx != x) {
            return false;
        }
        if (y == 2) {
            return (nx == x && y + 2 >= ny && ny > y);
            }
            else {
                return (nx == x && y +1 == ny);

        }
    }
    public void move(int nx, int ny){
        if(canMove(nx,ny)){
            this.x = nx;
            this.y = ny;
        }
    }
    public boolean canBeat(int nx, int ny){
        if(x + 1 != nx) {
            return false;
        }
        return (y + 1 == ny || y - 1 == ny);
    }
    public void beat( int nx, int ny) {
        if(canBeat(nx,ny)){
            this.x = nx;
            this.y = ny;
        }
    }
}


