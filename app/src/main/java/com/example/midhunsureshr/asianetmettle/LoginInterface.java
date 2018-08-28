package com.example.midhunsureshr.asianetmettle;

// Import statements
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.webkit.WebView;
import android.widget.TextView;
import static android.content.Context.MODE_PRIVATE;
/**
 * This class deals primarily with the auto-login functionality.
 * In addition, it also houses the method which writes the user credentials into a preference file.
 * @author      Midhun Suresh <rmidhunsuresh@outlook.com>
 * @version     1.0
 * @since       1.0
 */
class LoginInterface{
    /**
     * Name of preference file to which the credentials are written.
     */
    private static final String preferenceName = "MyPrefsFile";
    /**
     * The key with which the username is associated in the preference file.
     */
    private static final String preferenceUsername = "username";
    /**
     * The key with which the password is associated in the preference file.
     */
    private static final String preferencePassword = "password";
    /**
     * The web-view component on which injectScript is called.
     */
    private WebView webView;

    /**
     * Constructor which initializes the web-view component webView.
     * @param web web-view component
     */
    LoginInterface(WebView web){
        webView = web;
    }

    /**
     * AsyncTask which deals with the auto-login functionality.
     * <p>Note: This way of using AsyncTask might theoretically lead to memory-leak however it makes
     * sense to ignore it here because after all this async-task only lasts for a couple of seconds.
     * </p>
     */
    class AutoLogin extends AsyncTask<Activity,Void,Activity> {
        /**
         * The username related to the account.
         */
        private String username;
        /**
         * The password related to the account.
         */
        private String password;

        protected Activity doInBackground(Activity... argumentActivity){
            SharedPreferences preference = argumentActivity[0].getSharedPreferences(preferenceName,MODE_PRIVATE);
            username = preference.getString(preferenceUsername, null);
            password = preference.getString(preferencePassword, null);
            return argumentActivity[0];
        }

        protected void onPostExecute(Activity argumentActivity){
            if(username!=null && password!=null) {
                if (!(username.isEmpty() && password.isEmpty())) {
                        MainActivity.injectScript(webView, username, password);
                }
            }
        }
    }

    /**
     * This method saves the username and password to the preference file.
     * @param argumentActivity The activity that contains the UI components.
     */
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
