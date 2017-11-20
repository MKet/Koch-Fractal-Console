package com.company;

import calculate.Edge;
import calculate.KochFractal;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

public class Main implements Observer {
    FileOutputStream fos;
    ObjectOutputStream oos;

    public static void main(String[] args) throws IOException {
	   Main main = new Main();
	   main.Stuff();
	   main.Close();

	   System.out.println("Successful");
    }

    public void Stuff() throws IOException {
        System.out.println("Koch level?!");
        char read = (char) System.in.read();

        int level = Integer.parseInt(""+ read);

        System.out.println("Level " + level + " chosen.");

        fos = new FileOutputStream("edges.kfr");
        oos = new ObjectOutputStream(fos);

        KochFractal fractal = new KochFractal(level);
        fractal.addObserver(this);

        System.out.println("generating bottom edge");
        fractal.generateBottomEdge();
        System.out.println("generating right edge");
        fractal.generateRightEdge();
        System.out.println("generating left edge");
        fractal.generateLeftEdge();
    }

    public void Close() throws IOException {
        oos.close();
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
