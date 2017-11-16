package com.example.sethcohen.muchotrabajo;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Marlboro.ttf");
        TextView textView = (TextView)findViewById(R.id.tv_company_name);
        textView.setTypeface(typeface);

        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "Marlboro.ttf");
        TextView textView1 = (TextView)findViewById(R.id.tv_slogan);
        textView1.setTypeface(typeface1);

        new AsyncTaskSplash().execute();
    }

    private class AsyncTaskSplash extends android.os.AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.currentThread();
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;

        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent i = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

    }

}
