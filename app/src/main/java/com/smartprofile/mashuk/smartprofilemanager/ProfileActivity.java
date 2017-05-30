package com.smartprofile.mashuk.smartprofilemanager;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public  void startMethod(View v) {

        //Toast.makeText(this,"Start method",Toast.LENGTH_LONG).show();
        Intent i = new Intent(this,ProfileService.class);
        //Toast.makeText(this,"Start service",Toast.LENGTH_LONG).show();
        startService(i);
        //Toast.makeText(this,"Started",Toast.LENGTH_LONG).show();

    }
    public void stopMethod(View v) {
        Intent i = new Intent(this, ProfileService.class);
        stopService(i);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
