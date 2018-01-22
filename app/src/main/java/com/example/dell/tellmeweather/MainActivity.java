package com.example.dell.tellmeweather;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static android.R.id.message;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MainActivity extends AppCompatActivity {

    EditText mEditText;
    TextView mTextViewDetail;
    public void getData(String url)
    {
        Ion.with(this)
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {

                    //data has arrived
                    @Override
                    public void onCompleted(Exception e, String result) {
                        try {
                            String message="";
                            JSONObject json = new JSONObject(result);

                            //Log.d("json data", String.valueOf(json));

                            String weatherPart = json.getString("weather");
                            Log.d("json data", weatherPart);
                            JSONArray ar = new JSONArray(weatherPart);
                            for(int i =0;i <ar.length();i++)
                            {
                                String main="";
                                String desc="";
                                JSONObject jobj = ar.getJSONObject(i);
                                main = jobj.getString("main");
                                desc = jobj.getString("description");
                                if(main!="" && desc!="")
                                {
                                    message+=main+": "+desc+"\r\n";
                                }
                                if(message!="")
                                {
                                    mTextViewDetail.setText(message);
                                }
                                else
                                {
                                    mTextViewDetail.setText(message);
                                    Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG).show();
                                }
                            }

                        } catch (JSONException e1) {
                            mTextViewDetail.setText("");
                            Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.editText);
        mTextViewDetail = (TextView) findViewById(R.id.textView) ;


       // getData("http://api.openweathermap.org/data/2.5/weather?q=washington&APPID=d42a97d7f02fb2a4eec9bc7cf6dfd407");
    }

    public void FindWeather(View view) {
        //Log.d("burron","button clicked");
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(mEditText.getWindowToken(),0);

        try {
            String encodedString = URLEncoder.encode(mEditText.getText().toString(),"utf-8");
            getData("http://api.openweathermap.org/data/2.5/weather?q="+encodedString+"&APPID=d42a97d7f02fb2a4eec9bc7cf6dfd407");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            mTextViewDetail.setText("");
            Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG).show();

        }

    }
}
