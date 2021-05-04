//package com.example.codescannerjava;
package myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.budiyev.android.codescanner.*;
import com.google.zxing.Result;

import net.simplifiedcoding.simplifiedcoding.R;
import net.simplifiedcoding.simplifiedcoding.SharedPrefManager;

import myapp.api.ApiServiceType;
import myapp.api.AsyncTask.AsyncTaskApi;
import myapp.api.CallbackImpl;
import myapp.api.ApiBuilder;
import myapp.api.RetrofitApi;
import myapp.api.ResponseContainer;
import myapp.model.Employee;
import myapp.model.Product;
import myapp.releaseActivity.AddReleaseActivityCustViewDial;
import myapp.releaseActivity.ScanToAddActivity;
import retrofit2.Call;
import retrofit2.Response;

public class ScanToCheckActivity extends ScanActivity {

    private final String GET_EMPLOYEE_TAG = "GET_EMPLOYEE";
    private final String GET_PRODUCT_TAG = "GET_PRODUCT";
    protected final String IS_NEXT_ACTIVITY_KEY = "IS_NEXT_ACTIVITY";

    protected Product mProductScanned;
    protected Employee mEmployeeScanned;

    private boolean mIsNextActivity;

    public ScanToCheckActivity() {
        super();
        mProductScanned = null;
        mEmployeeScanned = null;
        mIsNextActivity = true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            mIsNextActivity = savedInstanceState.getBoolean(IS_NEXT_ACTIVITY_KEY, true);
            mScanType = ScanType.fromId(
                    savedInstanceState.getInt(ScanType.class.getName(), ScanType.EACH.ordinal())
            );
            Log.d(ScanToCheckActivity.class.getName(), "mScanType="+mScanType.name());
            Log.d(ScanToCheckActivity.class.getName(), "mIsNextActivity="+mIsNextActivity);
        }
        catch (NullPointerException ne) { }


        this.setButtonCheckAction();

    }

    @Override
    protected void setButtonCheckAction() {
        mButtonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(ScanToCheckActivity.class.getName(), "mIsNextActivity="+mIsNextActivity);
                Log.d(ScanToCheckActivity.class.getName(), "mScanType="+mScanType.name());
                if(mScanType == ScanType.Product ||
                        mScanType == ScanType.EACH) {
                    getProduct(mIsNextActivity);
                    openAddReleaseActivityCustViewDialActivity();

                }

                if(mScanType == ScanType.Employee ||
                        mScanType == ScanType.EACH) {
                    getEmployee(mIsNextActivity);
                    finish();
                }
            }
        });
    }

    public void getProduct (boolean isNextActivity) {
        if(mApiServiceType == ApiServiceType.Retrofit) {
            mRetrofitApiSevice.getProduct(mSymbol).enqueue(
                    new CallbackImpl<ResponseContainer<Product>>(GET_PRODUCT_TAG) {
                        @Override
                        public void onResponse(Call<ResponseContainer<Product>> call,
                                               Response<ResponseContainer<Product>> response) {
                            super.onResponse(call, response);

                            if(response.body().isError()) {
                                Toast.makeText(getApplicationContext(), "Product does not exist in warehouse database",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Product product = response.body().getObject();
                            Log.d(getTag() + "_name", product.getName());
                            Log.d(getTag() + "_symbol", product.getSymbol());

                            mProductScanned = product;
                            //storing the product in shared preferences
                            SharedPrefManager.getInstance(getApplicationContext()).productLogin(product);

                            if(isNextActivity) {
                                //starting the profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), ProductInfoActivity.class));
                            }
                        }

                    });
        }

        if(mApiServiceType == ApiServiceType.AsyncTask)
            mAsyncTaskApiService.getProduct(mSymbol);
    }

    public void getEmployee(boolean isNextActivity) {
        if(mApiServiceType == ApiServiceType.Retrofit) {
            mRetrofitApiSevice.getEmployee(mSymbol).enqueue(
                    new CallbackImpl<ResponseContainer<Employee>>(GET_EMPLOYEE_TAG) {
                        @Override
                        public void onResponse(Call<ResponseContainer<Employee>> call,
                                               Response<ResponseContainer<Employee>> response) {
                            super.onResponse(call, response);

                            if(response.body().isError()) {
                                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Employee employee = response.body().getObject();
                            Log.d(getTag() + "_name", employee.getName());
                            Log.d(getTag() + "_symbol", employee.getSymbol());

                            mEmployeeScanned = employee;

                            //storing the employee in shared preferences
                            SharedPrefManager.getInstance(getApplicationContext()).employeeLogin(employee);

                            if(isNextActivity) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), EmployeeInfoActivity.class));
                            }
                        }

                    });
        }

        if(mApiServiceType == ApiServiceType.AsyncTask)
            mAsyncTaskApiService.getEmployee(mSymbol);
    }

    public void openAddReleaseActivityCustViewDialActivity() {
        Intent intent = new Intent(this, AddReleaseActivityCustViewDial.class);
        startActivity(intent);
    }

}