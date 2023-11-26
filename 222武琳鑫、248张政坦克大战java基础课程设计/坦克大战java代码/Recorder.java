package com.game.tankgame6;

import java.io.*;
import java.util.Vector;

public class Recorder {
    private static int allEnemyTankNum = 0;
    //定义io流，准备写数据到文件中
    private static BufferedWriter bw = null;
    private static BufferedReader br = null;
    private static String recordFile = "/Users/wulinxin/Desktop/myRecord.txt";
    private static Vector<EnemyTank> enemyTanks = null;
    //存放敌人的信息放在node里面
    private static Vector<Node> nodes = new Vector<>();

    public static String getRecordFile() {
        return recordFile;
    }

    public static void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        Recorder.enemyTanks = enemyTanks;
    }

    public static Vector<Node> getNodesAndEnemyTankRec()
    {
        try {
            br = new BufferedReader((new FileReader(recordFile)));
            allEnemyTankNum =Integer.parseInt(br.readLine());
            String line;
            while((line = br.readLine()) != null)
            {
                String[] xyd = line.split("  ");
                Node node = new Node(Integer.parseInt(xyd[0]), Integer.parseInt(xyd[1]),
                        Integer.parseInt(xyd[2]));
                nodes.add(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(br != null)
                {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return nodes;
    }
    public static void keepRecord()
    {
        try {
            bw = new BufferedWriter(new FileWriter(recordFile));
            bw.write(allEnemyTankNum + "\r\n");
            //保存敌人坦克坐标
            for(int i = 0; i < enemyTanks.size(); i++)
            {
                EnemyTank enemyTank = enemyTanks.get(i);
                if(enemyTank.isLive)
                {
                    String record = enemyTank.getX() + "  " + enemyTank.getY() + "  " + enemyTank.getDirect();
                    bw.write(record + "\r\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bw != null)
            {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static int getAllEnemyTankNum() {
        return allEnemyTankNum;
    }

    public static void setAllEnemyTankNum(int allEnemyTankNum) {
        Recorder.allEnemyTankNum = allEnemyTankNum;
    }

    public static void addAllEnemyTankNum()
    {
        Recorder.allEnemyTankNum++;
    }
}
