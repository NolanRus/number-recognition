package com.nolan.patrec;

import java.io.*;
import java.util.ArrayList;

class DataSet {
    private final Matrix[] images;
    private final int[] labels;
    public final int size;
    public final int width;
    public final int height;

    public DataSet(InputStream imagesInputStream, InputStream labelsInputStream) throws IOException {
        DataInputStream imagesDataInputStream = new DataInputStream(imagesInputStream);
        DataInputStream labelsDataInputStream = new DataInputStream(labelsInputStream);
        if (0x00000803 != imagesDataInputStream.readInt()) {
            throw new Error("Magic number doesn't match.");
        }
        if (0x00000801 != labelsDataInputStream.readInt()) {
            throw new Error("Magic number doesn't match.");
        }
        int numberOfImages = imagesDataInputStream.readInt();
        int numberOfLabels = labelsDataInputStream.readInt();
        if (numberOfImages != numberOfLabels) {
            throw new Error("Number of labels is not same as number of images.");
        }
        size = numberOfImages;
        images = new Matrix[size];
        labels = new int[size];
        height = imagesDataInputStream.readInt();
        width = imagesDataInputStream.readInt();
        for (int i = 0; i < numberOfImages; ++i) {
            ArrayList<Double> data = new ArrayList<>(height * width);
            for (int j = 0; j < height * width; ++j) {
                data.add((double) imagesDataInputStream.readUnsignedByte());
            }
            images[i] = new Matrix(height, width, data);
            labels[i] = labelsDataInputStream.readUnsignedByte();
        }
    }

    public void train(Perceptron perceptron, int numberOfEpochs) {
        for (int k = 0; k < numberOfEpochs; ++k) {
            int numberOfErrors = 0;
            for (int i = 0; i < size; ++i) {
                Matrix image = images[i];
                int label = labels[i];
                Matrix expectedOutput = new Matrix(1, 10, (ignore, j) -> j == label ? 1.0 : 0);
                Matrix output = perceptron.train(image, expectedOutput);
                if (Perceptron.getAnswer(output) != label) ++numberOfErrors;
            }
            System.out.println("Training error rate: " + (double) numberOfErrors / size);
        }
    }

    public void test(Perceptron perceptron) {
        int numberOfErrors = 0;
        for (int i = 0; i < size; ++i) {
            Matrix image = images[i];
            int label = labels[i];
            Matrix output = perceptron.run(image);
            if (Perceptron.getAnswer(output) != label) ++numberOfErrors;
        }
        System.out.println("Error rate: " + (double)numberOfErrors / size);
    }
}