package com.example.dat.ailab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @InjectViews({R.id.cb1, R.id.cb2, R.id.cb3, R.id.cb4, R.id.cb5, R.id.cb6
            , R.id.cb7, R.id.cb8, R.id.cb9, R.id.cb10, R.id.cb11, R.id.cb12
            , R.id.cb13, R.id.cb14, R.id.cb15, R.id.cb16, R.id.cb17, R.id.cb18
            , R.id.cb19, R.id.cb20, R.id.cb21, R.id.cb22, R.id.cb23, R.id.cb24
            , R.id.cb25, R.id.cb26, R.id.cb27, R.id.cb28, R.id.cb29, R.id.cb30
            , R.id.cb31, R.id.cb32, R.id.cb33, R.id.cb34, R.id.cb35, R.id.cb36})
    List<CheckBox> checkBoxes;

    @InjectView(R.id.buttonCheck)
    Button buttonCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);


    }

    @OnClick(R.id.buttonCheck)
    protected void check() {

        String checked = "";
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked()) {

                checked += checkBoxes.indexOf(checkBox) + " ";
            }
        }
        Toast.makeText(this, checked, Toast.LENGTH_SHORT).show();
    }
}
