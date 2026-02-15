import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class GamePanel extends JPanel {
    private GameController controller;

    public void setController(GameController controller) {
        this.controller = controller;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (controller == null) return;

        Graphics2D g2d = (Graphics2D) g;

        // 绘制网格线
        g2d.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i <= Constants.ROWS; i++) {
            g2d.drawLine(0, i * Constants.CELL_SIZE,
                    Constants.COLS * Constants.CELL_SIZE, i * Constants.CELL_SIZE);
        }
        for (int j = 0; j <= Constants.COLS; j++) {
            g2d.drawLine(j * Constants.CELL_SIZE, 0,
                    j * Constants.CELL_SIZE, Constants.ROWS * Constants.CELL_SIZE);
        }

        // 绘制墙壁（边界）
        g2d.setColor(Color.DARK_GRAY);
        for (int r = 0; r < Constants.ROWS; r++) {
            for (int c = 0; c < Constants.COLS; c++) {
                if (r == 0 || r == Constants.ROWS-1 || c == 0 || c == Constants.COLS-1) {
                    g2d.fillRect(c * Constants.CELL_SIZE, r * Constants.CELL_SIZE,
                            Constants.CELL_SIZE, Constants.CELL_SIZE);
                }
            }
        }

        // 绘制食物
        Food food = controller.getFood();
        if (food != null) {
            g2d.setColor(Color.RED);
            int x = food.getCol() * Constants.CELL_SIZE;
            int y = food.getRow() * Constants.CELL_SIZE;
            g2d.fillOval(x + 2, y + 2, Constants.CELL_SIZE - 4, Constants.CELL_SIZE - 4);
        }

        // 绘制蛇
        Snake snake = controller.getSnake();
        if (snake != null && !snake.getBody().isEmpty()) {
            LinkedList<Point> body = snake.getBody();
            for (int i = 0; i < body.size(); i++) {
                Point p = body.get(i);
                int x = p.y * Constants.CELL_SIZE;
                int y = p.x * Constants.CELL_SIZE;
                if (i == 0) {
                    g2d.setColor(new Color(0, 150, 0)); // 蛇头深绿
                } else {
                    g2d.setColor(Color.GREEN);          // 蛇身绿
                }
                g2d.fillRect(x + 2, y + 2, Constants.CELL_SIZE - 4, Constants.CELL_SIZE - 4);
            }
        }
    }
}