package com.example.midhunsureshr.asianetmettle;

//Import Statements
import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author      Midhun Suresh <rmidhunsuresh@outlook.com>
 * @version     1.0
 * @since       1.0
 */

public class MainActivity extends AppCompatActivity {
    /**
     * Boolean variable that signifies complete load of a web-page.
     */
    static volatile boolean isLoaded = false;
    /**
     * Boolean variable that decides whether the credentials are to be stored or not.
     */
    static volatile boolean writePassword = true;
    /**
     * Boolean variable that whose true state prompts auto login on page reload.
     */
    static volatile boolean doLogin=true;
    /**
     * Boolean variable whose true state prompts validation of a login attempt after page load
     */
    static volatile boolean checkValidation = false;

    /**
     * Checks for login success
     * <p>
     * Compares the url of the web-view to decide if the login was successful or not.
     * </p>
     * <p>
     * If successful, the credentials are stored for auto-login depending on check state of
     * the checkbox and the web-view is set as the content view.
     * </p>
     * <p>
     *  If unsuccessful, a toast message alerts the user that the credentials were wrong.
     * </p>
     *
     * @param  webView The web-view on which url comparison is done to determine successful login.
     */
    void validateLogin(WebView webView) {
        if (isLoaded) {
            if (!webView.getUrl().equals("https://myaccount.adlkerala.com/#home.php")) {
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
    /**
     * Log into Asianet mettle-wire website.
     * <p>
     *  Injects javascript code into the web-view to cause a login attempt.
     *  Sets the value of checkValidation to validate the attempt when response is
     *  loaded.
     * </p>
     *
     * @param  webView The web-view on which javascript is injected.
     * @param  username Username of the account.
     * @param  password Password of the account.
     */
    static void injectScript(WebView webView, String username, String password) {
        webView.loadUrl("javascript:document.getElementById('username').value = '" + username + "';void(0);");
        webView.loadUrl("javascript:document.getElementById('password').value = '" + password + "';void(0);");
        webView.loadUrl("javascript:document.getElementById('form').submit();void(0);");
        checkValidation = true;
    }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Our primary web-view component.
            final WebView webView = new WebView(this);
            //UI components
            final Button loginButton = findViewById(R.id.btnLogin);
            final TextView usernameBox = findViewById(R.id.usernameBox);
            final TextView passBox = findViewById(R.id.passwordBox);
            final Activity myActivity = this;
            //We do need javascript to interact with the website programmatically.
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("https://myaccount.adlkerala.com/login.php");
            //Login button click handler
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
            //Do login through the soft keyboard enter key.
            //Note: Does not seem to work with all android phones.
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

            //Web-view handles
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
                    //Remove navigation menu from the information page.
                    webView.loadUrl("javascript:function removeDummy(){var elem = document.getElementsByTagName(\"header\");elem[0].parentNode.removeChild(elem[0]);return false;}removeDummy();void(0);");
                    }

            });

            //Swipe to refresh handler.
            final SwipeRefreshLayout mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
            mySwipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            doLogin = true;
                            webView.reload();
                            new UserInterfaceManager().enableComponents(myActivity);
                            mySwipeRefreshLayout.setRefreshing(false);
                        }
                    }
            );


        }

    }







