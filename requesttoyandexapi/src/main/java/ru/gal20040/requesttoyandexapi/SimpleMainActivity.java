package ru.gal20040.requesttoyandexapi;//package ru.gal20040.requesttoyandexapi;
//
//import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.JsonReader;
//import android.view.View;
//import android.widget.Button;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import javax.net.ssl.HttpsURLConnection;
//
////https://code.tutsplus.com/tutorials/android-from-scratch-using-rest-apis--cms-27117
//
//public class MainActivity extends AppCompatActivity {
//
//    private final String LOG_TAG = "myLogs";
//    private final String ERROR_MESSAGE = String.format("Error! Look in logs with tag=%s", LOG_TAG);
//
//    final String restURL = "https://api.github.com/";
//    Button getData;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        getData = findViewById(R.id.getServiceDataBtn);
//        getData.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                sendRequest();
//            }
//        });
//    }
//
//    private void sendRequest() {
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                URL githubEndpoint = null;
//
//                try {
//                    // Create URL
//                    githubEndpoint = new URL(restURL);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//
//                if (githubEndpoint != null) {
//                    HttpsURLConnection myConnection = null;
//                    try {
//                        // Create connection
//                        myConnection = (HttpsURLConnection) githubEndpoint.openConnection();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    if (myConnection != null) {
//                        myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");
//                        myConnection.setRequestProperty("Accept", "application/vnd.github.v3+json");
//                        myConnection.setRequestProperty("Contact-Me", "gal20040@gmail.com");
//
//                        try {
//                            if (myConnection.getResponseCode() == 200) {
//                                // Success
//                                InputStream responseBody = myConnection.getInputStream();
//                                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
//                                JsonReader jsonReader = new JsonReader(responseBodyReader);
//
//                                String value = readJsonObject(jsonReader);
//                                jsonReader.close();
//                            } else {
//                                // Error handling code goes here
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        myConnection.disconnect();
//                    }
//                }
//            }
//        });
//    }
//
//    private String readJsonObject(JsonReader jsonReader) {
//        String desiredKey = "organization_url";
//        try {
//            jsonReader.beginObject(); // Start processing the JSON object
//            while (jsonReader.hasNext()) { // Loop through all keys
//                String key = jsonReader.nextName(); // Fetch the next key
//                if (key.equals(desiredKey)) {
//                    // Fetch the value as a String
//                    return jsonReader.nextString();
////                    break; // Break out of the loop
//                } else {
//                    jsonReader.skipValue(); // Skip values of other keys
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}