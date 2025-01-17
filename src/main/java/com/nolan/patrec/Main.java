package com.nolan.patrec;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {

    private static DataSet getTrainDataSet() throws IOException {
        Class clazz = Main.class;
        InputStream images = clazz.getResourceAsStream("/train-images.idx3-ubyte");
        InputStream labels = clazz.getResourceAsStream("/train-labels.idx1-ubyte");
        return new DataSet(images, labels);
    }

    private static DataSet getTestDataSet() throws IOException {
        Class clazz = Main.class;
        InputStream images = clazz.getResourceAsStream("/t10k-images.idx3-ubyte");
        InputStream labels = clazz.getResourceAsStream("/t10k-labels.idx1-ubyte");
        return new DataSet(images, labels);
    }

    public static Perceptron trainPerceptron() throws IOException {
        DataSet trainDataSet = getTrainDataSet();
        DataSet testDataSet = getTestDataSet();
        int[] layerSizes = new int[] {
                trainDataSet.height * trainDataSet.width, // input layer
                300,    // hidden layer
                100,    // hidden layer
                10      // output layer
        };
        Perceptron perceptron = new Perceptron(layerSizes, 0.05);
        trainDataSet.train(perceptron, 10);
        testDataSet.test(perceptron);
        return perceptron;
    }

    public static void main(final String... args) throws IOException {
        new Window();
//        trainPerceptron().save(new FileOutputStream("perceptron.data"));
    }
}
