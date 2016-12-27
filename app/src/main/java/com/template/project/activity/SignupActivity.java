package com.template.project.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.template.project.R;
import com.template.project.constants.URLConst;
import com.template.project.objects.GeneralPayload;
import com.template.project.objects.SignUpPayload;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;

import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

//    @Bind(R.id.input_name) EditText _nameText;
//    @Bind(R.id.input_email) EditText _emailText;
//    @Bind(R.id.input_password) EditText _passwordText;
//    @Bind(R.id.btn_signup) Button _signupButton;
//    @Bind(R.id.link_login) TextView _loginLink;
//    @Bind(R.id.progress)
AVLoadingIndicatorView _progressBar;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        _progressBar.hide();

//        _signupButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signup();
//            }
//        });
//
//        _loginLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Finish the registration screen and return to the Login activity
//                finish();
//            }
//        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


        if (!validate()) {
            onSignupFailed();
            return;
        }

//        _signupButton.setEnabled(false);
//        _emailText.setEnabled(false);
//        _nameText.setEnabled(false);
//        _passwordText.setEnabled(false);
//
//        _progressBar.setVisibility(View.VISIBLE);
//
//        String name = _nameText.getText().toString();
//        String email = _emailText.getText().toString();
//        String password = _passwordText.getText().toString();

        String url = URLConst.serverIp + "/poc-api/rest/UserService/signup";


        SignUpPayload signUpPayload = new SignUpPayload();
//        signUpPayload.setEmail(email);
//        signUpPayload.setPassword(password);
//        signUpPayload.setUsername(name);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = "";
        try {
            json = ow.writeValueAsString(signUpPayload);
        } catch (JsonProcessingException e) {
            Log.e("", "", e);
        }

        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.e("", "", e);
                SignupActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SignupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        onSignupFailed();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ObjectMapper mapper2 = new ObjectMapper();
                mapper2.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                final GeneralPayload generalPayload = mapper2.readValue(response.body().string(), GeneralPayload.class);


                if (generalPayload.getStatusCode() == 0) {

                    SignupActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            onSignupSuccess();
                        }
                    });

                } else {
                    SignupActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(SignupActivity.this, generalPayload.getStatusMessage(), Toast.LENGTH_LONG).show();
                            onSignupFailed();
                        }
                    });

                }
            }
        });


    }


    public void onSignupSuccess() {
//        _signupButton.setEnabled(true);
//
//        _emailText.setEnabled(true);
//        _nameText.setEnabled(true);
//        _passwordText.setEnabled(true);

//        _progressBar.setVisibility(View.GONE);

//        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {

//        _emailText.setEnabled(true);
//        _nameText.setEnabled(true);
//        _passwordText.setEnabled(true);
//
//        _progressBar.setVisibility(View.GONE);
//
//        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

       /* String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
*/
        return valid;
    }
}