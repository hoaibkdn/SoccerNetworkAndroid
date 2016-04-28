package fs.tandat.soccernetwork;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import fs.tandat.soccernetwork.helpers.UserHelper;

public class LoginActivity extends AppCompatActivity {
    public static final String PREFERENCE_KEY = "Prefs";
    public static String LOGIN_KEY = "username";
    private SharedPreferences loginPrefs;
    private SharedPreferences.Editor loginEditor;

    Button btLogin;
    Button btRegister;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btLogin = (Button) findViewById(R.id.btLogin);
        btRegister = (Button) findViewById(R.id.btRegister);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });


        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(in);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void checkLogin() {
        EditText txtUsername = (EditText) findViewById(R.id.txtUssername);
        EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
        String username = txtUsername.getText().toString();
        UserHelper userHelper = new UserHelper(LoginActivity.this);
        String password = txtPassword.getText().toString();
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            if (username.trim().isEmpty()) {
                txtUsername.setError("Username is empty");
            }
            if (password.trim().isEmpty()) {
                txtPassword.setError("Password is empty");
            }
        } else {
            if (userHelper.checkUser(username, password)) {
                Log.d("LOGIN", username + " " + password);
                Intent in = new Intent(LoginActivity.this, MainActivity.class);
                in.putExtra("username", username);
//                loginPrefs = getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
//                loginEditor = loginPrefs.edit();
//                loginEditor.putString(LOGIN_KEY, username);
//                loginEditor.commit();

//
                startActivity(in);
            }
        }

    }

}
