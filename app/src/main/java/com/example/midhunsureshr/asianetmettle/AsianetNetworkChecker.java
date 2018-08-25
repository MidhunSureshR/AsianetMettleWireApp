package com.example.midhunsureshr.asianetmettle;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.webkit.WebView;
import android.widget.Toast;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.io.IOException;

class AsianetNetworkChecker {

    private  WebView localWebview;

    AsianetNetworkChecker(WebView web) {
        localWebview = web;
    }

    class AsianetNetworkCheckerThread extends AsyncTask<Context, Void, Context> {
        private boolean success = true;

        protected Context doInBackground(Context... argumentContext) {
            Connection.Response testresponse = null;
            try {
                testresponse = Jsoup.connect("https://myaccount.adlkerala.com/login.php").execute();

            } catch (IOException exception) {
                if (exception.toString().contains("403")) {
                    success = false;
                }
            }
            return argumentContext[0];
        }

        protected void onPostExecute(Context argumentContext) {
            Activity myActivity = (Activity) argumentContext;
            if (!success) {
                new UserInterfaceManager().disableComponents(myActivity);
                if (argumentContext != null) {
                    Toast.makeText(argumentContext, "Error: Please Connect To Valid Asianet Network.", Toast.LENGTH_LONG).show();
                }
            } else {
                LoginInterface login = new LoginInterface(localWebview);
                LoginInterface.AutoLogin autoLoginTask = login.new AutoLogin();
                autoLoginTask.execute(myActivity);
            }

        }

    }
}