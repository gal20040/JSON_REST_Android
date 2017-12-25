package ru.gal20040.json_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";
    private JSONObject jsonObject = new JSONObject();
    private Random random = new Random();

    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.resultStringTextView);
    }

    private static String getStackTrace(Exception ex) {
        StringBuilder sb = new StringBuilder(500);
        StackTraceElement[] st = ex.getStackTrace();
        sb.append(ex.getClass().getName()).append(": ").append(ex.getMessage()).append("\n");
        for (StackTraceElement aSt : st) {
            sb.append("\t at ").append(aSt.toString()).append("\n");
        }
        return sb.toString();
    }

    public void onNewBoolBtnClick(View view) {
        Boolean newBoolean = random.nextBoolean();
        addNewJSONObject(newBoolean, newBoolean.getClass().toString());
    }

    public void onNewDoubleBtnClick(View view) {
        Double newDouble = random.nextDouble();
        addNewJSONObject(newDouble, newDouble.getClass().toString());
    }

    public void onLastBoolBtnClick(View view) {
    }

    public void onLastDoubleBtnClick(View view) {
    }

    private void addNewJSONObject(Object newData, String dataType) {
        dataType = dataType.replace("class java.lang.", "");

        //ищем уникальный ключ
        byte randomSeed = 100;
        int randomInt = random.nextInt(randomSeed);
        try{
            while (jsonObject.has(dataType + randomInt))
                randomInt += random.nextInt(randomSeed);
            dataType += randomInt;
            jsonObject.put(dataType, newData);
            Log.i(LOG_TAG, String.format("json new %s = %s", dataType, newData));
            resultTextView.setText(String.format("%s", newData));
        } catch (Exception ex){
            Log.d(LOG_TAG, getStackTrace(ex));
        }
    }

    public void onClearJSONBtnClick(View view) {
        jsonObject = new JSONObject();
    }

    public void onShowJSONObjectBtnClick(View view) {
        resultTextView.setText(jsonObject.toString());
    }
}