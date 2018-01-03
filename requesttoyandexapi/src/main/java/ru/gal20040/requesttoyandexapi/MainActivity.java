package ru.gal20040.requesttoyandexapi;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

//https://code.tutsplus.com/tutorials/android-from-scratch-using-rest-apis--cms-27117
//https://developer.android.com/reference/android/util/JsonReader.html

//https://tech.yandex.ru/translate/doc/dg/reference/getLangs-docpage/

public class MainActivity extends AppCompatActivity {

    @SuppressWarnings("FieldCanBeLocal")
    private final String LOG_TAG = "myLogs";
    private final short RESPONSE_FOR_SUCCESSFUL_HTTP_REQUESTS = 200;
    @SuppressWarnings("FieldCanBeLocal")
    private final byte pauseForServerResponse = 50;
    private String responseAsString;

    private final String apiKeyAdditionTemplate = "?key="; //[key=<API-ключ>]
    private final String apiKeyYandexTranslate = "trnsl.1.1.20180102T000656Z.94e24b28bb2be45d.75eada6d438957deaa2eea52d51c83121739c2ba";
    private final String restURL = "https://translate.yandex.net/api/v1.5/tr.json/getLangs"
            .concat(apiKeyAdditionTemplate)
            .concat(apiKeyYandexTranslate);
    @SuppressWarnings("FieldCanBeLocal")
    private final String langAdditionTemplate = "&ui="; //[ui=<код языка>]

    TextView resultStringTextView;
    Spinner spinnerLangFrom, spinnerLangTo;

    String[] languages = {"русский", "английский", "польский"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultStringTextView = findViewById(R.id.resultStringTextView);

        // spinner адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLangFrom = findViewById(R.id.langFrom);
        spinnerLangTo = findViewById(R.id.langTo);

        spinnerLangFrom.setAdapter(adapter);
        spinnerLangTo.setAdapter(adapter);

//        spinner.setPrompt("Выбор языка"); // заголовок
        spinnerLangFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
        //todo вытащить в отдельный общий метод для spinnerLangFrom и spinnerLangTo
        spinnerLangTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
    }

    private void errorAction(Exception ex) {
        Log.d(LOG_TAG, getStackTrace(ex));
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

    public void onGetListSupportedLanguagesBtnClick(View view) {
        String reqUrl = restURL
                .concat(langAdditionTemplate)
                .concat("ru");

        sendRequest(reqUrl);

        while(true) {
            /*метод sendRequest выполняется в асинхронном режиме,
            поэтому метод onShowJSONObjectBtnClick, следующий следом, выполняется раньше,
            чем заполняется responseAsString, поэтому пришлось вставить такой костыль.
            todo найти вариант вывода ответа сервера без этого костыля - Thread.sleep(50);*/
            try {
                if (responseAsString != null) {
                    onShowJSONObjectBtnClick();
                    responseAsString = null;
                    break;
                }
                Thread.sleep(pauseForServerResponse);
            } catch (InterruptedException e) {
                errorAction(e);
            }
        }
    }

    private void sendRequest(final String reqUrl) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                responseAsString = null;

                URL serverEndpoint;
                try {
                    serverEndpoint = new URL(reqUrl);
                } catch (MalformedURLException e) {
                    errorAction(e);
                    return;
                }

                HttpsURLConnection myConnection;
                try {
                    myConnection = (HttpsURLConnection) serverEndpoint.openConnection();
                } catch (IOException e) {
                    errorAction(e);
                    return;
                }

                try {
                    if (myConnection.getResponseCode() == RESPONSE_FOR_SUCCESSFUL_HTTP_REQUESTS) {
                        InputStream responseBody = myConnection.getInputStream();
                        responseAsString = convertInputStreamToString(responseBody);
                    } else {
                        throw new ConnectException(
                                String.format(
                                        "myConnection.getResponseCode()=%s",
                                        myConnection.getResponseCode()
                                )
                        );
                    }
                } catch (Exception e) {
                    errorAction(e);
                } finally {
                    myConnection.disconnect();
                }
            }
        });
    }

    private String convertInputStreamToString(InputStream is) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        try {
            while ((length = is.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
        } catch (IOException e) {
            errorAction(e);
        }

        try {
            return result.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            errorAction(e);
            return null;
        }
    }

    public void onShowJSONObjectBtnClick() {
        final byte indent = 3;
        try {
            JSONObject jsonObject = new JSONObject(responseAsString);
            resultStringTextView.setText(jsonObject.toString(indent));
        } catch (JSONException e) {
            errorAction(e);
        }
    }
}