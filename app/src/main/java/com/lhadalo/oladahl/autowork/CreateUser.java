package com.lhadalo.oladahl.autowork;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Henrik on 2016-03-10.
 */
public class CreateUser {
    public static String TAG = RegistrationActivity.class.getSimpleName();
    private Context context;
    private SQLiteDB sqLiteDB;
    private static final String URL = "";
    private ProgressDialog pDialog;


    public void showMessege() {
        pDialog.setMessage("Registering ...");
        showDialog();
    }

    public void registerUser(final String firstname, final String lastname, final String email, final String password, final String salary, final String workplace) {

            showMessege();
        StringRequest strReq = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Register Response: " + response.toString());
                        hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            if (!error) {
                                String userId = jObj.getString("userid");

                                JSONObject user = jObj.getJSONObject("Users");
                                String firstname = user.getString("firstname");
                                String lastname = user.getString("lastname");
                                String email = user.getString("email");
                                String workplace = user.getString("workplace");
                                String workplaceid = user.getString("workplaceid");
                                String salary = user.getString("salary");

                                sqLiteDB.createUser(userId, firstname, lastname,email,salary,workplace,workplaceid);




                                // TODO: 2016-03-10: metod för att komma till en annan sida.

                            } else {
                                String errorMsg = jObj.getString("error_msg");
                                showMessage(errorMsg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                showMessage(error.getMessage());
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("firstname", firstname);
                params.put("lastname", lastname);
                params.put("email", email);
                params.put("password", password);
                params.put("salary", salary);
                params.put("workplace", workplace);

                return params;
            }

        };


        RequestQueue.getInstance().addToRequestQueue(strReq);
    }



    private void showMessage(String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }



    public void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    public void hideDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

}
