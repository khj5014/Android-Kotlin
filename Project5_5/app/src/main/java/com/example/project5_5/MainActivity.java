package com.example.project5_5;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText edit1, edit2;
    Button btnAdd, btnSub,btnMul, btnDiv, btnRem;
    TextView textResult;
    String num1, num2;
    Integer result;
    Button[] numButtons=new Button[10];
    Integer[] numBtnIDs={R.id.BtnNum0, R.id.BtnNum1, R.id.BtnNum2,
            R.id.BtnNum3, R.id.BtnNum4, R.id.BtnNum5, R.id.BtnNum6,
            R.id.BtnNum7, R.id.BtnNum8, R.id.BtnNum9};
    int i;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("그리드레이아웃 계산기");

        edit1 = (EditText) findViewById(R.id.Edit1);
        edit2 = (EditText) findViewById(R.id.Edit2);
        btnAdd = (Button) findViewById(R.id.BtnAdd);
        btnSub = (Button) findViewById(R.id.BtnSub);
        btnMul = (Button) findViewById(R.id.BtnMul);
        btnDiv = (Button) findViewById(R.id.BtnDiv);
        btnRem = (Button) findViewById(R.id.BtnRem);
        textResult = (TextView) findViewById(R.id.TextResult);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                num1 = edit1.getText().toString();
                num2 = edit2.getText().toString();

                if (num1.isEmpty() || num2.isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "빈칸 채워 주세요", Toast.LENGTH_SHORT).show();

                } else {
                    result = Integer.parseInt(num1) + Integer.parseInt(num2);
                    textResult.setText("더하기 계산 결과 : " + result.toString());
                }
            }

        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                num1 = edit1.getText().toString();
                num2 = edit2.getText().toString();

                if (num1.isEmpty() || num2.isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "빈칸 채워 주세요", Toast.LENGTH_SHORT).show();

                } else {
                    result = Integer.parseInt(num1) - Integer.parseInt(num2);
                    textResult.setText("빼기 계산 결과 : " + result.toString());
                }
            }

        });

        btnMul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                num1 = edit1.getText().toString();
                num2 = edit2.getText().toString();

                if (num1.isEmpty() || num2.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "빈칸 채워 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    result = Integer.parseInt(num1) * Integer.parseInt(num2);
                    textResult.setText("곱하기 계산 결과 : " + result.toString());
                }
            }


        });

        btnDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                num1 = edit1.getText().toString();
                num2 = edit2.getText().toString();

                if (num1.isEmpty() || num2.isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "빈칸 채워 주세요", Toast.LENGTH_SHORT).show();

                } else {
                    result = Integer.parseInt(num1) / Integer.parseInt(num2);
                    textResult.setText("나누기(몫) 계산 결과 : " + result.toString());
                }
            }

        });

        btnRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                num1 = edit1.getText().toString();
                num2 = edit2.getText().toString();

                if (num1.isEmpty() || num2.isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "빈칸 채워 주세요", Toast.LENGTH_SHORT).show();

                } else {
                    result = Integer.parseInt(num1) % Integer.parseInt(num2);
                    textResult.setText("나머지 계산 결과 : " + result.toString());
                }
            }

        });


        for (i = 0; i < numBtnIDs.length; i++)
        {
            numButtons[i] = (Button) findViewById(numBtnIDs[i]);
        }
        for (i = 0; i < numBtnIDs.length; i++) {
            final int index;
            index = i;

            numButtons[index].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (edit1.isFocused()) {
                        num1 = edit1.getText().toString()
                                + numButtons[index].getText().toString();
                        edit1.setText(num1);
                    } else if (edit2.isFocused()) {
                        num2 = edit2.getText().toString()
                                + numButtons[index].getText().toString();
                        edit2.setText(num2);
                    } else {
                        Toast.makeText(getApplicationContext(), "먼저 에디트텍스트를 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}