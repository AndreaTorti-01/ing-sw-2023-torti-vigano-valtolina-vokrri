import java.io.*;

public class datGenerator {
    public static void main(String args[]) {

        boolean[][] board2 = {
                {false, false, false, false, false, false, false, false, false},
                {false, false, false, true, true, false, false, false, false},
                {false, false, false, true, true, true, false, false, false},
                {false, false, true, true, true, true, true, true, false},
                {false, true, true, true, true, true, true, true, false},
                {false, true, true, true, true, true, true, false, false},
                {false, false, false, true, true, true, false, false, false},
                {false, false, false, false, true, true, false, false, false},
                {false, false, false, false, false, false, false, false, false},
        };

        boolean[][] board3 = {
                {false, false, false, true, false, false, false, false, false},
                {false, false, false, true, true, false, false, false, false},
                {false, false, true, true, true, true, true, false, false},
                {false, false, true, true, true, true, true, true, true},
                {false, true, true, true, true, true, true, true, false},
                {true, true, true, true, true, true, true, false, false},
                {false, false, true, true, true, true, true, false, false},
                {false, false, false, false, true, true, false, false, false},
                {false, false, false, false, false, true, false, false, false},
        };

        boolean[][] board4 = {
                {false, false, false, true, true, false, false, false, false},
                {false, false, false, true, true, true, false, false, false},
                {false, false, true, true, true, true, true, false, false},
                {false, true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true, false},
                {false, false, true, true, true, true, true, false, false},
                {false, false, false, true, true, true, false, false, false},
                {false, false, false, false, true, true, false, false, false},
        };

        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Board/board2.dat"));
            outputStream.writeObject(board2);
        } catch (Exception e) {
        }

        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Board/board3.dat"));
            outputStream.writeObject(board3);
        } catch (Exception e) {
        }

        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Board/board4.dat"));
            outputStream.writeObject(board4);
        } catch (Exception e) {
        }

    }
}