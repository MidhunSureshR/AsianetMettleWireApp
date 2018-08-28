package com.example.midhunsureshr.asianetmettle;
//Import Statements
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.webkit.WebView;
import android.widget.Toast;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
/**
 * This class contains code that checks whether the user is connected to the internet.
 * @author      Midhun Suresh <rmidhunsuresh@outlook.com>
 * @version     1.0
 * @since       1.0
 */
class InternetChecker {
    /**
     * The web-view component which will be passed on to next class.
     */
    private WebView localWebview;
    /**
    Constructor to initialize the web-view from the main activity.
     @param web The web-view component.
     */
    InternetChecker(WebView web) {
        localWebview = web;
    }

    /**
     * AsyncTask to check for internet connectivity using sockets.
     */
    class InternetCheckerThread extends AsyncTask<Context, Void, Context> {
        private boolean success;
        protected Context doInBackground(Context... argumentContext) {
            try {
                int timeoutMs = 1500;
                Socket sock = new Socket();
                SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);
                sock.connect(socketAddress, timeoutMs);
                sock.close();
                success = true;
            } catch (IOException exception) {
                success = false;
            }
            return argumentContext[0];
        }

        protected void onPostExecute(Context argumentContext) {
            if (!success) {
                Toast.makeText(argumentContext, "Error: Internet Connection Problem", Toast.LENGTH_LONG).show();
                Activity a = (Activity) argumentContext;
                new UserInterfaceManager().disableComponents(a);
            } else {
                /*Now that we know that a valid internet connection is available,
                lets make sure that a valid asianet network is present. The asianet mettle wire website
                will not load unless you are connected to a asianet network.*/
                AsianetNetworkChecker asianetNetworkChecker = new AsianetNetworkChecker(localWebview);
                AsianetNetworkChecker.AsianetNetworkCheckerThread asianetThread = asianetNetworkChecker.new AsianetNetworkCheckerThread();
                asianetThread.execute(argumentContext);
            }

        }
    }

}
