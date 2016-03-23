package com.lhadalo.oladahl.autowork;

import android.content.Context;
import android.os.AsyncTask;

import UserPackage.User;

/**
 * Created by Henrik on 2016-03-23.
 */
public class SettingsController {
    private Context context;
    private User user;
    private String tag;
    public SettingsController(Context context, String tag, User user){
        this.user = user;
        this.context = context;
        this.tag = tag;
        if(tag.equals("Delete User")){
            new DeleteUser(context).execute(user);
        }else if(tag.equals("Change User Info")){
            new ChangeUserInfo(context).execute(user);
        }

    }




    class DeleteUser extends AsyncTask<User, Void, String>{
        private Context context;

        public DeleteUser(Context context){
            this.context = context;
        }

        protected String doInBackground(User... params) {
            return null;
        }
    }




    class ChangeUserInfo extends AsyncTask<User, Void, String>{
        private Context context;

        public ChangeUserInfo(Context context){
            this.context = context;
        }


        protected String doInBackground(User... params) {
            return null;
        }
    }

}