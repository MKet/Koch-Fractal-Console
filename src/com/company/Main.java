package com.company;

import calculate.Edge;
import calculate.KochFractal;
import timeutil.TimeStamp;

import java.io.*;
import java.nio.Buffer;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class Main implements Observer {
    FileOutputStream fos;
    ObjectOutputStream oos;
    BufferedOutputStream buffer;
    PrintWriter p;
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
        fos = new FileOutputStream(""+level);
        buffer = new BufferedOutputStream(fos);
        p = new PrintWriter(buffer);

        TimeStamp time = new TimeStamp();
        time.setBegin("calculation start");
        KochFractal fractal = new KochFractal(level);
        fractal.addObserver(this);

        System.out.println("generating bottom edge");
        fractal.generateBottomEdge();
        System.out.println("generating right edge");
        fractal.generateRightEdge();
        System.out.println("generating left edge");
        fractal.generateLeftEdge();

        p.close();
        time.setEnd();

        System.out.println(time.toString());
    }

    @Override
    public void update(Observable o, Object arg) {
    //    try {
            Edge edge = (Edge)arg;
            p.print(edge.toString() + "\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
