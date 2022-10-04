package erabbit.oauth.web.controller;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * @Author 111
 * @Date 2022/6/15 12:30
 * @Description 多线程写入文件
 */
public class TestFileLock {

    public static void main(String[] args) {


        CountDownLatch countDownLatch1 = new CountDownLatch(0);
        CountDownLatch countDownLatch2 = new CountDownLatch(1);
        CountDownLatch countDownLatch3 = new CountDownLatch(1);
        CountDownLatch countDownLatch4 = new CountDownLatch(1);
        CountDownLatch countDownLatch5 = new CountDownLatch(1);

        ThreadWriterFileWithLock thread1 = new ThreadWriterFileWithLock("1", countDownLatch1, countDownLatch2);
        ThreadWriterFileWithLock thread2 = new ThreadWriterFileWithLock("1001",countDownLatch2,countDownLatch3);
        ThreadWriterFileWithLock thread3 = new ThreadWriterFileWithLock("2001",countDownLatch3,countDownLatch4);
        ThreadWriterFileWithLock thread4 = new ThreadWriterFileWithLock("3001",countDownLatch4,countDownLatch5);
        ThreadWriterFileWithLock thread5 = new ThreadWriterFileWithLock("4001",countDownLatch5,countDownLatch5);
//        new ThreadWriterFileWithLock("5000").start();
        new Thread(thread1).start();
        new Thread(thread2).start();
        new Thread(thread3).start();
        new Thread(thread4).start();
        new Thread(thread5).start();
        try {
            countDownLatch5.await();
            System.out.println("写入文件结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected static class ThreadWriterFileWithLock implements Runnable{
        String threadName;

        CountDownLatch firstCount;
        CountDownLatch endCount;

        public ThreadWriterFileWithLock(String threadName,CountDownLatch firstCount,CountDownLatch endCount){
            this.threadName = threadName;
            this.firstCount = firstCount;
            this.endCount = endCount;
        }

        @Override
        public void run(){
            try {
                firstCount.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long start = System.currentTimeMillis();

            File file = new File("E:\\idea\\jmeter-test\\testUser.txt");
//            if (file.exists()) {
//                return;
//            }
            FileOutputStream outputStream = null;
            BufferedWriter bufferedWriter = null;
            FileChannel fileChannel = null;

            try {
                //写入数据
                outputStream = new FileOutputStream(file,true);
                //字节流变为字符流
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

                //对文件加锁
//                fileChannel = outputStream.getChannel();
//                FileLock fileLock = null;

//                int num = 0;
//                if ("444".equals(threadName))

//                fileLock = fileChannel.lock(1,Long.MAX_VALUE,false);
//                System.out.println(fileLock);
//                System.out.println(fileLock.isShared());
                int first = Integer.parseInt(threadName);
                int num = first + 999;
                for (int i = first; i <= num; i++) {
//                    sleep(1000);
                    bufferedWriter.write("user"+i+"\n");
                    bufferedWriter.flush();
                }

//                fileLock.release();


            }catch (Exception e){

                e.printStackTrace();
                System.out.println(threadName + "err");
            }finally {
                try {
                    bufferedWriter.close();
                    fileChannel.close();
                    outputStream.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }

            long end = System.currentTimeMillis();
            System.out.println(threadName + "写文件共花了" + (end - start) + "ms");
            endCount.countDown();
        }

    }

    public static class ThreadWriterFileWithoutLock extends Thread{

    }
}
