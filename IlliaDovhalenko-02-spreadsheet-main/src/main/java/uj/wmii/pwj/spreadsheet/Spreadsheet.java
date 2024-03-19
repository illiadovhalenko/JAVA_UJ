package uj.wmii.pwj.spreadsheet;

enum Excel {
    $ {
        @Override
        public int apply(String[][] arr, String... a) {
            int column = a[0].charAt(0) - 'A';
            int row = Integer.parseInt(a[0].substring(1)) - 1;
            if (isNumeric(arr[row][column]))
                return Integer.parseInt(arr[row][column]);
            else
                return cell(arr, arr[row][column]);
        }
    },
    ADD {
        @Override
        public int apply(String[][] arr, String... a) {
            return cell(arr, a[0]) + cell(arr, a[1]);
        }
    },
    DIV {
        @Override
        public int apply(String[][] arr, String... a) {
            return cell(arr, a[0]) / cell(arr, a[1]);
        }
    },
    MOD {
        @Override
        public int apply(String[][] arr, String... a) {
            return cell(arr, a[0]) % cell(arr, a[1]);
        }
    },
    MUL {
        @Override
        public int apply(String[][] arr, String... a) {
            return cell(arr, a[0]) * cell(arr, a[1]);
        }
    },
    SUB {
        @Override
        public int apply(String[][] arr, String... a) {
            return cell(arr, a[0]) - cell(arr, a[1]);
        }
    },
    ARRAY {
        public int apply(String[][] arr, String... a) {
            return 0;
        }
    };

    public abstract int apply(String[][] arr, String... a);

    public String[][] calculate(String[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++)
                arr[i][j] = Integer.toString(cell(arr, arr[i][j]));
        }
        return arr;
    }

    public int cell(String[][] arr, String str) {
        switch (str.charAt(0)) {
            case '$':
                Excel op1 = Excel.valueOf(str.substring(0, 1));
                return op1.apply(arr, str.substring(1), "");
            case '=':
                Excel op2 = Excel.valueOf(str.substring(1, 4));
                int index_of_last_bracket = str.lastIndexOf(')');
                String[] argumetns_in_form = str.substring(5, index_of_last_bracket).split(",");
                return op2.apply(arr, argumetns_in_form[0], argumetns_in_form[1]);
            default:
                return Integer.parseInt(str);
        }
    }

    public boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

public class Spreadsheet {
    public String[][] calculate(String[][] input) {
        Excel res = Excel.ARRAY;
        return res.calculate(input);
    }
}
