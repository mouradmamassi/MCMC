package com.l3info.utf.mm.MC;

import android.app.Application;
import android.content.Intent;

import com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.ui.MainActivity;
import com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.utils.ParseConstant;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;

/**
 * Created by mm on 25/12/15.
 */
public class MCApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "TxboaHuImrtEMY5TQmFFggeXiHTnSYiqPNsOnPgw", "FErhfUkaYTau1ImExEWaBqkQNIAOLUYqnKLcNg4L");
        ParseInstallation.getCurrentInstallation().saveInBackground();




    }
    public static void updateParseInstallation(ParseUser user){
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(ParseConstant.KEY_USER_ID, user.getObjectId());
        installation.saveInBackground();
    }
}
