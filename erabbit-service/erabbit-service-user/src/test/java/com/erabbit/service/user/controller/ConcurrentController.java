package com.erabbit.service.user.controller;


import java.io.*;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author 111
 * @Date 2022/5/8 10:17
 * @Description
 */
public class ConcurrentController {

    //该字符串的内容将会被多线程写入到文件中
    static String CONTENS = "【作者】欧阳修 【朝代】宋 译文对照\n" +
            "欧阳子方夜读书，闻有声自西南来者，悚然而听之，曰：“异哉！”初淅沥以萧飒，忽奔腾而砰湃，如波涛夜惊，风雨骤至。其触于物也，鏦鏦铮铮，金铁皆鸣；又如赴敌之兵，衔枚疾走，不闻号令，但闻人马之行声。予谓童子：“此何声也？汝出视之。”童子曰：“星月皎洁，明河在天，四无人声，声在树间。”\n" +
            "\n" +
            "予曰：“噫嘻悲哉！此秋声也，胡为而来哉？盖夫秋之为状也：其色惨淡，烟霏云敛；其容清明，天高日晶；其气栗冽，砭人肌骨；其意萧条，山川寂寥。故其为声也，凄凄切切，呼号愤发。丰草绿缛而争茂，佳木葱茏而可悦；草拂之而色变，木遭之而叶脱。其所以摧败零落者，乃其一气之余烈。夫秋，刑官也，于时为阴；又兵象也，于行用金，是谓天地之义气，常以肃杀而为心。天之于物，春生秋实，故其在乐也，商声主西方之音，夷则为七月之律。商，伤也，物既老而悲伤；夷，戮也，物过盛而当杀。”\n" +
            "\n" +
            "“嗟乎！草木无情，有时飘零。人为动物，惟物之灵；百忧感其心，万事劳其形；有动于中，必摇其精。而况思其力之所不及，忧其智之所不能；宜其渥然丹者为槁木，黟然黑者为星星。奈何以非金石之质，欲与草木而争荣？念谁为之戕贼，亦何恨乎秋声！”\n" +
            "\n" +
            "童子莫对，垂头而睡。但闻四壁虫声唧唧，如助予之叹息耶。";


    public static void await(CountDownLatch latch) {
        if (latch == null) {
            return;
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public static void main(String... args) {

        File file = getByName("D://ConcurrentWrite.txt");
        byte[] bytes = null;
        try {
            bytes = CONTENS.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        int len = bytes.length;
        System.out.println("len: " + len);
        int concurrentNum = 4;

        if (len < concurrentNum) {
            concurrentNum = 1;
        } else {
            concurrentNum = (len % concurrentNum == 0) ? concurrentNum : (concurrentNum + 1);
        }

        int size = len / concurrentNum;

        List<FileBlockWriter> writers = new ArrayList<FileBlockWriter>();

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(concurrentNum);

        for (int i = 0; i < concurrentNum; i++) {
            FileBlockWriter writer = null;

            int from = i * size;
            int sliceLen = 0;
            System.out.println(from);

            if (i == concurrentNum - 1) {
                sliceLen = bytes.length - i * size;
                System.out.println(sliceLen);
            } else {
                sliceLen = size;
            }

            byte[] slice = new byte[sliceLen];
            System.arraycopy(bytes, from, slice, 0, sliceLen);
            writer = new FileBlockWriter(file, from, sliceLen, slice, startLatch, endLatch);

            writers.add(writer);
        }

        for (FileBlockWriter writer : writers) {
            writer.start();
        }

        //调用countDown后，所有写入线程才真正开始写入工作
        startLatch.countDown();

        //等待所有线程写入完成
        await(endLatch);

        System.out.println("done !!! ");


    }


    private static File getByName(String path) {

        File file = new File(path);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new IllegalArgumentException("create file failed", e);
            }
        }

        if (file.isDirectory()) {
            throw new IllegalArgumentException("not a file");
        }

        return file;
    }


    /**
     * 该线程类负责将指定的内容(contents)写入文件（target）,
     * 写入的起始位置是：from，往后写的字节数目是：length
     */
    public static class FileBlockWriter extends Thread {

        private File target;
        private int from;
        private int length;
        private byte[] contents;
        private CountDownLatch start;
        private CountDownLatch end;

        public FileBlockWriter(File target, int from, int length, byte[] contents, CountDownLatch start, CountDownLatch end) {
            this.target = target;
            this.from = from;
            this.length = length;
            this.contents = contents;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {

            RandomAccessFile randFile = null;
            FileChannel channel = null;
            MappedByteBuffer mbb = null;
            FileLock fileLock = null;

            ConcurrentController.await(start);

            try {

                randFile = new RandomAccessFile(target, "rw");
                channel = randFile.getChannel();
                mbb = channel.map(FileChannel.MapMode.READ_WRITE, from, length);

                fileLock = channel.lock(from, length, true);

                while (fileLock == null || !fileLock.isValid()) {
                    fileLock = channel.lock(from, length, true);
                    System.out.print("锁无效，重复获取");
                }
                mbb.put(contents);
                mbb.force();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (OverlappingFileLockException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("程序设计不合理，加锁区域相互重叠");

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                release(fileLock);
                forceClose(mbb);
                close(channel, randFile);
            }

            end.countDown();
        }
    }


    private static void release(FileLock fileLock) {
        if (fileLock == null) {
            return;
        }

        try {
            fileLock.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void close(Closeable... closeables) {


        if (closeables == null || closeables.length == 0) {
            return;
        }

        for (Closeable closeable : closeables) {
            if (closeable == null) {
                continue;
            }
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 强制关闭MappedByteBuffer
     * @param mbb
     */
    private static void forceClose(MappedByteBuffer mbb) {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    Method getCleanerMethod = mbb.getClass().getMethod("cleaner", new Class[0]);
                    getCleanerMethod.setAccessible(true);
                    sun.misc.Cleaner cleaner = (sun.misc.Cleaner)
                            getCleanerMethod.invoke(mbb, new Object[0]);
                    cleaner.clean();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

}
