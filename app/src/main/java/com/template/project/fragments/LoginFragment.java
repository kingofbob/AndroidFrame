package com.template.project.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.template.project.BuildConfig;
import com.template.project.R;
import com.template.project.activity.LoginActivity;
import com.template.project.activity.MainActivity;
import com.template.project.base.BaseBackFragment;
import com.template.project.connections.LoginAPI;
import com.template.project.constants.ErrorCodes;
import com.template.project.constants.SystemConfigConst;
import com.template.project.objects.LoginResponsePayload;
import com.template.project.utils.GlobalConfig;
import com.template.project.utils.QuickUtils;
import com.template.project.utils.TinyDB;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.services.concurrency.AsyncTask;


public class LoginFragment extends BaseBackFragment implements Validator.ValidationListener{
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    protected LoginActivity mActivity;
    @NotEmpty(sequence = 1)
//    @CustomEmailPhoneValidator(sequence = 2, messageResId = R.string.invalid_email_phone)
    @Bind(R.id.contact)EditText _emailText;

    @NotEmpty(sequence = 1)
    @Length(sequence = 2, min = 4, max = 15, messageResId = R.string.invalid_password)
    @Bind(R.id.password)EditText _passwordText;

    @Bind(R.id.toolbar)Toolbar mToolbar;
    @Bind(R.id.new_account)TextView createAccountText;
    @Bind(R.id.forgotpassword)TextView forgotpasswordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.progress)AVLoadingIndicatorView _progress;
    @Bind(R.id.version)TextView versionText;

    private static final String ARG_PARAM1 = "param1";

    private int eventId = 0;
    private Validator validator;

    public static LoginFragment newInstance(int param1) {

        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalConfig.getInstance().getEventBus().register(this);

        if (getArguments() != null) {
            eventId = getArguments().getInt(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);


        validator = new Validator(this);
        validator.setValidationListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _progress.hide();
        initView();

    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {

    }




    public void initView(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (new TinyDB(getActivity()).getString(SystemConfigConst.FIRST_LAUNCH).length() <= 0) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.h_fragment_pop_enter, R.anim.h_fragment_pop_exit)
                            .replace(R.id.container, new AppTutorialFragment())
                            .addToBackStack(null)
                            .commit();

                }

            }
        }, 100);

        QuickUtils.setStatusBarColor(getActivity(), R.color.primary_darker);

//        initToolbarNav(mToolbar);
//         mToolbar.setTitle(getActivity().getResources().getString(R.string.login));


        QuickUtils.underlineText(createAccountText);
        QuickUtils.underlineText(forgotpasswordText);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                validator.validate();
            }

        });
//        _loginButton.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                onLoginSuccess();
//                return false;
//            }
//        });

        createAccountText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
//                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
//                startActivityForResult(intent, REQUEST_SIGNUP);
                start(SignUpFragment.newInstance(0));
            }
        });

        forgotpasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPasswordFragment.newInstance(0).show(getChildFragmentManager(),LoginFragment.this.getClass().getSimpleName());
            }
        });


            versionText.setText(BuildConfig.VERSION_NAME + " - " + BuildConfig.FLAVOR);



    }


    public void login() {
        Log.d(TAG, "Login");

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


        _loginButton.setEnabled(false);
        _emailText.setEnabled(false);
        _passwordText.setEnabled(false);
        _progress.show();

        String username = _emailText.getText().toString();
        String password = _passwordText.getText().toString();



        new LoginAPI(getActivity()).execute(username, password);

    }




    public void onLoginSuccess(Context context) {



        Log.d(LoginFragment.class.getSimpleName(), "Login Success process");
        _loginButton.setEnabled(true);
        _progress.hide();
        _emailText.setEnabled(true);
        _passwordText.setEnabled(true);
//        getActivity().finish();

//        Intent intent = new Intent(getActivity(), MainActivity.class);
//        startActivity(intent);



//        GlobalConfig.getInstance().getEventBus().post(new MainActivityAction(0));
//        EventMainAction eventAction = new EventMainAction();
//        eventAction.setAction(EventMainAction.REFRESH_UI);
//        eventAction.setActionBool(false);
//        GlobalConfig.getInstance().getEventBus().post(eventAction);

        Intent intent = new Intent(( (Activity)context), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ( (Activity)context).startActivity(intent);


        ( (Activity)context).finish();


    }

    public void onLoginFailed() {
//        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
        _emailText.setEnabled(true);
        _passwordText.setEnabled(true);
        _progress.hide();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (LoginActivity) activity;
    }


    @Subscribe
    public void onLoginResponse(LoginResponsePayload dataPayload) {

        Log.d(this.getClass().getSimpleName(), "Entering event bus");

        if (dataPayload.getStatusCode() == ErrorCodes.SUCCESS_CODE) {
            new AsyncTask<LoginResponsePayload, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(LoginResponsePayload... dataPayload) {

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    onLoginSuccess(mActivity);
                }
            }.execute(dataPayload);
        } else {
            QuickUtils.runToast(getActivity(), dataPayload.getStatusMessage());
            onLoginFailed();
        }
    }

    @Override
    public void onValidationSucceeded() {
        login();

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getFailedRules().get(0).getMessage(getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                QuickUtils.runToast(getActivity(), message);
            }
        }
    }

}
