package ru.gal20040.json_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
        } catch (JSONException ex){
            Log.d(LOG_TAG, getStackTrace(ex));
        }
    }

    public void onClearJSONBtnClick(View view) {
        jsonObject = new JSONObject();
    }

    public void onShowJSONObjectBtnClick(View view) {
        resultTextView.setText(jsonObject.toString());
    }

    public void onGetItemBtnClick(View view) {
        EditText editText = findViewById(R.id.jsonobject_name);
        String name = editText.getText().toString();
        try {
            String result;
            if (jsonObject.has(name))
                result = String.format("\"%s\":%s", name, jsonObject.get(name));
            else
                result = String.format("No item with name '%s'", name);
            resultTextView.setText(result);
        } catch (JSONException ex){
            Log.d(LOG_TAG, getStackTrace(ex));
        }
    }

    public void onRemoveItemBtnClick(View view) {
        EditText editText = findViewById(R.id.jsonobject_name);
        String name = editText.getText().toString();
        try {
            String result;
            if (jsonObject.has(name)) {
                result = String.format("\"%s\":%s has been removed", name, jsonObject.get(name));
                jsonObject.remove(name);
            }
            else
                result = String.format("No item with name '%s'", name);
            resultTextView.setText(result);
        } catch (JSONException ex){
            Log.d(LOG_TAG, getStackTrace(ex));
        }
    }
}