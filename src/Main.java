import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        Game game = new Game();

        // Setting jframe window properties
        // Setting position and dimensions of window
        jFrame.setBounds(10, 10, 700, 600);
        // Setting title of window
        jFrame.setTitle("Brick Breaker");
        // Make the window non-resizable
        jFrame.setResizable(false);
        // Make the window visible
        jFrame.setVisible(true);
        // Allow the window to be closed when the 'X' is clicked
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Add the game object in the jframe object
        jFrame.add(game);
    }
}