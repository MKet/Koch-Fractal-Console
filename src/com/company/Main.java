package com.company;

import calculate.Edge;
import calculate.KochFractal;
import timeutil.TimeStamp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class Main implements Observer {
    FileOutputStream fos;
    ObjectOutputStream oos;

    public static void main(String[] args) throws IOException {
	   Main main = new Main();
	   main.Stuff();
	   System.out.println("Successful");
    }

    public void Stuff() throws IOException {
        System.out.println("Koch level?!");

        Scanner scanner = new Scanner(System.in);
        int level = scanner.nextInt();

        System.out.println("Level " + level + " chosen.");

        calculateFractal(level);

    }

    public void BunchOfStuff() throws IOException {
        for (int i = 1; i < 10; i++)
            calculateFractal(i);
    }

    public void calculateFractal(int level) throws IOException {
        fos = new FileOutputStream(""+level);
        oos = new ObjectOutputStream(fos);

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

        oos.close();
        time.setEnd();

        System.out.println(time.toString());
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            Edge edge = (Edge)arg;
            oos.writeObject(edge);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
