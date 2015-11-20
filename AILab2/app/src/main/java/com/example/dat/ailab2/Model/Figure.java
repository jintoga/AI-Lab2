package com.example.dat.ailab2.Model;

/**
 * Created by DAT on 11/4/2015.
 */
public class Figure {

    boolean[] vect;
    String name;
    int classN;

    public Figure() {
    }

    public Figure(boolean[] vect, int classN) {

        this.vect = vect;
        this.classN = classN;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (name.equals("E")) {
            setClassN(0);
        } else if (name.equals("Э")) {
            setClassN(1);
        } else if (name.equals("+")) {
            setClassN(2);
        }
    }

    public boolean[] getVect() {
        return vect;
    }

    public double[] getVectDouble() {
        double[] vectDouble = new double[this.vect.length];
        for (int i = 0; i < this.vect.length; i++) {
            if(this.vect[i]==true){
                vectDouble[i] = 1;
            }
            else {
                vectDouble[i] = 0;
            }
        }
        return vectDouble;
    }


    public void setVect(boolean[] vect) {
        this.vect = vect;
    }

    public int getMes(Figure o) {
        int res = 0;
        for (int i = 0; i < vect.length; i++) {
            if (vect[i] != o.vect[i])
                res++;
        }
        return res;
    }

    public double getK(Figure o)        //Потенциал
    {
        int mes = getMes(o);
        double K = 1.0D / (1 + mes * mes);               //можно добавить параметр!!!!!
        return K;

    }

    public int getClassN() {
        return classN;
    }

    public void setClassN(int classN) {
        this.classN = classN;
    }
}
