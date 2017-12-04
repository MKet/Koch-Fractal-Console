package com.company;

import calculate.Edge;
import calculate.KochFractal;
import sun.nio.ch.DirectBuffer;
import timeutil.TimeStamp;

import java.io.*;
import java.nio.Buffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class Main implements Observer {
    private MappedByteBuffer out;
    private static final int EDGE_SIZE = 7*8;
    private FileChannel fc;
    private FileLock lock;

    public static void main(String[] args) throws IOException {
	   Main main = new Main();
	   main.calculateFractal();
	   System.out.println("Successful");
    }

    public void calculateFractal() throws IOException {
        System.out.println("Koch level?!");

        Scanner scanner = new Scanner(System.in);
        int level = scanner.nextInt();

        System.out.println("Level " + level + " chosen.");

        calculateFractal(level);

    }

    public void calculateFractals() throws IOException {
        for (int i = 1; i < 10; i++)
            calculateFractal(i);
    }

    public void calculateFractal(int level) throws IOException {
        KochFractal fractal = new KochFractal(level);
        fractal.addObserver(this);

        File file = new File("../"+level);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        fc = randomAccessFile.getChannel();
        out = fc.map(FileChannel.MapMode.READ_WRITE, 0, EDGE_SIZE * fractal.getNrOfEdges()+4);
        lock = fc.lock(0, 4,false);
        out.putInt(0);

        TimeStamp time = new TimeStamp();
        time.setBegin("calculation start");
        System.out.println("generating bottom edge");
        fractal.generateBottomEdge();
        System.out.println("generating right edge");
        fractal.generateRightEdge();
        System.out.println("generating left edge");
        fractal.generateLeftEdge();
        randomAccessFile.close();

        time.setEnd("calculation end");
        System.out.println(time.toString());
    }

    @Override
    public void update(Observable o, Object arg) {
        Edge edge = (Edge) arg;
        try {
            long position = out.position();
            FileLock l = lock;
            lock = fc.lock(position, EDGE_SIZE, false);
            l.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.putDouble(edge.X1);
        out.putDouble(edge.Y1);
        out.putDouble(edge.X2);
        out.putDouble(edge.Y2);
        out.putDouble(edge.color.getRed());
        out.putDouble(edge.color.getBlue());
        out.putDouble(edge.color.getGreen());

        try {
            int position = out.position();
            FileLock l = fc.lock(0, 4,false);
            out.position(0);
            int amount = out.getInt();
            out.position(0);
            out.putInt(amount+1);
            out.position(position);
            l.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
