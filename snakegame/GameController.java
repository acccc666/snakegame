import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class GameController extends KeyAdapter implements ActionListener {
    private GamePanel panel;
    private GameFrame frame;
    private Snake snake;
    private Food food;
    private Direction currentDir;
    private Direction pendingDir;
    private int score;
    private int interval;
    private boolean running;
    private long startTime;
    private Timer moveTimer;
    private Timer clockTimer;
    private boolean paused; // 新增：暂停状态

    public GameController(GamePanel panel, GameFrame frame) {
        this.panel = panel;
        this.frame = frame;
        panel.setController(this);
        initGame();
        startTimers();
    }

    private void initGame() {
        snake = new Snake();
        currentDir = Direction.RIGHT;
        pendingDir = null;
        score = 0;
        interval = 200;
        food = new Food(snake.getBody());
        running = true;
        paused = false; // 初始化暂停为 false
        startTime = System.currentTimeMillis();
        frame.updateScore(score);
    }

    private void startTimers() {
        moveTimer = new Timer(interval, this);
        moveTimer.start();

        clockTimer = new Timer(1000, e -> {
            if (running && !paused) { // 暂停时不计时
                long seconds = (System.currentTimeMillis() - startTime) / 1000;
                frame.updateTime(seconds);
            }
        });
        clockTimer.start();
    }

    public void resetGame() {
        moveTimer.stop();
        clockTimer.stop();
        initGame(); // 会重置 paused = false
        startTimers();
        panel.repaint();
    }

    public Snake getSnake() { return snake; }
    public Food getFood() { return food; }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!running || paused) return; // 暂停时也不移动
        move();
    }

    private void move() {
        // 应用待处理方向
        if (pendingDir != null && !isOpposite(currentDir, pendingDir)) {
            currentDir = pendingDir;
            pendingDir = null;
        }

        // 计算新蛇头
        Point head = snake.getHead();
        int newRow = head.x, newCol = head.y;
        switch (currentDir) {
            case UP:    newRow--; break;
            case DOWN:  newRow++; break;
            case LEFT:  newCol--; break;
            case RIGHT: newCol++; break;
        }
        Point newHead = new Point(newRow, newCol);

        // 检查是否吃到食物
        boolean ateFood = food != null && newHead.equals(food.getPosition());

        if (ateFood) {
            // 吃食物：蛇头前进，蛇尾保留
            snake.moveAndGrow(newHead);
            score += 10;
            interval = Math.max(100, interval - 10);
            moveTimer.setDelay(interval);
            frame.updateScore(score);

            // 生成新食物
            if (!food.regenerate(snake.getBody())) {
                gameOver(true); // 地图满，胜利
                return;
            }
        } else {
            // 普通移动
            snake.move(newHead);

            // 碰撞检测
            if (isCollision()) {
                gameOver(false);
                return;
            }
        }

        panel.repaint();
    }

    private boolean isOpposite(Direction d1, Direction d2) {
        return (d1 == Direction.UP && d2 == Direction.DOWN) ||
                (d1 == Direction.DOWN && d2 == Direction.UP) ||
                (d1 == Direction.LEFT && d2 == Direction.RIGHT) ||
                (d1 == Direction.RIGHT && d2 == Direction.LEFT);
    }

    private boolean isCollision() {
        Point head = snake.getHead();
        // 撞墙检测
        if (head.x < Constants.MIN_ROW || head.x > Constants.MAX_ROW ||
                head.y < Constants.MIN_COL || head.y > Constants.MAX_COL) {
            return true;
        }
        // 撞自身
        LinkedList<Point> body = snake.getBody();
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }

    private void gameOver(boolean isWin) {
        if (!running) return;
        running = false;
        paused = false; // 游戏结束时清除暂停状态
        moveTimer.stop();
        clockTimer.stop();

        long duration = (System.currentTimeMillis() - startTime) / 1000;
        String message;
        if (isWin) {
            message = "恭喜通关！地图已无空位\n得分: " + score + "\n时长: " + duration + " 秒";
        } else {
            message = "游戏结束！\n得分: " + score + "\n时长: " + duration + " 秒";
        }
        JOptionPane.showMessageDialog(frame, message, "游戏结束", JOptionPane.INFORMATION_MESSAGE);
    }


    @Override
    public void keyPressed(KeyEvent e) {
        // 空格键处理（暂停/继续）
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!running) return; // 游戏已结束时无效
            paused = !paused;
            if (paused) {
                moveTimer.stop();
                clockTimer.stop();
            } else {
                moveTimer.start();
                clockTimer.start();
                // frame.setTitle("贪吃蛇");
            }
            return; // 不再处理方向键
        }

        if (paused || !running) return;

        Direction newDir = null;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:    newDir = Direction.UP; break;
            case KeyEvent.VK_DOWN:  newDir = Direction.DOWN; break;
            case KeyEvent.VK_LEFT:  newDir = Direction.LEFT; break;
            case KeyEvent.VK_RIGHT: newDir = Direction.RIGHT; break;
        }
        if (newDir != null && !isOpposite(currentDir, newDir)) {
            pendingDir = newDir;
        }
    }
}