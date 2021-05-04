package net.simplifiedcoding.simplifiedcoding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import myapp.model.Employee;
import myapp.model.Product;

/**
 * Created by Belal on 9/5/2017. modified by Arek on 19.01.2021
 */

//here for this class we are using a singleton pattern

public class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    private static final String KEY_USER_ID = "key_user_id";
    private static final String KEY_USER_NAME = "key_user_name";
    private static final String KEY_EMAIL = "key_email";
    private static final String KEY_EMPLOYEE_ID = "key_employee_id";
    private static final String KEY_EMPLOYEE_NAME = "key_employee_name";
    private static final String KEY_EMPLOYEE_SURNAME = "key_employee_surname";
    private static final String KEY_EMPLOYEE_SYMBOL = "key_employee_symbol";
    private static final String KEY_ALL_EMPLOYEES = "employees";
    //private static final String KEY_GENDER = "keygender";
    private static final String KEY_PRODUCT_ID = "key_prod_id";
    private static final String KEY_PRODUCT_NAME = "key_prod_name";
    private static final String KEY_PRODUCT_SYMBOL = "key_prod_symbol";
    private static final String KEY_PRODUCT_QUANTITY = "key_prod_quantity";


    private static SharedPrefManager mInstance;
    private static SharedPrefManager mInstanceProduct;
    private static Context mCtx;
    private static Context mCtxProduct;

    private SharedPrefManager(Context context/*, Context contextproduct*/) {
        mCtx = context;
        //}
        //private SharedPrefManager(Context contextproduct) {
        //mCtxProduct = contextproduct;
    }

    public static synchronized SharedPrefManager getInstance(Context context/*, Context contextproduct*/) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context/*, contextproduct*/);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferencesUser = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesUser.edit();
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        //editor.putString(KEY_GENDER, user.getGender());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferencesUser = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferencesUser.getString(KEY_USER_NAME, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_USER_ID, -1),
                sharedPreferences.getString(KEY_USER_NAME, null),
                sharedPreferences.getString(KEY_EMAIL, null) //,
                //sharedPreferences.getString(KEY_GENDER, null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferencesUser = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesUser.edit();
        //editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }

    //method to let the product login
    //this method will store the product data in shared preferences
    public void productLogin(Product product) {
        SharedPreferences sharedPreferencesProduct = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesProduct.edit();
        editor.putInt(KEY_PRODUCT_ID, product.getId());
        editor.putInt(KEY_PRODUCT_QUANTITY, product.getQuantity());
        editor.putString(KEY_PRODUCT_NAME, product.getName());
        editor.putString(KEY_PRODUCT_SYMBOL, product.getSymbol());
        //editor.putString(KEY_GENDER, user.getGender());
        editor.apply();
    }

    public void employeeLogin(Employee employee) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMPLOYEE_SYMBOL, employee.getSymbol());
        editor.putString(KEY_EMPLOYEE_SURNAME, employee.getSurname());
        editor.putString(KEY_EMPLOYEE_NAME, employee.getName());
        //editor.putString(KEY_GENDER, user.getGender());
        editor.apply();
    }

    //this method will give the employee
    public Employee getEmployee() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Employee(
                sharedPreferences.getInt(KEY_EMPLOYEE_ID, -1),
                sharedPreferences.getString(KEY_EMPLOYEE_SURNAME, null),
                sharedPreferences.getString(KEY_EMPLOYEE_NAME, null),
                sharedPreferences.getString(KEY_EMPLOYEE_SYMBOL, null) //,
                //sharedPreferences.getString(KEY_GENDER, null)
        );
    }
   /* //this method will give the registered employees
    public static AllEmployees getAllEmployees() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new AllEmployees(
        //        sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_ALL_EMPLOYEES, null)
        );
    }*/


        //this method will give the logged in ProductInfoActivity
        public Product getProduct() {
            SharedPreferences sharedPreferencesProduct = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            return new Product(
                    sharedPreferencesProduct.getInt(KEY_PRODUCT_ID, -1),
                    sharedPreferencesProduct.getInt(KEY_PRODUCT_QUANTITY, -1),
                    sharedPreferencesProduct.getString(KEY_PRODUCT_NAME, null),
                    sharedPreferencesProduct.getString(KEY_PRODUCT_SYMBOL, null) //,
                    //sharedPreferences.getString(KEY_GENDER, null)
            );
        }
    }
