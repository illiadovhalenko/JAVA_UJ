package src;

public class Point {
    int x;
    int y;
    public Point(int x_, int y_){
        x=Math.max(x_, 0);
        y=Math.max(y_, 0);
    }
}