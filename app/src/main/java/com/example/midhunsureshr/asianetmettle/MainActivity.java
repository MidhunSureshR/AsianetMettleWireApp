package com.example.midhunsureshr.asianetmettle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static volatile boolean isLoaded = false;
    static volatile boolean writePassword = true;
    static volatile boolean doLogin=true;
    static volatile boolean checkValidation = false;

    void validateLogin(WebView webView) {
        if (isLoaded) {
            if (!webView.getUrl().equals("https://myaccount.adlkerala.com/#home.php")) {
                Log.d("FetchUrl:", webView.getUrl());
                Toast.makeText(MainActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
            } else {
                if (writePassword) {
                        CheckBox checkBox = findViewById(R.id.saveBox);
                        LoginInterface l = new LoginInterface(webView);
                        if (checkBox.isChecked()) {
                            l.writeData(this);
                        }
                    }
                    setContentView(webView);
                    writePassword = false;
                    }
            checkValidation = false;
            }

        }

        static void injectScript(WebView webView, String username, String password) {

            webView.loadUrl("javascript:document.getElementById('username').value = '" + username + "';void(0);");
            webView.loadUrl("javascript:document.getElementById('password').value = '" + password + "';void(0);");
            webView.loadUrl("javascript:document.getElementById('form').submit();void(0);");
            checkValidation = true;
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            final WebView webView = new WebView(this);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("https://myaccount.adlkerala.com/login.php");
            final Button loginButton = findViewById(R.id.btnLogin);
            final TextView usernameBox = findViewById(R.id.usernameBox);
            final TextView passBox = findViewById(R.id.passwordBox);
            final Activity myActivity = this;
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isLoaded) {
                        injectScript(webView, usernameBox.getText().toString(), passBox.getText().toString());
                    }else {
                        Toast.makeText(getApplicationContext(), "Resource Not Loaded. Try in five seconds.", Toast.LENGTH_LONG).show();
                    }

                }
            });

            passBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        loginButton.performClick();
                        return true;
                    }
                    return false;
                }
            });


            webView.setWebViewClient(new WebViewClient() {
                @Override

                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    isLoaded = false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    isLoaded = true;
                    if(doLogin){
                        InternetChecker internetChecker = new InternetChecker(webView);
                        InternetChecker.InternetCheckerThread internetThread = internetChecker.new InternetCheckerThread();
                        internetThread.execute(myActivity);
                        doLogin = false;
                    }

                    if (checkValidation) {
                        validateLogin(webView);

                    }
                    webView.loadUrl("javascript:function removeDummy(){var elem = document.getElementsByTagName(\"header\");elem[0].parentNode.removeChild(elem[0]);return false;}removeDummy();void(0);");
                    }

            });

            final SwipeRefreshLayout mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
            mySwipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                           // setContentView(webView);
                            doLogin = true;
                            webView.reload();
                            /*InternetChecker internetChecker = new InternetChecker(webView);
                            InternetChecker.InternetCheckerThread internetTask = internetChecker.new InternetCheckerThread();
                            internetTask.execute(myActivity);*/
                            new UserInterfaceManager().enableComponents(myActivity);
                            mySwipeRefreshLayout.setRefreshing(false);
                        }
                    }
            );


        }

    }







