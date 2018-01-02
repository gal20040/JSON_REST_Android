package ru.gal20040.json_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    final String REG_EXP = "[^A-Za-z0-9.]";

    final String LOG_TAG = "myLogs";
    final String ERROR_MESSAGE = String.format("Error! Look in logs with tag=%s", LOG_TAG);
    private JSONObject jsonObject = new JSONObject();
    private Random random = new Random();

    TextView resultStringTextView;
    EditText newValueET, newNameET;
    Button addItemBtn;
    Button show_jsonobject_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        show_jsonobject_btn = findViewById(R.id.show_jsonobject_btn);

        resultStringTextView = findViewById(R.id.resultStringTextView);
        addItemBtn = findViewById(R.id.addItemBtn);
        addItemBtn.setEnabled(false);

        newValueET = findViewById(R.id.new_value);
        newValueET.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNonAlphaNumericalEditText(newValueET);

                boolean enabled = true;
                if (isStringNullOrEmpty(newValueET.getText().toString()))
                    enabled = false;
                addItemBtn.setEnabled(enabled);
            }
        });

        newNameET = findViewById(R.id.new_name);
        newNameET.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNonAlphaNumericalEditText(newNameET);

                //проверяем уникальность введённого имени и в зависимости от этого блокируем кнопку addItemBtn
                //сейчас эта проверка не нужна - в любом случае к введённому имени добавляем случайное число
                /*boolean enabled = true;
                if (!isNameUnique(newNameET.getText().toString()))
                    enabled = false;
                addItemBtn.setEnabled(enabled);*/
            }
        });
    }

    private void filterNonAlphaNumericalEditText(EditText editText) {
        if (getCurrentFocus() == editText) {
            String oldString = editText.getText().toString();
            String newString = oldString.replaceAll(REG_EXP, "");

            int cursorPosition = editText.getSelectionStart();
            //если мы убираем какой-то лишний знак из строки, то надо вернуть курсор на 1 знак левее
            if (!oldString.equals(newString))
                cursorPosition -= 1;

            editText.clearFocus();
            editText.setText(newString);
            editText.requestFocus();
            editText.setSelection(cursorPosition);
        }
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

    public void onNewRandBoolBtnClick(View view) {
        addNewJSONObject(random.nextBoolean(), null);
    }

    public void onNewRandDoubleBtnClick(View view) {
        addNewJSONObject(random.nextDouble(), null);
    }

    private void addNewJSONObject(Object newData, String newName) {
        newName = getRandomUniqueName(newData, newName);

        try{
            jsonObject.put(newName, newData);
            Log.i(LOG_TAG, String.format("json new %s = %s", newName, newData));
            resultStringTextView.setText(String.format("%s", newData));
        } catch (JSONException ex){
            errorAction(ex);
        }
    }

    /** Проверка строки на null и "" (пустая строка).
     * true вернётся, если строка равна null или "".
     * false - во всех других случаях.
     *
     * От ненужных пробелов (и всего прочего-ненужного) избавились в методах onTextChanged().
     */
    public boolean isStringNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /** Проверяет уникальность newName в рамках jsonObject.
     * Предварительно запускает проверку на null и "" через метод isStringNullOrEmpty().
     *
     * true вернётся, если (newName не null и не "") И (newName нет в составе jsonObject).
     * false - во всех других случаях.
     */
    public boolean isNameUnique(String newName) {
        return !isStringNullOrEmpty(newName)
                && !jsonObject.has(newName);
    }

    private String getRandomUniqueName(Object newData, String newName) {
        if (isStringNullOrEmpty(newName))
            newName = newData.getClass().toString().replace("class java.lang.", "");

        byte randomSeed = 100;
        int randomInt = random.nextInt(randomSeed);
        while (!isNameUnique(newName + randomInt))   //проверяем уникальность newName
            randomInt += random.nextInt(randomSeed); //подбираем уникальное имя
        newName += randomInt;

        return newName;
    }

    public void onClearJSONBtnClick(View view) {
        jsonObject = new JSONObject();
        onShowJSONObjectBtnClick(show_jsonobject_btn);
    }

    public void onShowJSONObjectBtnClick(View view) {
        resultStringTextView.setText(String.format("%s\nItem number in JSONObject = %s", jsonObject.toString(), jsonObject.length()));
    }

    public void onGetItemBtnClick(View view) {
        EditText editText = findViewById(R.id.jsonobject_name);
        String name = editText.getText().toString();
        try {
            String result;
            if (jsonObject.has(name))
                result = String.format("\"%s\":%s", name, jsonObject.get(name));
                //можно использовать методы getBoolean, getDouble, getInt, getLong, getString
                //также есть методы getJSONArray, getJSONObject
            else
                result = String.format("No item with name '%s'", name);
            resultStringTextView.setText(result);
        } catch (JSONException ex){
            errorAction(ex);
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
            resultStringTextView.setText(result);
        } catch (JSONException ex){
            errorAction(ex);
        }
    }

    private void errorAction(JSONException ex) {
        Log.d(LOG_TAG, getStackTrace(ex));
        resultStringTextView.setText(ERROR_MESSAGE);
    }

    public void onAddItemBtnClick(View view) {
        EditText new_value = findViewById(R.id.new_value);
        TextView new_name = findViewById(R.id.new_name);
        String name = new_name.getText().toString();
        String value = new_value.getText().toString();

        switch (value) {
            case ("true"):
                addNewJSONObject(Boolean.valueOf("true"), name);
                break;
            case ("false"):
                addNewJSONObject(Boolean.valueOf("false"), name);
                break;
            default:
                DoubleSafeParser doubleSafeParser = new DoubleSafeParser();
                Object parsedValue = doubleSafeParser.getDouble(value);
                if (parsedValue instanceof Double)
                    addNewJSONObject(parsedValue, name);
                else
                    //resultStringTextView.setText(String.format("Value in the field %s must be boolean (true, false) or double (0.123) type."), R.string.new_item_value);
                    resultStringTextView.setText("Value in the field \"New item value\" must be boolean (true, false) or double (0.123) type.");
                break;
        }
    }

    public void onAddRndJSONArrayBtnClick(View view) {
        String arrayName = "It's an array of double type numbers.";
        int arraySize = 3;
        Double[] doubleNumber = new Double[arraySize];

        for (int i = 0; i < arraySize; i++) {
            Double d = random.nextDouble();
            doubleNumber[i] = d;
        }

        JSONObject numbers = new JSONObject();
        JSONArray doubleArray = new JSONArray();

        try{
            numbers.put("array_name", arrayName);
            numbers.put("array_item_count", doubleNumber.length);

            for (Double aDoubleNumber : doubleNumber) {
                doubleArray.put(aDoubleNumber);
            }

            numbers.put("array_items", doubleArray);

            jsonObject.put("some_array" + random.nextInt(10), numbers);

            onShowJSONObjectBtnClick(show_jsonobject_btn);
        } catch (JSONException ex){
            errorAction(ex);
        }
    }
}