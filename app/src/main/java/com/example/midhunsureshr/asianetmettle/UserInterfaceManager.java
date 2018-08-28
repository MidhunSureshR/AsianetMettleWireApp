package com.example.midhunsureshr.asianetmettle;

import android.app.Activity;

/**
 * This class deals with disabling the user interface when there is a
 * connectivity issue.
 * @author      Midhun Suresh <rmidhunsuresh@outlook.com>
 * @version     1.0
 * @since       1.0
 */
class UserInterfaceManager{
    /**
     * Disables the UI components
     * @param argumentActivity The activity that contains the Ui components.
     */
    void disableComponents(Activity argumentActivity){
        argumentActivity.findViewById(R.id.btnLogin).setEnabled(false);
        argumentActivity.findViewById(R.id.usernameBox).setEnabled(false);
        argumentActivity.findViewById(R.id.passwordBox).setEnabled(false);
        argumentActivity.findViewById(R.id.saveBox).setEnabled(false);
    }
    /**
     * Enables the UI components
     * @param argumentActivity The activity that contains the Ui components.
     */
    void enableComponents(Activity argumentActivity){
        argumentActivity.findViewById(R.id.btnLogin).setEnabled(true);
        argumentActivity.findViewById(R.id.usernameBox).setEnabled(true);
        argumentActivity.findViewById(R.id.passwordBox).setEnabled(true);
        argumentActivity.findViewById(R.id.saveBox).setEnabled(true);
    }

}
