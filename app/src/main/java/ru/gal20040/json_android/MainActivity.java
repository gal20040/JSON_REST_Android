package ru.gal20040.json_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
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

//    private Double getDoubleJson() {
//        double result = 0;
//        try{
//            jsonObject = new JSONObject("{dollar:54.48}");
//            result = jsonObject.getDouble("dollar");
//            Log.i(LOG_TAG, "json double");
//            Log.i(LOG_TAG, "Курс доллара: " + result);
//        } catch (JSONException ex){
//            Log.d(LOG_TAG, getStackTrace(ex));
//        }
//
//        return result;
//    }

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
        boolean newBoolean = random.nextBoolean();
        try{
            jsonObject.put("boolean", newBoolean);
            Log.i(LOG_TAG, "json new boolean = " + newBoolean);
        } catch (JSONException ex){
            Log.d(LOG_TAG, getStackTrace(ex));
        }

        resultTextView.setText(String.format("%s", newBoolean));
    }

    public void onLastBoolBtnClick(View view) {
    }

    public void onNewDoubleBtnClick(View view) {
        Double newDouble = random.nextDouble();
        try{
            jsonObject.put("double", newDouble);
            Log.i(LOG_TAG, "json new double = " + newDouble);
        } catch (JSONException ex){
            Log.d(LOG_TAG, getStackTrace(ex));
        }

        resultTextView.setText(String.format("%s", newDouble));
    }

    public void onLastDoubleBtnClick(View view) {
    }
}