import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel implements KeyListener, ActionListener {
    private boolean isRunning = false;
    private int score = 0;

    private int totalBricks = 21;
    private final Timer timer;
    private final int delay = 8;

    private int playerX = 310;

    private int ballX = 120;
    private int ballY = 350;
    private int ballXDir = -1;
    private int ballYDir = -2;

    private MapGenerator mapGenerator;

    public Game() {
        mapGenerator = new MapGenerator(3, 7);

        // Listen for keys and notifies this component when there is an input
        addKeyListener(this);
        // Determines which component is currently active and ready to receive input from the keyboard or other input devices
        // Component can be selected to receive inputs
        setFocusable(true);
        // Keys that allow user to move focus from one component to another (e.g. tab)
        setFocusTraversalKeysEnabled(false);
        // Concept is same like frames per second(FPS), delay is the FPS
        timer = new Timer(delay, this);
        timer.start();
    }

    // Draw things on the window
    public void paint(Graphics graphics) {
        // background
        graphics.setColor(Color.black);
        graphics.fillRect(1, 1, 692, 592);

        // draw bricks
        mapGenerator.draw((Graphics2D) graphics);

        // borders
        graphics.setColor(Color.yellow);
        graphics.fillRect(0, 0, 3, 592);
        graphics.fillRect(0, 0, 692, 3);
        graphics.fillRect(683, 0, 3, 592);

        // scores display
        graphics.setColor(Color.white);
        graphics.setFont(new Font("serif", Font.BOLD, 25));
        graphics.drawString("Score: " + score, 590, 30);

        // paddle (player)
        graphics.setColor(Color.green);
        graphics.fillRect(playerX, 550, 100, 8);

        // ball
        graphics.setColor(Color.yellow);
        graphics.fillOval(ballX, ballY, 20, 20);

        // Win game
        if (totalBricks <= 0) {
            isRunning = false;
            ballXDir = 0;
            ballYDir = 0;

            graphics.setColor(Color.green);
            graphics.setFont(new Font("serif", Font.BOLD, 30));
            graphics.drawString("You won! Scores: " + score, 190, 300);
            graphics.setFont(new Font("serif", Font.BOLD, 20));
            graphics.drawString("Press enter to restart.", 230, 350);
        }

        // If ball crosses the bottom border
        if (ballY > 570) {
            isRunning = false;
            ballXDir = 0;
            ballYDir = 0;

            graphics.setColor(Color.red);
            graphics.setFont(new Font("serif", Font.BOLD, 30));
            graphics.drawString("Game Over, Scores: " + score, 190, 300);
            graphics.setFont(new Font("serif", Font.BOLD, 20));
            graphics.drawString("Press enter to restart.", 230, 350);
        }

        // Release system resources
        graphics.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (isRunning) {
            if (new Rectangle(ballX, ballY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYDir = -ballYDir;
            }

            A:
            for (int i = 0; i < mapGenerator.map.length; i++) {
                for (int j = 0; j < mapGenerator.map[i].length; j++) {
                    if (mapGenerator.map[i][j] > 0) {
                        int brickX = j * mapGenerator.brickWidth + 80;
                        int brickY = i * mapGenerator.brickHeight + 50;
                        int brickWidth = mapGenerator.brickWidth;
                        int brickHeight = mapGenerator.brickHeight;

                        Rectangle brickRectangle = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRectangle = new Rectangle(ballX, ballY, 20, 20);

                        if (ballRectangle.intersects(brickRectangle)) {
                            mapGenerator.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballX + 19 <= brickRectangle.x || ballX + 1 >= brickRectangle.x + brickRectangle.width) {
                                ballXDir = -ballXDir;
                            } else {
                                ballYDir = -ballYDir;
                            }

                            break A;
                        }
                    }
                }
            }

            ballX += ballXDir;
            ballY += ballYDir;

            if (ballX <= 0 || ballX >= 668) ballXDir = -ballXDir;
            if (ballY <= 0) ballYDir = -ballYDir;
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Moving left
        if (e.getKeyCode() == KeyEvent.VK_D && playerX <= 570) {
            moveRight();
        }
        // Moving left
        if (e.getKeyCode() == KeyEvent.VK_A && playerX >= 10) {
            moveLeft();
        }
        // Restart game
        if (e.getKeyCode() == KeyEvent.VK_ENTER && !isRunning) {
            isRunning = true;
            ballX = 120;
            ballY = 350;
            ballXDir = -1;
            ballYDir = -2;
            playerX = 310;
            score = 0;
            totalBricks = 21;
            mapGenerator = new MapGenerator(3, 7);

            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void moveRight() {
        isRunning = true;
        playerX += 20;
    }

    public void moveLeft() {
        isRunning = true;
        playerX -= 20;
    }
}
