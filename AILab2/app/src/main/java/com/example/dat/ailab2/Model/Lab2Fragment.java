package com.example.dat.ailab2.Model;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.dat.ailab2.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

/**
 * Created by DAT on 11/21/2015.
 */
public class Lab2Fragment extends Fragment {


    @InjectViews({R.id.cb1, R.id.cb2, R.id.cb3, R.id.cb4, R.id.cb5, R.id.cb6
            , R.id.cb7, R.id.cb8, R.id.cb9, R.id.cb10, R.id.cb11, R.id.cb12
            , R.id.cb13, R.id.cb14, R.id.cb15, R.id.cb16, R.id.cb17, R.id.cb18
            , R.id.cb19, R.id.cb20, R.id.cb21, R.id.cb22, R.id.cb23, R.id.cb24
            , R.id.cb25, R.id.cb26, R.id.cb27, R.id.cb28, R.id.cb29, R.id.cb30
            , R.id.cb31, R.id.cb32, R.id.cb33, R.id.cb34, R.id.cb35, R.id.cb36})
    List<CheckBox> checkBoxes;

    List<Figure> listOfFigures;
    int Q;
    int Z;
    double[][] matrixK;      //значение потенц ф-й обуч выборки
    double[][] matrixL;      //обуч-е к-ты (лямбда)
    String[] ch_class;
    @InjectView(R.id.buttonCheck)
    Button buttonCheck;
    @InjectView(R.id.textViewResult)
    TextView textViewResult;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Lab 2");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lab2, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.buttonCheck)
    protected void check() {

        String checked = "";
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked()) {

                checked += checkBoxes.indexOf(checkBox) + " ";
            }
        }
        //Toast.makeText(this, checked, Toast.LENGTH_SHORT).show();

        if (initialize()) {
            checkResult();
        }

        //clearStuff();
    }

    private void clearStuff() {
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setChecked(false);
        }
    }

    private void checkResult() {
        boolean[] vec = getUserVec();
        Figure userObr = new Figure(vec, -1);

        double[] pot = new double[Q];

        for (int j = 0; j < Q; j++) {
            pot[j] = getPot(userObr, j);      // j - текущий класс
        }

        double maxValue = getMaxDouble(pot);
        int m = toList(pot).indexOf(maxValue);     ///класс получился

       /* Toast.makeText(this, "Это символ " + ch_class[m]
                + "\n\nПотенциалы:\nКласс " + ch_class[0] + " = " + pot[0]
                + "\nКласс " + ch_class[1] + " = " + pot[1]
                + "\nКласс " + ch_class[2] + " = " + pot[2]
                , Toast.LENGTH_LONG).show();*/
        textViewResult.setText("Это символ " + ch_class[m]
                        + "\n\nПотенциалы:\nКласс " + ch_class[0] + " = " + pot[0]
                        + "\nКласс " + ch_class[1] + " = " + pot[1]
                        + "\nКласс " + ch_class[2] + " = " + pot[2]
        );

    }

    private boolean[] getUserVec() {
        boolean[] vec = new boolean[36];

        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isChecked()) {
                vec[i] = true;
            }
        }

        return vec;
    }

    boolean doneInitializing = false;

    private boolean initialize() {
        getTeacher();       //подгружаем выборку
        getMatrixK();       //получаем матрицу потенциалов
        getMatrixL();       //получаем матрицу обучающих к-тов лямбда Q*Z. заполняем 0 и 1

        teaching();
        doneInitializing = true;
        return doneInitializing;
    }


    private double getPot(Figure o, int j) {
        double K = 0;
        for (int z = 0; z < listOfFigures.size(); z++) {
            K += matrixL[j][z] * listOfFigures.get(z).getK(o);
        }
        return K;
    }

    private boolean[] getVec(String vecString) {
        boolean[] vec = new boolean[vecString.length()];

        for (int i = 0; i < vecString.length(); i++) {
            if (vecString.charAt(i) == '0') {
                vec[i] = false;
            } else vec[i] = true;
        }

        return vec;
    }

    private double getPotTeaching(int i, int j) //i-текущий ветор, j - текущий класс
    {
        double K = 0;
        for (int z = 0; z < Z; z++) {
            K += matrixL[j][z] * matrixK[i][z];
        }
        return K;
    }

    private void getMatrixK() {
        matrixK = new double[Z][Z];
        for (int i = 0; i < Z; i++) {
            for (int j = 0; j < Z; j++) {
                matrixK[i][j] = listOfFigures.get(i).getK(listOfFigures.get(j));
            }
        }
    }

    private void getMatrixL() {
        Random rand = new Random();
        matrixL = new double[Q][Z];
        for (int i = 0; i < Q; i++) {
            for (int j = 0; j < Z; j++) {
                matrixL[i][j] = rand.nextInt(2);
            }
        }
        Log.d("matrixL", matrixL.toString());
    }

    private void getTeacher() {
        listOfFigures = new ArrayList<>();
        TypedArray typedArray = getResources().obtainTypedArray(R.array.figures_list);
        int n = typedArray.length();
        String[][] array_classes = new String[n][];
        for (int i = 0; i < typedArray.length(); i++) {
            int resourceID = typedArray.getResourceId(i, 0);
            if (resourceID > 0) {
                array_classes[i] = getResources().getStringArray(resourceID);

                for (int j = 1; j < array_classes[i].length; j++) {
                    Figure figure = new Figure();
                    figure.setName(array_classes[i][0]);
                    array_classes[i][j] = array_classes[i][j].replaceAll("\\s", "");
                    boolean[] vec = getVec(array_classes[i][j]);
                    figure.setVect(vec);
                    listOfFigures.add(figure);
                }

            } else {
                Log.e("Error", "Res ERROR");
            }
        }
        Z = listOfFigures.size();
        Q = 3;
        ch_class = getClassNames();
        Log.d("classes", listOfFigures.toString());
        vectorTo2DArray();
        typedArray.recycle();
    }


    private ArrayList<double[][]> vectorTo2DArray() {

        ArrayList<double[][]> matrixList = new ArrayList<>();
        for (int ind = 0; ind < listOfFigures.size(); ind++) {
            Figure figure = listOfFigures.get(ind);
            double[][] matrix = new double[6][6];
            int vecIndex = 0;
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    matrix[i][j] = figure.getVectDouble()[vecIndex];
                    vecIndex++;
                }
            }
            matrixList.add(matrix);
            Log.d("classes", figure.getVectDouble().toString());
        }
        Log.d("classes", matrixList.toString());
        return matrixList;
    }

    private void teaching() {
        boolean flag = true;

        while (flag) {
            flag = false;
            for (int i = 0; i < Z; i++) {
                Figure curO = listOfFigures.get(i);
                double[] pot = new double[Q];

                for (int j = 0; j < Q; j++) {
                    pot[j] = getPotTeaching(i, j);      //i-текущий ветор, j - текущий класс
                }

                double maxValue = getMaxDouble(pot);
                int m = toList(pot).indexOf(maxValue);
                //int m = Arrays.asList(pot).indexOf(maxValue);     ///класс получился
                int p = curO.getClassN();                        //класс, к-й должен быть

                if (m != p) {
                    matrixL[p][i] += 1;      //корректируем
                    matrixL[m][i] -= 1;
                    flag = true;
                }


            }

        }
    }

    private List<Double> toList(double[] pot) {
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < pot.length; i++) {
            list.add(pot[i]);
        }
        return list;
    }

    private double getMaxDouble(double[] array) {
        double max = Double.MIN_VALUE;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    private String[] getClassNames() {
        String[] names = getResources().getStringArray(R.array.class_names);
        return names;
    }

}
