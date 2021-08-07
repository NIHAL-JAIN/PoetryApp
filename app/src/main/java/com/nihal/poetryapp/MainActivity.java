package com.nihal.poetryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.nihal.poetryapp.Adapter.PoetryAdapter;
import com.nihal.poetryapp.Api.ApiClient;
import com.nihal.poetryapp.Api.ApiInterface;
import com.nihal.poetryapp.Models.PoetryModel;
import com.nihal.poetryapp.Response.GetPoetryResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    PoetryAdapter poetryAdapter;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intialization();
        getdata();


    }

    private void intialization() {
        recyclerView = findViewById(R.id.poetry_recyclerView);
        Retrofit retrofit = ApiClient.getclient();
        apiInterface = retrofit.create(ApiInterface.class);

    }
    private void setadapter(List<PoetryModel> poetryModels) {
        poetryAdapter = new PoetryAdapter(this,poetryModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(poetryAdapter);
    }
    private void getdata(){
        apiInterface.getpoetry().enqueue(new Callback<GetPoetryResponse>() {
            @Override
            public void onResponse(Call<GetPoetryResponse> call, Response<GetPoetryResponse> response) {
                try {
                    if (response != null ){
                        if (response.body().getStatus().equals("1")){
                            setadapter(response.body().getData());
                        }else{
                            Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                catch (Exception e){
                    Log.e( "onResponse: ",e.getLocalizedMessage());
                }

            }

            @Override
            public void onFailure(Call<GetPoetryResponse> call, Throwable t) {
                Log.e("onFailure: ",t.getLocalizedMessage());

            }
        });
    }
}