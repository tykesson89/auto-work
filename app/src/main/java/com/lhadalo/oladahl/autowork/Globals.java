package com.lhadalo.oladahl.autowork;

/**
 * Created by oladahl on 16-04-30.
 */
public class Globals {
    private static Globals instance;
    private boolean companyIsSynced;


    public void setCompanyIsSynced(boolean syncedState){
        this.companyIsSynced = syncedState;
    }

    public boolean getCompanyIsSynced(){
        return this.companyIsSynced;
    }

    public static synchronized Globals getInstance(){
        if(instance == null){
            instance = new Globals();
        }
        return instance;
    }
}
