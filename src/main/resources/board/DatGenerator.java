import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class DatGenerator {
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
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("board/board2.dat"));
            outputStream.writeObject(board2);
        } catch (Exception e) {
        }

        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("board/board3.dat"));
            outputStream.writeObject(board3);
        } catch (Exception e) {
        }

        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("board/board4.dat"));
            outputStream.writeObject(board4);
        } catch (Exception e) {
        }

    }
}