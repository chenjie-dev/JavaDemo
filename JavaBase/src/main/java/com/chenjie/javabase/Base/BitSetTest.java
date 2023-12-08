package com.chenjie.javabase.Base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.BitSet;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

public class BitSetTest {

    public static void main(String[] args) throws Exception {
        // 创建一个 BitSet 对象，用于存储整数
        BitSet bitSet = new BitSet();
        // 打开 txt 文件
        Scanner scanner = new Scanner(new File("D:\\workspace\\chenjie-dev\\JavaDemo\\JavaBase\\src\\main\\java\\com\\chenjie\\javabase\\Base\\random-numbers.txt"));
        // 读取 txt 文件中的每一行
        while (scanner.hasNextLine()) {
            System.out.println(">>>>  读取 txt 文件中的每一行  <<<<");

            // 将每一行中的数字拆分成数组
            String line = scanner.nextLine();
            StringTokenizer tokenizer = new StringTokenizer(line, ",");
            // 将数组中的每个数字添加到 BitSet 对象中
            while (tokenizer.hasMoreTokens()) {
                int num = Integer.parseInt(tokenizer.nextToken());
                if (num < 0) {
                    num = num * -1;
                }
                bitSet.set(num);
            }
        }
        // 关闭 txt 文件
        scanner.close();

        System.out.println("数据个数：" + bitSet.stream().count());

        // 打印 BitSet 对象中没有出现过的整数
        System.out.println("没有出现过的整数：");
        for (int i = 0; true; i++) {
            if (!bitSet.get(i)) {
                System.out.println(i);
            }
        }
    }


//    public static void main(String[] args) throws IOException {
//
//        // 创建一个文件写入对象
//        FileWriter fileWriter = new FileWriter("D:\\workspace\\chenjie-dev\\JavaDemo\\JavaBase\\src\\main\\java\\com\\chenjie\\javabase\\Base\\random-numbers.txt");
//        // 创建一个字符串缓冲区，用于批量写入数字
//        StringBuilder stringBuilder = new StringBuilder();
//
//        for (int i = 0; i < Integer.MAX_VALUE; i++) {
//            stringBuilder.append(i).append(",");
//            // 每 10 个数字换行
//            if (i % 100 == 99) {
//                stringBuilder.append("\n");
//            }
//            // 每 1000 个数字进行一次批量写入操作
//            if (i % 1000 == 999) {
//                System.out.println(">>>>  每 1000 个数字进行一次批量写入操作  <<<<");
//                fileWriter.write(stringBuilder.toString());
//                stringBuilder.setLength(0); // 清空字符串缓冲区
//            }
//        }
//        // 将剩余的数字写入文件
//        fileWriter.write(stringBuilder.toString());
//        // 关闭文件写入对象
//        fileWriter.close();
//    }
}
