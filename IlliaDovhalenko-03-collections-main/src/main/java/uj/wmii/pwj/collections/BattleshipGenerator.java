package uj.wmii.pwj.collections;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

/**
 * 4 jednomasztowce
 * 3 dwumasztowce
 * 2 trójmasztowce
 * 1 czteromasztowiec
 *
 * direction == 0 -- oś X
 * direction == 1 -- oś Y
 */

public interface BattleshipGenerator {

    String generateMap();

    static BattleshipGenerator defaultInstance() {
        return new Battleship();
    }

}
class Battleship implements BattleshipGenerator {
    static class Point {
        int x, y;

        Point(int x_, int y_) {
            x = Math.max(x_, 0);
            y = Math.max(y_, 0);
        }
    }

    StringBuilder board;
    int size_of_board = 10;
    int num_of_type_ships = 4;
    int[] ships = new int[]{4, 3, 2, 1};

    boolean is_on_the_board(Point point, int size, int direction) {
        return direction == 0 ? (point.x + size) < size_of_board : (point.y + size) < size_of_board;
    }

    boolean is_ship_near(Point point, int size, int direction) {
        Point upper_left_corner;
        Point lower_right_corner;
        if (direction == 0) {
            upper_left_corner = new Point(point.x - 1, point.y - 1);
            lower_right_corner = new Point(point.x + size + 1, point.y + 1);
        } else {
            upper_left_corner = new Point(point.x - 1, point.y - 1);
            lower_right_corner = new Point(point.x + 1, point.y + size + 1);
        }
        for (int x = upper_left_corner.x; x <= lower_right_corner.x; x++) {
            for (int y = upper_left_corner.y; y <= lower_right_corner.y; y++) {
                if (x < size_of_board && y < size_of_board && board.charAt(index(y, x)) == '#')
                    return true;
            }
        }
        return false;
    }

    void set_ship(int size_of_ship) {
        Random random = new Random();
        int dir = random.nextInt(2);
        Point start_point = new Point(random.nextInt(size_of_board), random.nextInt(size_of_board));
        while (!(is_on_the_board(start_point, size_of_ship, dir) && !is_ship_near(start_point, size_of_ship, dir))) {
            start_point = new Point(random.nextInt(size_of_board), random.nextInt(size_of_board));
            dir = random.nextInt(2);
        }
        if (dir == 0)
            for (int i = 0; i < size_of_ship; i++)
                board.setCharAt(index(start_point.y, start_point.x + i), '#');
        else
            for (int i = 0; i < size_of_ship; i++)
                board.setCharAt(index(start_point.y + i, start_point.x), '#');
    }


    int index(int i, int j) {
        return i * size_of_board + j;
    }

    @Override
    public String generateMap() {
        String board_str = ".";
        board_str = board_str.repeat((size_of_board) * (size_of_board));
        board = new StringBuilder(board_str);
        for (int i = 0; i < num_of_type_ships; i++) {
            for (int j = 0; j < ships[num_of_type_ships - i - 1]; j++) {
                set_ship(num_of_type_ships - i);
            }
        }
        return board.toString();
    }
}