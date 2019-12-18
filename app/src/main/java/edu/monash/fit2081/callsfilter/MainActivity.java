package edu.monash.fit2081.callsfilter;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "new";
    public static final String MOBILE = "04";

    TextView landlineCountTv, mobileCountTv, incomingNoTv;
    int landlineCount, mobileCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        landlineCountTv = findViewById(R.id.landlineCountTv);
        mobileCountTv = findViewById(R.id.mobileCountTv);
        incomingNoTv = findViewById(R.id.incomingNoTv);

        landlineCount = Integer.parseInt(landlineCountTv.getText().toString());
        mobileCount = Integer.parseInt(mobileCountTv.getText().toString());

        List<String> wantedPermissions = new ArrayList<>();

        // add permissions to wantedPermissions list
        wantedPermissions.add(Manifest.permission.CALL_PHONE);
        wantedPermissions.add(Manifest.permission.READ_CALL_LOG);
        wantedPermissions.add(Manifest.permission.READ_PHONE_STATE);
        wantedPermissions.add(Manifest.permission.PROCESS_OUTGOING_CALLS);

        // request the list of wantedPermissions for user to accept
        ActivityCompat.requestPermissions(this, wantedPermissions.toArray(new String[wantedPermissions.size()]), 0);

        // register an instance of the receiver created within this activity to subscribe
        // to a broadcast with an action string: "intent.filter.data"
        registerReceiver(new UpdateTvReceiver(), new IntentFilter("intent.filter.data"));


    }

    // this inner class is the broadcast receiver handler that is used get data from the
    // MyCallsReceiver and update the appropriate textviews
    class UpdateTvReceiver extends BroadcastReceiver {

        // this method will be invoked each time new broadcast is sent by the broadcast intent
        // with the action of "intent.filter.data"
        @Override
        public void onReceive(Context context, Intent intent) {
            // get the message and update the UI
            Log.i(TAG, "data received");

            // get the data that was passed with the intent
            String code = intent.getStringExtra("codeKey");
            String incomingNo = intent.getStringExtra("phoneNoKey");

            // if phoneNo starts with "04", update mobile count and its textview
            if (code.equals(MOBILE)) {
                mobileCount += 1;
                mobileCountTv.setText(String.valueOf(mobileCount));
                Log.i(TAG, "mobile count updated");

            }

            // else, the phoneNo is a landline
            // update landline count and its textview
            else {
                landlineCount += 1;
                landlineCountTv.setText(String.valueOf(landlineCount));
                Log.i(TAG, "landline count updated");
            }

            // update incoming no textview
            incomingNoTv.setText(incomingNo);

        }
    }
}
