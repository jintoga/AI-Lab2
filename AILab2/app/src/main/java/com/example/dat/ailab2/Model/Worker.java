package com.example.dat.ailab2.Model;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created by DAT on 26-Dec-15.
 */
public class Worker {

    //public MFrame frame;
    public char c[] = new char[3];
    public boolean a[][] = new boolean[6][6];
    public double errGraph[];
    public int exactIter = 0;

    public int middleLayer = 100;
    public double bet = 1;
    public double alp = 1e-1;
    public int maxIter = 10000;
    public double maxErr = 2;

    int x[][] = new int[100][36];
    int q[] = new int[100];
    int z = 0;

    double xin[][];
    double xout[][];
    double w[][][];
    double de[][];


    int lcnt = 3;
    int layer[] = {36, 0, 3};
    double[] tmp = new double[3];

    public String out = "";
    Context context;

    public Worker(Context context) {
        this.context = context;
    }

    public void apply(int type) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                x[z][i * 6 + j] = (a[i][j] ? 1 : 0);
            }
        }
        q[z] = type;
        z++;
    }

    public void precalc() {
        Random rand = new Random();
        xin = new double[lcnt][];
        xout = new double[lcnt][];
        de = new double[lcnt][];
        w = new double[lcnt][][];
        errGraph = new double[maxIter];
        exactIter = maxIter;

        for (int i = 0; i < lcnt; i++) {
            xin[i] = new double[layer[i]];
            xout[i] = new double[layer[i]];
            de[i] = new double[layer[i]];
            if (i != 0) {
                w[i] = new double[layer[i - 1]][layer[i]];
                for (int j = 0; j < layer[i - 1]; j++) {
                    for (int k = 0; k < layer[i]; k++) {
                        w[i][j][k] = ((rand.nextBoolean()) ? -1 : 1) *
                                rand.nextDouble();
                    }
                }
            }
        }

        for (int ci = 0; ci < maxIter; ci++) {
            double sumErr = 0;
            for (int cv = 0; cv < z; cv++) {
                getP(x[cv], tmp);
                for (int i = 0; i < layer[lcnt - 1]; i++) {
                    double cerr = xout[lcnt - 1][i] - ((i == q[cv]) ? 1 : 0);
                    de[lcnt - 1][i] = cerr;
                    sumErr += cerr * cerr / 2;
                }
                for (int cl = lcnt - 1; cl > 0; cl--) {
                    for (int i = 0; i < layer[cl - 1]; i++)
                        de[cl - 1][i] = 0;
                    for (int i = 0; i < layer[cl]; i++) {
                        double y = de[cl][i];
                        y *= bet * xout[cl][i] * (1 - xout[cl][i]);
                        de[cl][i] = y;
                        for (int j = 0; j < layer[cl - 1]; j++) {
                            de[cl - 1][j] += y * w[cl][j][i];
                            w[cl][j][i] -= y * alp;
                        }
                    }
                }
            }
            errGraph[ci] = sumErr;
            if (sumErr < maxErr) {
                exactIter = ci + 1;
                break;
            }
        }

    }

    public void getP(int[] cur, double[] ret) {
        for (int i = 0; i < layer[0]; i++)
            xout[0][i] = cur[i];
        for (int cl = 1; cl < lcnt; cl++) {
            for (int i = 0; i < layer[cl]; i++) {
                xin[cl][i] = 0;
                for (int j = 0; j < layer[cl - 1]; j++) {
                    xin[cl][i] += w[cl][j][i] * xout[cl - 1][j];
                }
                xout[cl][i] = 1. / (1. + Math.exp(-bet * xin[cl][i]));
            }
        }
        for (int i = 0; i < layer[lcnt - 1]; i++)
            ret[i] = xout[lcnt - 1][i];
    }

    public void scan(double[] ret) {
        apply(-1);
        z--;
        getP(x[z], ret);
    }

    public void analyse() {


        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(context.getAssets().open("input.txt"), "UTF-8"));
            String s = in.readLine();
            for (int i = 0; i < 3; i++)
                c[i] = s.charAt(i);
            int n = Integer.parseInt(in.readLine());

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < 6; j++) {
                    s = in.readLine();
                    for (int k = 0; k < 6; k++) {
                        a[j][k] = s.charAt(k) == '#';
                    }
                }
                int type = Integer.parseInt(in.readLine()) - 1;
                apply(type);
                //iterate();
            }
            in.close();
            layer[1] = middleLayer;
        } catch (IOException e) {
            Log.e("Er", e.getMessage());
        }

    }


}


