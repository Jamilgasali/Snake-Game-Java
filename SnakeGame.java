import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Segment {
        int posX;
        int posY;

        Segment(int posX, int posY) {
            this.posX = posX;
            this.posY = posY;
        }
    }

    int gridWidth;
    int gridHeight;
    int cellSize = 25;

    // Snake
    Segment snakeHead;
    ArrayList<Segment> snakeSegments;

    // Food
    Segment foodItem;
    Random randomGenerator;

    // game logic
    Timer gameTimer;
    int moveX;
    int moveY;
    boolean isGameOver = false;

    public SnakeGame(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        setPreferredSize(new Dimension(this.gridWidth, this.gridHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Segment(5, 5);
        snakeSegments = new ArrayList<Segment>();

        foodItem = new Segment(10, 10);
        randomGenerator = new Random();
        spawnFood();

        moveX = 1;
        moveY = 0;

        gameTimer = new Timer(100, this);
        gameTimer.start();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        render(graphics);
    }

    public void render(Graphics graphics) {
        // Grid
        for (int i = 0; i < gridWidth / cellSize; i++) {
            graphics.drawLine(i * cellSize, 0, i * cellSize, gridHeight);
            graphics.drawLine(0, i * cellSize, gridWidth, i * cellSize);
        }

        // Food
        graphics.setColor(Color.red);
        graphics.fillRect(foodItem.posX * cellSize, foodItem.posY * cellSize, cellSize, cellSize);

        // Snake Head
        graphics.setColor(Color.green);
        graphics.fillRect(snakeHead.posX * cellSize, snakeHead.posY * cellSize, cellSize, cellSize);

        // Snake Segments
        for (int i = 0; i < snakeSegments.size(); i++) {
            Segment segment = snakeSegments.get(i);
            graphics.fillRect(segment.posX * cellSize, segment.posY * cellSize, cellSize, cellSize);
        }

        // Score
        graphics.setFont(new Font("Arial", Font.PLAIN, 16));
        if (isGameOver) {
            graphics.setColor(Color.red);
            graphics.drawString("Game Over: " + String.valueOf(snakeSegments.size()), cellSize - 16, cellSize);
        } else {
            graphics.drawString("Score: " + String.valueOf(snakeSegments.size()), cellSize - 16, cellSize);
        }
    }

    public void spawnFood() {
        foodItem.posX = randomGenerator.nextInt(gridWidth / cellSize);
        foodItem.posY = randomGenerator.nextInt(gridHeight / cellSize);
    }

    public void updateGame() {
        // Check food collision
        if (hasCollision(snakeHead, foodItem)) {
            snakeSegments.add(new Segment(foodItem.posX, foodItem.posY));
            spawnFood();
        }

        for (int i = snakeSegments.size() - 1; i >= 0; i--) {
            Segment currentSegment = snakeSegments.get(i);
            if (i == 0) {
                currentSegment.posX = snakeHead.posX;
                currentSegment.posY = snakeHead.posY;
            } else {
                Segment previousSegment = snakeSegments.get(i - 1);
                currentSegment.posX = previousSegment.posX;
                currentSegment.posY = previousSegment.posY;
            }
        }

        // Move snake head
        snakeHead.posX += moveX;
        snakeHead.posY += moveY;

        for (int i = 0; i < snakeSegments.size(); i++) {
            Segment segment = snakeSegments.get(i);

            // Self-collision
            if (hasCollision(snakeHead, segment)) {
                isGameOver = true;
            }
        }

        if (snakeHead.posX * cellSize < 0 || snakeHead.posX * cellSize > gridWidth || // Out of left or right bounds
            snakeHead.posY * cellSize < 0 || snakeHead.posY * cellSize > gridHeight) { // Out of top or bottom bounds
            isGameOver = true;
        }
    }

    public boolean hasCollision(Segment segment1, Segment segment2) {
        return segment1.posX == segment2.posX && segment1.posY == segment2.posY;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        updateGame();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_UP && moveY != 1) {
            moveX = 0;
            moveY = -1;
        } else if (event.getKeyCode() == KeyEvent.VK_DOWN && moveY != -1) {
            moveX = 0;
            moveY = 1;
        } else if (event.getKeyCode() == KeyEvent.VK_LEFT && moveX != 1) {
            moveX = -1;
            moveY = 0;
        } else if (event.getKeyCode() == KeyEvent.VK_RIGHT && moveX != -1) {
            moveX = 1;
            moveY = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {}

    @Override
    public void keyReleased(KeyEvent event) {}
}
