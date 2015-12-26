package com.example.dat.ailab2.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dat.ailab2.Model.Worker;
import com.example.dat.ailab2.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

/**
 * Created by DAT on 26-Dec-15.
 */
public class Lab4Fragment extends Fragment {
    @InjectViews({R.id.cb1, R.id.cb2, R.id.cb3, R.id.cb4, R.id.cb5, R.id.cb6
            , R.id.cb7, R.id.cb8, R.id.cb9, R.id.cb10, R.id.cb11, R.id.cb12
            , R.id.cb13, R.id.cb14, R.id.cb15, R.id.cb16, R.id.cb17, R.id.cb18
            , R.id.cb19, R.id.cb20, R.id.cb21, R.id.cb22, R.id.cb23, R.id.cb24
            , R.id.cb25, R.id.cb26, R.id.cb27, R.id.cb28, R.id.cb29, R.id.cb30
            , R.id.cb31, R.id.cb32, R.id.cb33, R.id.cb34, R.id.cb35, R.id.cb36})
    List<CheckBox> checkBoxes;

    @InjectView(R.id.graph)
    GraphView graph;
    @InjectView(R.id.buttonBuild)
    Button buttonBuild;
    @InjectView(R.id.buttonCheck)
    Button buttonCheck;

    @InjectView(R.id.editTextLayerSize)
    EditText editTextLayerSize;
    @InjectView(R.id.editTextMaxIter)
    EditText editTextMaxIter;
    @InjectView(R.id.editTextMaxError)
    EditText editTextMaxError;
    @InjectView(R.id.editTextAlpha)
    EditText editTextAlpha;
    @InjectView(R.id.progressBar)
    ProgressBar progressBar;

    @InjectView(R.id.textViewIters)
    TextView textViewIters;
    @InjectView(R.id.textViewMinVal)
    TextView textViewMinVal;
    Worker worker;


    CheckBox grid[][];

    boolean neuroReady = false;

    int n = -1;
    public double af[];

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Lab 4");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lab4, container, false);
        ButterKnife.inject(this, view);
        grid = convertListTo2DArray();
        worker = new Worker(getActivity());
        worker.analyse();
        graph.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        return view;
    }

    private CheckBox[][] convertListTo2DArray() {

        CheckBox[] array = new CheckBox[checkBoxes.size()];
        for (int i = 0; i < checkBoxes.size(); i++) {
            array[i] = checkBoxes.get(i);
        }
        int count = 0;
        CheckBox[][] array2D = new CheckBox[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (count == array.length)
                    break;
                array2D[i][j] = array[count];
                count++;
            }
        }
        return array2D;
    }

    @OnClick(R.id.buttonCheck)
    protected void check() {
        if (!neuroReady) {
            Toast.makeText(getActivity(), "No neural network!", Toast.LENGTH_SHORT).show();
            return;
        }
        double x[] = new double[3];

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                worker.a[i][j] = grid[i][j].isChecked();
            }
        }

        worker.scan(x);

        int id = 0;
        for (int i = 0; i < 3; i++)
            if (x[id] < x[i])
                id = i;
        String s = "";
        for (int i = 0; i < 3; i++)
            s = s + worker.c[i] + ": " + String.format("%.4f", x[i]) + ". ";

        Toast.makeText(getContext(), "Результат: " + worker.c[id] + "\n" + s, Toast.LENGTH_SHORT).show();

    }


    @OnClick(R.id.buttonBuild)
    protected void buildNetwork() {
        if (!editTextLayerSize.getText().toString().equals("")
                && !editTextMaxIter.getText().toString().equals("")
                && !editTextMaxError.getText().toString().equals("")
                && !editTextAlpha.getText().toString().equals("")) {

            worker.middleLayer = Integer.parseInt(editTextLayerSize.getText() + "");
            worker.maxIter = Integer.parseInt(editTextMaxIter.getText() + "");
            worker.maxErr = Double.parseDouble(editTextMaxError.getText() + "");
            worker.alp = Double.parseDouble(editTextAlpha.getText() + "");
            //Toast.makeText(getActivity(), worker.middleLayer + "\n" + worker.maxIter + "\n" + worker.maxErr + "\n" + worker.alp + "\n", Toast.LENGTH_SHORT).show();

            startBuilding();
            //drawGraph();
        } else {
            Toast.makeText(getActivity(), "Required input is empty!", Toast.LENGTH_SHORT).show();
        }
    }

    private void startBuilding() {

        graph.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                worker.precalc();
                return null;
            }

            @Override
            protected void onPostExecute(Void res) {
                neuroReady = true;
                af = worker.errGraph;
                n = worker.exactIter;
                progressBar.setVisibility(View.INVISIBLE);
                drawGraph();
            }
        }.execute();
    }


    private void drawGraph() {
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;
        if (n != -1) {
            graph.setVisibility(View.VISIBLE);
            graph.removeAllSeries();
            DataPoint[] dataPoints = new DataPoint[n];
            for (int i = 0; i < n; i++) {
                max = Math.max(af[i], max);
                min = Math.min(af[i], min);
            }

            for (int i = 0; i < n; i++) {
                DataPoint dataPoint = new DataPoint(i, af[i]);
                dataPoints[i] = dataPoint;
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(
                    dataPoints
            );
            series.setColor(getResources().getColor(R.color.colorPrimary));
            series.setTitle("series");
            graph.addSeries(series);
            textViewMinVal.setText("min:" + min);
            textViewIters.setText("iterations:" + n);
        }


    }

}
