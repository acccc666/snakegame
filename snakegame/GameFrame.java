import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private GameController controller;
    private JLabel scoreLabel;
    private JLabel timeLabel;

    public GameFrame() {
        setTitle("贪吃蛇");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // 1. 创建游戏面板（但先不设置控制器）
        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(
                Constants.COLS * Constants.CELL_SIZE,
                Constants.ROWS * Constants.CELL_SIZE
        ));
        gamePanel.setFocusable(true);

        // 2. 创建控制面板及其中的标签（确保 scoreLabel 已初始化）
        JPanel controlPanel = new JPanel(new FlowLayout());
        scoreLabel = new JLabel("得分: 0");
        timeLabel = new JLabel("时间: 0 s");
        JButton restartBtn = new JButton("重新开始");
        controlPanel.add(scoreLabel);
        controlPanel.add(timeLabel);
        controlPanel.add(restartBtn);

        // 3. 创建控制器（此时 scoreLabel 已可用）
        controller = new GameController(gamePanel, this);
        gamePanel.addKeyListener(controller);
        restartBtn.addActionListener(e -> controller.resetGame());

        // 4. 将组件添加到窗口
        add(gamePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    public void updateScore(int score) {
        scoreLabel.setText("得分: " + score);
    }

    public void updateTime(long seconds) {
        timeLabel.setText("时间: " + seconds + " s");
    }
}