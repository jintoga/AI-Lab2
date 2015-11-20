package com.example.dat.ailab2.Model;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by DAT on 11/21/2015.
 */
public class Kinside {
    int countG1 = 0, countG2 = 0, countG3 = 0;
    ArrayList<double[][]> matrixList;
    double[][] G1 = new double[6][6];      //maxmin центр первого
    double[][] G2 = new double[6][6];      //maxmin центр 2-го
    double[][] G3 = new double[6][6];      //maxmin центр 3-го

    ArrayList<double[][]> matrixListForG1;
    ArrayList<double[][]> matrixListForG2;
    ArrayList<double[][]> matrixListForG3;

    double[][][] ForG1 = new double[30][][];
    double[][][] ForG2 = new double[30][][];
    double[][][] ForG3 = new double[30][][];

    double[][] newG1 = new double[6][6];      //K центр первого
    double[][] newG2 = new double[6][6];      //K центр 2-го
    double[][] newG3 = new double[6][6];

    ArrayList<double[][]> listG;

    public Kinside(ArrayList<double[][]> matrixList) {
        this.matrixList = matrixList;
        matrixListForG1 = matrixList;
        matrixListForG2 = matrixList;
        matrixListForG3 = matrixList;
    }

    public void firstStep() {
        countG1 = countG2 = countG3 = 0;
        //step 1
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                G1[i][j] = matrixList.get(0)[i][j];
            }
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                G2[i][j] = matrixList.get(1)[i][j];
            }
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                G3[i][j] = matrixList.get(2)[i][j];
            }
        }

        //step 2
        for (int i = 0; i < matrixList.size(); i++) {
            switch (MinOf3(mes(matrixList.get(i), G1), mes(matrixList.get(i), G2), mes(matrixList.get(i), G3))) {
                case 1:
                    ForG1[countG1] = matrixList.get(i);
                    countG1++;
                    break;
                case 2:
                    ForG2[countG2] = matrixList.get(i);
                    countG2++;
                    break;
                case 3:
                    ForG3[countG3] = matrixList.get(i);
                    countG3++;
                    break;
                default:
                    Log.e("Error", "Error");
                    break;
            }
        }
        Log.e("Passed", "Passed 1");
    }

    public ArrayList<double[][]> secondStep() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                newG1[i][j] = 0;
                newG2[i][j] = 0;
                newG3[i][j] = 0;

                for (int k = 0; k < countG1; k++) {
                    newG1[i][j] += ForG1[k][i][j];
                }

                for (int k = 0; k < countG2; k++) {
                    newG2[i][j] += ForG2[k][i][j];
                }

                for (int k = 0; k < countG3; k++) {
                    newG3[i][j] += ForG3[k][i][j];
                }

            }
        }
        listG = new ArrayList<>();
        listG.add(newG1);
        listG.add(newG2);
        listG.add(newG3);
        Log.e("Passed", "Passed 2");
        return listG;
    }



    int MinOf3(double q1, double q2, double q3) {
        double min = q1;
        int index = 1;

        if (q1 < min) {
            min = q1;
            index = 1;
        }
        if (q2 < min) {
            min = q2;
            index = 2;
        }
        if (q3 < min) {
            min = q3;
            index = 3;
        }

        return index;
    }

    double mes(double[][] a, double[][] b) {
        //Евклидовая мера сходства
        double sum = 0;
        for (int j = 0; j < 6; j++) {
            for (int k = 0; k < 6; k++) {
                sum += (a[j][k] - b[j][k]) * (a[j][k] - b[j][k]);
            }
        }
        return Math.sqrt(sum);
        //return sum;
    }
}
