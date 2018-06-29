package com.example.android.jerry.journalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.jerry.R;
import com.example.android.jerry.journalapp.sqlitehelper.LogTableHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    EditText texttitle, summary;
    Button save;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.additem);

        texttitle = (EditText) findViewById(R.id.texttitle);
        summary = (EditText) findViewById(R.id.editsummary);

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            goToLogin();
        }else {
            FirebaseUser user = mAuth.getCurrentUser();
            TextView textView = findViewById(R.id.user_name);
            String name = user.getDisplayName() + "\n" + " " + user.getEmail();
            textView.setText(name);
        }
    }

    private void goToLogin() {
        Intent login = new Intent(this,LoginActivity.class);
        login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if(id== R.id.menu_logout) {
            if(mAuth.getCurrentUser() != null) {
                mAuth.signOut();
                goToLogin();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == save) {
            if (texttitle.getText().toString().length() > 0 && summary.getText().toString().length() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateandTime = sdf.format(new Date());
                LogTableHelper log = new LogTableHelper(getApplicationContext(), "log.db", null, 1);
                log.insertLog(texttitle.getText().toString(), summary.getText().toString(), currentDateandTime);
                Toast.makeText(getApplicationContext(), currentDateandTime, Toast.LENGTH_LONG).show();

                finish();
                Intent i = new Intent(getApplicationContext(), MenuScreen.class);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Summary or Text is missing", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onBackPressed() {
        finish();
        Intent i = new Intent(getApplicationContext(), MenuScreen.class);
        startActivity(i);
    }

}
