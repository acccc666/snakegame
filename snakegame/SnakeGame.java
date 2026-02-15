public class SnakeGame {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new GameFrame().setVisible(true);
        });
    }
}