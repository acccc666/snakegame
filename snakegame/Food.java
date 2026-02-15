import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;

public class Food {
    private Point position;
    private Random random = new Random();

    public Food(LinkedList<Point> snakeBody) {
        regenerate(snakeBody);
    }

    public Point getPosition() {
        return position;
    }

    public int getRow() { return position.x; }
    public int getCol() { return position.y; }

    /**
     * 在空白格生成新食物，最多尝试5次
     * @param snakeBody 蛇身坐标列表
     * @return true表示生成成功，false表示地图已满
     */
    public boolean regenerate(LinkedList<Point> snakeBody) {
        for (int attempt = 0; attempt < 5; attempt++) {
            int row = random.nextInt(Constants.MAX_ROW - Constants.MIN_ROW + 1) + Constants.MIN_ROW;
            int col = random.nextInt(Constants.MAX_COL - Constants.MIN_COL + 1) + Constants.MIN_COL;
            Point p = new Point(row, col);
            if (!snakeBody.contains(p)) {
                position = p;
                return true;
            }
        }
        // 尝试5次失败，认为地图已满
        return false;
    }
}