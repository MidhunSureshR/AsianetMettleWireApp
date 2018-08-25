package com.example.midhunsureshr.asianetmettle;

import android.app.Activity;


class UserInterfaceManager{

    void disableComponents(Activity argumentActivity){
        argumentActivity.findViewById(R.id.btnLogin).setEnabled(false);
        argumentActivity.findViewById(R.id.usernameBox).setEnabled(false);
        argumentActivity.findViewById(R.id.passwordBox).setEnabled(false);
        argumentActivity.findViewById(R.id.saveBox).setEnabled(false);
    }

    void enableComponents(Activity argumentActivity){
        argumentActivity.findViewById(R.id.btnLogin).setEnabled(true);
        argumentActivity.findViewById(R.id.usernameBox).setEnabled(true);
        argumentActivity.findViewById(R.id.passwordBox).setEnabled(true);
        argumentActivity.findViewById(R.id.saveBox).setEnabled(true);
    }

}
