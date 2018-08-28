package com.example.midhunsureshr.asianetmettle;
// Imports
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.webkit.WebView;
import android.widget.Toast;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.io.IOException;
/**
 * This class contains code that checks whether the user is connected to asianet network.
 * @author      Midhun Suresh <rmidhunsuresh@outlook.com>
 * @version     1.0
 * @since       1.0
 */
class AsianetNetworkChecker {
    /**
     * Web-view which is passed on to auto-login class.
     */
    private  WebView localWebview;
    /**
     * Constructor to initialize the web-view.
     * @param web The web-view component.
     */
    AsianetNetworkChecker(WebView web) {
        localWebview = web;
    }

    /**
     * Async class to test if the user is connected to a valid asianet network
     * <p>We achieve this by trying to connect to the asianet mettle wire website. If
     * we get a 403 error we can conclude that we are not connected to the network.</p>
     * <p>Jsoup is used for this functionality.</p>
     * <p>Note: This way of using AsyncTask might theoretically lead to memory-leak however it makes
     * sense to ignore it here because after all this async-task only lasts for a couple of seconds.
     * </p>
     */
    class AsianetNetworkCheckerThread extends AsyncTask<Context, Void, Context> {
        /**
         * Boolean variable representing the network connection status.
         */
        private boolean success = true;

        protected Context doInBackground(Context... argumentContext) {
            //Connection.Response testresponse = null;
            try {
                 Jsoup.connect("https://myaccount.adlkerala.com/login.php").execute();

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