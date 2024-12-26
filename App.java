import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int gridWidth = 600;
        int gridHeight = gridWidth;

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(gridWidth, gridHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGame snakeGame = new SnakeGame(gridWidth, gridHeight);
        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();
    }
}
