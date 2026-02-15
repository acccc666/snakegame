import java.awt.Point;
import java.util.LinkedList;

public class Snake {
    private LinkedList<Point> body;

    public Snake() {
        body = new LinkedList<>();
        // 初始蛇：头(10,16)，身(10,15)，尾(10,14)
        body.add(new Point(10, 16));
        body.add(new Point(10, 15));
        body.add(new Point(10, 14));
    }

    public LinkedList<Point> getBody() {
        return body;
    }

    public Point getHead() {
        return body.getFirst();
    }

    // 普通移动：添加新头，移除尾
    public void move(Point newHead) {
        body.addFirst(newHead);
        body.removeLast();
    }

    // 吃食物时移动：添加新头，保留尾（长度+1）
    public void moveAndGrow(Point newHead) {
        body.addFirst(newHead);
        // 不移除尾部，长度增加
    }
}