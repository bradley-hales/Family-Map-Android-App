package Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.bradl.familymapserver.LoginViewModel;
import com.example.bradl.familymapserver.Model;
import com.example.bradl.familymapserver.R;
import com.example.bradl.familymapserver.ServerProxy;

import Activities.MainActivity;
import Async_Tasks.GetEventsAsyncTask;
import Async_Tasks.GetPeopleAsyncTask;
import Models.AuthToken;
import Request.EventRequest;
import Request.LoginRequest;
import Request.PersonRequest;
import Request.RegisterRequest;
import Response.LoginResponse;
import Response.RegisterResponse;

public class LoginFragment extends Fragment implements GetPeopleAsyncTask.Context, GetEventsAsyncTask.Context {
    private static final String SERVER_HOST = "host";
    private static final String SERVER_PORT = "port";
    private static final String USER_NAME = "userName";
    private static final String PASS_WORD = "passWord";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final String GENDER = "gender";

    private static String mHost = null;
    private static String mPort = null;
    private static String userName = null;
    private static String passWord = null;
    private static String firstName = null;
    private static String lastName = null;
    private static String email = null;
    private static String gender = null;
    private static ServerProxy serverProxy;
    private static AuthToken authToken;


    private LoginViewModel mViewModel;
    private LoginRequest mLoginReguest;
    private RegisterRequest mRegisterRequest;
    private Button mLoginButton;
    private Button mRegisterButton;
    private EditText mServerHost;
    private EditText mServerPort;
    private EditText mUserName;
    private EditText mPassWord;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private RadioGroup mradioGroup;
    private RadioButton mGender;
    private Model model;



    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginReguest = new LoginRequest();
        mRegisterRequest = new RegisterRequest();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.login_fragment, container, false);

        mLoginButton = (Button) v.findViewById(R.id.sign_in_button);
        mRegisterButton = (Button) v.findViewById(R.id.register_button);
        // disable login and register button
        mLoginButton.setEnabled(false);
        mRegisterButton.setEnabled(false);

        mServerHost = (EditText) v.findViewById(R.id.server_host_input);
        mHost = mServerHost.getText().toString();
        model.setHost(mHost);
        mServerPort = (EditText) v.findViewById(R.id.server_port_input);
        mPort = mServerPort.getText().toString();
        model.setPort(mPort);
        mUserName = (EditText) v.findViewById(R.id.user_name_input);
        mPassWord = (EditText) v.findViewById(R.id.password_input);
        mFirstName = (EditText) v.findViewById(R.id.first_name_input);
        mLastName = (EditText) v.findViewById(R.id.last_name_input);
        mEmail = (EditText) v.findViewById(R.id.email_input);
        mradioGroup = (RadioGroup) v.findViewById(R.id.radioGender);
        mGender = (RadioButton) mradioGroup.findViewById(mradioGroup.getCheckedRadioButtonId());
        gender = String.valueOf(mGender.getText().charAt(0)).toLowerCase();
        mRegisterRequest.setGender(gender);


        mServerHost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mHost = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                CheckLoginEnable();
                CheckRegisterEnable();
            }
        });
        mServerPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPort = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                CheckLoginEnable();
                CheckRegisterEnable();
            }
        });
        mUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLoginReguest.setUserName(s.toString());
                mRegisterRequest.setUserName(s.toString());
                userName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                CheckLoginEnable();
                CheckRegisterEnable();
            }
        });
        mPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLoginReguest.setPassword(s.toString());
                mRegisterRequest.setPassword(s.toString());
                passWord = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                CheckLoginEnable();
                CheckRegisterEnable();
            }
        });
        mFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRegisterRequest.setFirstName(s.toString());
                firstName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                CheckRegisterEnable();
            }
        });
        mLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRegisterRequest.setLastName(s.toString());
                lastName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                CheckRegisterEnable();
            }
        });
        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRegisterRequest.setEmail(s.toString());
                email = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                CheckRegisterEnable();
            }
        });
        mradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                mGender = (RadioButton) mradioGroup.findViewById(checkedId);
                gender = String.valueOf(mGender.getText().charAt(0)).toLowerCase();
                mRegisterRequest.setGender(gender);
                CheckRegisterEnable();
            }
        });
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // disable button while completing log in
                mLoginButton.setEnabled(false);
                mRegisterButton.setEnabled(false);

                serverProxy = new ServerProxy(mHost, mPort);
                LoginAsyncTask login = new LoginAsyncTask();
                login.execute(mLoginReguest);
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // disable button while registering
                mRegisterButton.setEnabled(false);
                mLoginButton.setEnabled(false);

                serverProxy = new ServerProxy(mHost, mPort);
                RegisterAsyncTask register = new RegisterAsyncTask();
                register.execute(mRegisterRequest);
            }
        });


        return v;
    }

    private void CheckRegisterEnable(){
        String host = mServerHost.getText().toString();
        String port = mServerPort.getText().toString();
        String user = mUserName.getText().toString();
        String passW = mPassWord.getText().toString();
        String first = mFirstName.getText().toString();
        String last = mLastName.getText().toString();
        String email = mEmail.getText().toString();

        if (host.isEmpty() || port.isEmpty() || user.isEmpty() || passW.isEmpty() ||
                first.isEmpty() || last.isEmpty() || email.isEmpty()){
            // Not enough information to enable button
            mRegisterButton.setEnabled(false);
        }
        else{
            // All registration information given
            mRegisterButton.setEnabled(true);
        }
    }

    private void CheckLoginEnable(){
        String host = mServerHost.getText().toString();
        String port = mServerPort.getText().toString();
        String user = mUserName.getText().toString();
        String passW = mPassWord.getText().toString();

        if (host.isEmpty() || port.isEmpty() || user.isEmpty() || passW.isEmpty()){
         // not enough information
            mLoginButton.setEnabled(false);
        }
        else{
            // enable login button
            mLoginButton.setEnabled(true);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        // TODO: Use the ViewModel
    }

    private class LoginAsyncTask extends AsyncTask<LoginRequest, String, LoginResponse>{
        @Override
        protected void onPostExecute(LoginResponse loginResponse) {
            super.onPostExecute(loginResponse);
            if (loginResponse.getAuth() != null){
                getEventsPeople(loginResponse.getAuth());
            }
            else{
                Toast.makeText(getActivity(), loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                CheckLoginEnable();
            }
        }

        @Override
        protected LoginResponse doInBackground(LoginRequest... loginRequests) {
            return serverProxy.Login(loginRequests[0]);
        }
    }
    private class RegisterAsyncTask extends AsyncTask<RegisterRequest, Void, RegisterResponse>{
        @Override
        protected void onPostExecute(RegisterResponse registerResponse) {
            super.onPostExecute(registerResponse);
            if (registerResponse.getAuth() != null){
                getEventsPeople(registerResponse.getAuth());
            }
            else{
                Toast.makeText(getActivity(), registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                CheckRegisterEnable();
            }
        }

        @Override
        protected RegisterResponse doInBackground(RegisterRequest... registerRequests) {
            return serverProxy.Register(registerRequests[0]);
        }
    }

    public void getEventsPeople(AuthToken auth){
        authToken = auth;

        model = Model.getInstance();
        model.setAuthToken(authToken.getUUID());
        model.setCurrUser(authToken.getUserName());
        model.setPersonID(authToken.getPersonID());

        // make person and event request
        PersonRequest pRequest = new PersonRequest(authToken.getUUID());
        EventRequest eRequest = new EventRequest(authToken.getUUID());

        //get people and events
        GetPeopleAsyncTask getPeople = new GetPeopleAsyncTask(LoginFragment.this, mPort, mHost);
        GetEventsAsyncTask getEvents = new GetEventsAsyncTask(LoginFragment.this, mPort, mHost);
        getPeople.execute(pRequest);
        getEvents.execute(eRequest);

    }


    @Override
    public void onGetPeopleComplete() {
        // Do nothing, all tasks done in GetPeopleAsyncTask, Toast will display once
        // events are complete
    }

    @Override
    public void onGetEventsComplete() {
        // Login successful
        model.fillDictionairies();
        String name = model.getName();
        String welcome = "Welcome " + name + "!";
        Toast.makeText(getActivity(), welcome, Toast.LENGTH_LONG).show();
        ((MainActivity)getActivity()).replaceFrag();


    }

}
