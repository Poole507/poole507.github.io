package com.game.tankgame6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Vector;

public class MyPanel extends JPanel implements KeyListener, Runnable {
    Hero hero = null;
    Vector<EnemyTank> enemyTanks = new Vector<>();//存放敌人坦克
    Vector<Node> nodes = new Vector<>();
    Vector<Bomb> bombs = new Vector<>();//炸弹

    static int enemyTankSize = 9;
    static int enemyTankShot = 1;
    static int heroTankShot = 1;
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;

    public MyPanel(String key, int gameDifficulty)
    {
        File file = new File(Recorder.getRecordFile());
        if(file.exists())
        {
            nodes = Recorder.getNodesAndEnemyTankRec();
        }
        else
        {
            System.out.println("文件不存在，只能开启新的游戏");
            key = "1";
        }

        //将MyPanel对象的 enemyTanks 设置给Recorder的enemyTanks
        Recorder.setEnemyTanks(enemyTanks);
        hero = new Hero(500, 600);//自己的坦克
        //设置游戏难度
        switch (gameDifficulty)
        {
            case 1:
                enemyTankSize = 3;//坦克数量
                hero.setSpeed(10);//自己的坦克速度
                enemyTankShot = 1;
                heroTankShot = 3;
                break;
            case 2:
                enemyTankSize = 6;
                hero.setSpeed(8);
                enemyTankShot = 3;
                heroTankShot = 3;
                break;
            case 3:
                enemyTankSize = 9;
                hero.setSpeed(7);
                enemyTankShot = 8;
                heroTankShot = 3;
                break;
        }

        //hero.setSpeed(10);
        switch (key)
        {
            case "1"://重新开始游戏
                for(int i = 0; i < enemyTankSize; i++)//创建敌人坦克
                {
                    EnemyTank enemyTank = new EnemyTank(100 * (i + 1), 0);
                    if(gameDifficulty == 1)
                    {
                        enemyTank.setSpeed(3);//敌人坦克速度
                    }
                    else if(gameDifficulty == 2)
                    {
                        enemyTank.setSpeed(5);
                    }
                    else if(gameDifficulty == 3)
                    {
                        enemyTank.setSpeed(10);
                    }
                    enemyTank.setEnemyTanks(enemyTanks);
                    enemyTank.setDirect(2);
                    //启动坦克的线程,让他们动起来
                    new Thread(enemyTank).start();
                    Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
                    enemyTank.shots.add(shot);
                    new Thread(shot).start();
                    enemyTanks.add(enemyTank);
                }
                break;
            case  "2":
                for(int i = 0; i < nodes.size(); i++)//创建敌人坦克
                {
                    Node node = nodes.get(i);
                    EnemyTank enemyTank = new EnemyTank(node.getX(), node.getY());
                    enemyTank.setEnemyTanks(enemyTanks);
                    enemyTank.setDirect(node.getDirect());
                    //启动坦克的线程,让他们动起来
                    new Thread(enemyTank).start();
                    Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
                    enemyTank.shots.add(shot);
                    new Thread(shot).start();
                    enemyTanks.add(enemyTank);
                }
                break;
        }

        image1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));
        image2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
        image3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));

        //这里播放音乐
        new AePlayWave("src/111.wav").start();

    }

    public void showInfo(Graphics g)
    {
        g.setColor(Color.BLACK);
        Font font = new Font("宋体", Font.BOLD, 25);
        g.setFont(font);

        g.drawString("累积击毁敌方坦克",1020,30);
        drawTank(1020,60, g, 0, 1);
        g.setColor(Color.BLACK);
        g.drawString(Recorder.getAllEnemyTankNum() + "", 1080, 100);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, 1000, 750);//填充矩形，默认黑色
        showInfo(g);
        //自己的坦克
        if(hero != null && hero.isLive) {
            drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 0);
        }

        //画出子弹,只有一颗
//        if (hero.shot != null && hero.shot.isLive == true) {
//            //g.fill3DRect(hero.shot.x, hero.shot.y, 1, 1, false);
//            g.draw3DRect(hero.shot.x, hero.shot.y, 2, 2, false);
//        }
        //画出我方子弹，可以多颗
        for (int i = 0; i < hero.shots.size(); i++)
        {
            Shot shot = hero.shots.get(i);
            if(shot != null && shot.isLive)
            {
                g.draw3DRect(shot.x, shot.y, heroTankShot, 1, false);
            }
            else
            {
                hero.shots.remove(shot);
            }
        }
        //画出炸弹效果
        for(int i = 0; i < bombs.size(); i++)
        {
            Bomb bomb = bombs.get(i);
            if(bomb.life > 6)
            {
                g.drawImage(image1, bomb.x, bomb.y, 60, 60, this);
            }
            else if(bomb.life > 3)
            {
                g.drawImage(image2, bomb.x, bomb.y, 60, 60, this);
            }
            else
            {
                g.drawImage(image3, bomb.x, bomb.y, 60, 60, this);
            }
            bomb.lifeDown();
            if(bomb.life == 0)
            {
                bombs.remove(bomb);
            }
        }

        //画出敌方坦克和子弹
        for(int i = 0; i < enemyTanks.size(); i++)
        {
            EnemyTank enemyTank = enemyTanks.get(i);
            if(enemyTank.isLive) {//当敌人坦克是活才画
                drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirect(), 1);
                //画出敌人的子弹
                for (int j = 0; j < enemyTank.shots.size(); j++) {
                    Shot shot = enemyTank.shots.get(j);
                    if (shot.isLive) {
                        g.draw3DRect(shot.x, shot.y, enemyTankShot, 1, false);
                    } else {
                        enemyTank.shots.remove(shot);
                    }
                }
            }
        }
    }

    public void drawTank(int x, int y, Graphics g, int direct, int type)//画出坦克类型
    {
        switch (type)
        {
            case 0://我们的坦克
                g.setColor(Color.cyan);//青色
                break;
            case 1://敌人的坦克
                g.setColor(Color.red);//红色
                break;
        }
        switch (direct)
        {
            case 0://向上的为我们的坦克
                g.fill3DRect(x, y, 10, 60, false);//画出坦克左边的轮子
                g.fill3DRect(x + 30, y, 10, 60, false);//画出坦克右边的轮子
                g.fill3DRect(x + 10, y + 10, 20, 40, false);//画出坦克盘子
                g.fillOval(x + 10, y + 20, 20,20);//画出圆盖
                g.drawLine(x + 20, y  + 30, x + 20, y);
                break;
            case 1://向右
                g.fill3DRect(x, y, 60, 10, false);//画出坦克上边的轮子
                g.fill3DRect(x, y + 30, 60, 10, false);//画出坦克下边的轮子
                g.fill3DRect(x + 10, y + 10, 40, 20, false);//画出坦克盘子
                g.fillOval(x + 20, y + 10, 20,20);//画出圆盖
                g.drawLine(x + 30, y  + 20, x + 60, y + 20);
                break;
            case 2://向下
                g.fill3DRect(x, y, 10, 60, false);//画出坦克左边的轮子
                g.fill3DRect(x + 30, y, 10, 60, false);//画出坦克右边的轮子
                g.fill3DRect(x + 10, y + 10, 20, 40, false);//画出坦克盘子
                g.fillOval(x + 10, y + 20, 20,20);//画出圆盖
                g.drawLine(x + 20, y  + 30, x + 20, y + 60);
                break;
            case 3://向左
                g.fill3DRect(x, y, 60, 10, false);//画出坦克上边的轮子
                g.fill3DRect(x, y + 30, 60, 10, false);//画出坦克下边的轮子
                g.fill3DRect(x + 10, y + 10, 40, 20, false);//画出坦克盘子
                g.fillOval(x + 20, y + 10, 20,20);//画出圆盖
                g.drawLine(x + 30, y  + 20, x, y + 20);
                break;
            default:
                System.out.println("暂时没有处理");
        }
    }
    //判断多颗子弹是否击中敌人坦克
    public void hitEnemyTank() {
        for (int j = 0; j < hero.shots.size(); j++) {
            Shot shot = hero.shots.get(j);
            if (shot != null && shot.isLive) {
                for (int i = 0; i < enemyTanks.size(); i++) {
                    EnemyTank enemyTank = enemyTanks.get(i);
                    hitTank(shot, enemyTank);
                }
            }
        }
    }
    //判断是否击中我方坦克
    public void hitHero()
    {
        for(int i = 0; i < enemyTanks.size(); i++)
        {
            EnemyTank enemyTank = enemyTanks.get(i);
            for(int j = 0; j < enemyTank.shots.size(); j++)
            {
                Shot shot = enemyTank.shots.get(j);
                if(hero.isLive && shot.isLive)
                {
                    hitTank(shot, hero);
                }
            }
        }
    }
    //判断我方的子弹是否击中敌人坦克,---------------后面就变成了子弹是否击中坦克（无论我方还是敌方）
    public void hitTank(Shot s, tank enemyTank)
    {
        switch (enemyTank.getDirect())
        {
            case 0:
            case 2:
                if(s.x > enemyTank.getX() && s.x < enemyTank.getX() + 40
                && s.y > enemyTank.getY() && s.y < enemyTank.getY() + 60)
                {
                    s.isLive = false;
                    enemyTank.isLive = false;
                    enemyTanks.remove(enemyTank);
                    if(enemyTank instanceof EnemyTank)
                    {
                        Recorder.addAllEnemyTankNum();
                    }
                    Bomb bomb = new Bomb(enemyTank.getX(), enemyTank.getY());
                    bombs.add(bomb);
                }
                break;
            case 1:
            case 3:
                if(s.x > enemyTank.getX() && s.x < enemyTank.getX() + 60
                        && s.y > enemyTank.getY() && s.y < enemyTank.getY() + 40)
                {
                    s.isLive = false;
                    enemyTank.isLive = false;
                    enemyTanks.remove(enemyTank);
                    if(enemyTank instanceof EnemyTank)
                    {
                        Recorder.addAllEnemyTankNum();
                    }
                    Bomb bomb = new Bomb(enemyTank.getX(), enemyTank.getY());
                    bombs.add(bomb);
                }
                break;

        }
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }
    //wasd
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W){
            hero.setDirect(0);
            if(hero.getY() > 0) {
                hero.moveUp();
            }
        }else if(e.getKeyCode() == KeyEvent.VK_D){
            hero.setDirect(1);
            if(hero.getX() + 60 < 1000) {
                hero.moveRight();
            }
        }else if(e.getKeyCode() == KeyEvent.VK_S){
            hero.setDirect(2);
            if(hero.getY() + 60 < 750) {
                hero.moveDown();
            }
        }else if(e.getKeyCode() == KeyEvent.VK_A){
            hero.setDirect(3);
            if(hero.getX() > 0) {
                hero.moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_J)
        {
            //发射一颗子弹
//            if(hero.shot == null || !hero.shot.isLive) {
//                hero.shotEnemyTank();
//            }
            //发射多颗子弹
            hero.shotEnemyTank();
        }
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {//每隔100毫秒，重绘区域，子弹就会移动
        while(true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //判断是否击中了敌人坦克
            hitEnemyTank();
            //判断是否击中了我方坦克
            hitHero();
            this.repaint();
        }
    }
}
