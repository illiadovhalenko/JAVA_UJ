package uj.wmii.pwj.spreadsheet;


interface A {
    default void show() {
        System.out.println("Metoda z interfejsu A");
    }
}

interface B extends A {
    default void show() {
        System.out.println("Metoda z interfejsu B");
    }
}

interface C extends A {
    default void show() {
        System.out.println("Metoda z interfejsu C");
    }
}

class D implements B, C {
    public void show() {
        //super.show(); // Wybieramy, która metoda wywołać
        B.super.show();
        C.super.show();
    }
}

class Cars{
    private static int a=10;
    static class Taxi{
        public void display() {
            System.out.println("Wartość a : " + a);
        }
    }
    class Truck{
        public void display() {
            System.out.println("Wartość a : " + a);
        }
    }
    Taxi taxi;
    Truck truck;
}

public class Cw1 {
    public static void main(String[] args) {
        final int[][][][][][][][][][][][][][][][]
                 [][][][][][][][][][][][][][][][]
                 [][][][][][][][][][][][][][][][]
                 [][][][][][][][][][][][][][][][]
                 [][][][][][][][][][][][][][][][]
                 [][][][][][][][][][][][][][][][]
                 [][][][][][][][][][][][][][][][]
                 [][][][][][][][][][][][][][][][]
                 [][][][][][][][][][][][][][][][]
                 [][][][][][][][][][][][][][][][]
                 [][][][][][][][][][][][][][][][]
                 [][][][][][][][][][][][][][][][]
                 [][][][][][][][][][][][][][][][]
                 [][][][][][][][][][][][][][][][]
                 [][][][][][][][][][][][][][][][]
                 [][][][][][][][][][][][][][][] x;

        //juz nie dziala
//        final int[][][][][][][][][][][][][][][][]
//                [][][][][][][][][][][][][][][][]
//                [][][][][][][][][][][][][][][][]
//                [][][][][][][][][][][][][][][][]
//                [][][][][][][][][][][][][][][][]
//                [][][][][][][][][][][][][][][][]
//                [][][][][][][][][][][][][][][][]
//                [][][][][][][][][][][][][][][][]
//                [][][][][][][][][][][][][][][][]
//                [][][][][][][][][][][][][][][][]
//                [][][][][][][][][][][][][][][][]
//                [][][][][][][][][][][][][][][][]
//                [][][][][][][][][][][][][][][][]
//                [][][][][][][][][][][][][][][][]
//                [][][][][][][][][][][][][][][][]
//                [][][][][][][][][][][][][][][][] y;
        Cars cars = new Cars();
        //Cars.Truck truck =  new Cars.Truck();
        Cars.Truck truck =  cars.new Truck();
        truck.display();
        Cars.Taxi taxi = new Cars.Taxi();
        truck.display();
        taxi.display();

        D d =new D();
        d.show();
    }
}
