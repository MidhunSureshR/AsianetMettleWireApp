package com.example.midhunsureshr.asianetmettle;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.webkit.WebView;
import android.widget.Toast;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

class InternetChecker {

    private WebView localWebview;

    InternetChecker(WebView web) {
        localWebview = web;
    }

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
                AsianetNetworkChecker asianetNetworkChecker = new AsianetNetworkChecker(localWebview);
                AsianetNetworkChecker.AsianetNetworkCheckerThread asianetThread = asianetNetworkChecker.new AsianetNetworkCheckerThread();
                asianetThread.execute(argumentContext);
            }

        }
    }

}
