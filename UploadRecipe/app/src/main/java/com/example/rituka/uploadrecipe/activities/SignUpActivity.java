package com.example.rituka.uploadrecipe.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rituka.uploadrecipe.R;
import com.example.rituka.uploadrecipe.api.API;
import com.example.rituka.uploadrecipe.api.API_Interface;
import com.example.rituka.uploadrecipe.api.RailsAPI;
import com.example.rituka.uploadrecipe.models.RequestData;
import com.example.rituka.uploadrecipe.models.ResultData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText phone_no , email_signup , password_password , name ;
    TextView signin_again;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        name = (EditText)findViewById(R.id.name_signup);
        password_password = (EditText)findViewById(R.id.password_password);
        email_signup = (EditText)findViewById(R.id.email_signup);
        phone_no = (EditText)findViewById(R.id.phone_no);
        signup = (Button)findViewById(R.id.signup);
        signin_again = (TextView)findViewById(R.id.signin_again);


        signin_again.setOnClickListener(this);
        signup.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signup) {


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RailsAPI.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RailsAPI api = retrofit.create(RailsAPI.class);


            RequestData requestData = new RequestData();
            requestData.setEmail(email_signup.getText().toString());
            requestData.setPassword(password_password.getText().toString());
            requestData.setPhone_no(phone_no.getText().toString());
            requestData.setName(name.getText().toString());

            Call<ResultData> result = api.getresultsign(requestData);

            result.enqueue(new Callback<ResultData>() {
                @Override
                public void onResponse(Call<ResultData> call, Response<ResultData> response) {

                    ResultData resultData = response.body();
                    if (resultData.isvalid()) {



                        signupmongoreq(resultData.getName());

                        final String PREF_NAME = "com.data.wfi.userdetails";
                        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorffordata = sharedPreferences.edit();

                        editorffordata.putString("AUTH_TOKEN",resultData.getAuth_token());
                        editorffordata.putString("EMAIL_ID",resultData.getEmail());
                        editorffordata.putString("NAME",resultData.getName());
                        editorffordata.apply();


                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Account Already Exists", Toast.LENGTH_LONG).show();

                    }


                }

                @Override
                public void onFailure(Call<ResultData> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Intent intent = new Intent(getBaseContext(), SignInActivity.class);

            startActivity(intent);
        }
    }

    private void signupmongoreq(String name) {


        API_Interface api = API.getApiInstance().getApiInterface();


        RequestData requestData = new RequestData();
        requestData.setName(name);

        Call<ResultData> result = api.getresult(requestData);

        result.enqueue(new Callback<ResultData>() {
            @Override
            public void onResponse(Call<ResultData> call, Response<ResultData> response) {
                return;
            }

            @Override
            public void onFailure(Call<ResultData> call, Throwable t) {
                return;
            }
        });

    }
}

