package edu.monash.fit2081.callsfilter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MyCallsReceiver extends BroadcastReceiver {
    Context self;
    static TelephonyManager telephonyManager;
    String incoming, code;
    int stateNo;

    public void onReceive(Context context, Intent intent) {


        self = context;

        if (telephonyManager == null) {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            MyPhoneStateListener PhoneListener = new MyPhoneStateListener();
            telephonyManager.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
            Log.i(MainActivity.TAG, "onReceive()");
        }

    }


    private class MyPhoneStateListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber) {
            Log.i(MainActivity.TAG, "state changed");

            Intent intent = new Intent("intent.filter.state");
            Intent newIntent = new Intent("intent.filter.data");

            stateNo = state;
            if (state == 1) {
                showToast("Number:=" + incomingNumber);
                incoming = incomingNumber;
                code = incomingNumber.substring(0,2);

                // create a new broadcast intent to pass data to MainActiity
//                Intent newIntent = new Intent("intent.filter.data");
                newIntent.putExtra("codeKey", code);
                newIntent.putExtra("phoneNoKey", incoming);
                intent.putExtra("stateKey", "Ringing");
                Log.i(MainActivity.TAG, "phone: " + incoming + " type: " + code + "state: " + String.valueOf(state));
                self.sendBroadcast(newIntent);
            }
            else if (state == 0) {
                intent.putExtra("stateKey", "Idle");
                self.sendBroadcast(intent);
                Log.i(MainActivity.TAG, "state: " + String.valueOf(state));
            }
            else {
                intent.putExtra("stateKey", "In call");
                self.sendBroadcast(intent);
                Log.i(MainActivity.TAG, "state: " + String.valueOf(state));
            }

        }
    }

    void showToast(String msg) {
        Toast.makeText(self, msg, Toast.LENGTH_LONG).show();
    }
}
