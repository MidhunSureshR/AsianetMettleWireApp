package com.example.midhunsureshr.asianetmettle;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.webkit.WebView;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

class LoginInterface{

    private static final String preferenceName = "MyPrefsFile";
    private static final String preferenceUsername = "username";
    private static final String preferencePassword = "password";
    private WebView webView;

    LoginInterface(WebView web){
        webView = web;
    }

    class AutoLogin extends AsyncTask<Activity,Void,Activity> {

        private String username,password;

        protected Activity doInBackground(Activity... argumentActivity){
            SharedPreferences preference = argumentActivity[0].getSharedPreferences(preferenceName,MODE_PRIVATE);
            username = preference.getString(preferenceUsername, null);
            password = preference.getString(preferencePassword, null);
            return argumentActivity[0];
        }

        protected void onPostExecute(Activity argumentActivity){
            if(username!=null && password!=null) {
                if (!(username.isEmpty() && password.isEmpty())) {
                   // if(MainActivity.isLoaded) {
                        MainActivity.injectScript(webView, username, password);
                    //}
                }
            }
        }
    }

    void writeData(Activity argumentActivity){
        TextView usernameBox = argumentActivity.findViewById(R.id.usernameBox);
        TextView passwordBox = argumentActivity.findViewById(R.id.passwordBox);
        String username = usernameBox.getText().toString();
        String password = passwordBox.getText().toString();
        argumentActivity.getSharedPreferences(preferenceName,MODE_PRIVATE)
                .edit()
                .putString(preferenceUsername, username)
                .putString(preferencePassword, password)
                .apply();

    }
}
