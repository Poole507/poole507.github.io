package com.game.tankgame6;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class TankGame06 extends JFrame {
    MyPanel mp = null;
    static JFrame f = new JFrame("坦克大战");
    static JButton b1 = new JButton("新游戏");
    static JButton b2 = new JButton("继续上局游戏");
    static Scanner scanner = new Scanner(System.in);
    static String key = new String();
    static int GameDifficult;
    public static void main(String[] args) throws IOException {


        Box vBox = Box.createVerticalBox();
        ActionListener actionListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand();
                if (actionCommand.equals("新游戏"))
                {
                    key = "1";
                    JDialog jD = new JDialog(f, "关于新游戏", true);
                    JButton easyBu = new JButton("简单");
                    JButton commBu = new JButton("普通");
                    JButton difBu = new JButton("困难");
                    easyBu.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            GameDifficult = 1;
                            jD.setVisible(false);
                            jD.dispose();
                            f.setVisible(false);
                            f.dispose();
                            TankGame06 tankGame06 = new TankGame06();
                        }
                    });
                    commBu.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            GameDifficult = 2;
                            jD.setVisible(false);
                            jD.dispose();
                            f.setVisible(false);
                            f.dispose();
                            TankGame06 tankGame06 = new TankGame06();
                        }
                    });
                    difBu.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            GameDifficult = 3;
                            jD.setVisible(false);
                            jD.dispose();
                            f.setVisible(false);
                            f.dispose();
                            TankGame06 tankGame06 = new TankGame06();
                        }
                    });
                    Box horizontalBox = Box.createHorizontalBox();
                    horizontalBox.add(easyBu);
                    horizontalBox.add(commBu);
                    horizontalBox.add(difBu);
                    jD.add(horizontalBox);
                    jD.setBounds(400, 350, 300, 100);
                    jD.setVisible(true);
                }
                if(actionCommand.equals("继续上局游戏"))
                {
                    key = "2";
                    TankGame06 tankGame = new TankGame06();
                }
            }
        };
        b1.addActionListener(actionListener);
        b2.addActionListener(actionListener);
        vBox.add(b1);
        vBox.add(b2);
        f.setBounds(600, 300, 434, 227);
        BackGroundPanel backGroundPanel = new BackGroundPanel(ImageIO.read(new File("src/tankagme.gif")));
        backGroundPanel.add(vBox);
        f.add(backGroundPanel);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public TankGame06(){
        mp = new MyPanel(key, GameDifficult);
        Thread thread = new Thread(mp);
        thread.start();
        this.add(mp);

        this.setSize(1300,950);
        this.addKeyListener(mp);//监听事件
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        //在JFrname中增加相应关闭窗口的处理
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Recorder.keepRecord();
                System.exit(0);
            }
        });
    }
}
class BackGroundPanel extends JPanel{
    private Image backIcon;

    public BackGroundPanel(Image backIcon) {
        this.backIcon = backIcon;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backIcon, 0, 0, 434,227 , null);
    }
}