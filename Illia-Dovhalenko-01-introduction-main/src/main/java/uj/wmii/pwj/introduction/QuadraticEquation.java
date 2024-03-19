package uj.wmii.pwj.introduction;

public class QuadraticEquation {

    public double[] findRoots(double a, double b, double c) {
        double delta=b*b-4*a*c;
        return switch ((int) Math.signum(delta)) {
            case -1 -> new double[0];
            case 0 -> new double[]{(-b / (2 * a))};
            case 1 -> new double[]{(-b + Math.sqrt(delta)) / (2 * a), (-b - Math.sqrt(delta)) / (2 * a)};
            default -> null;
        };
    }

}

