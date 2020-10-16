package com.example.forestfiredetection.database;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences usersSession;
    SharedPreferences.Editor editor;

    Context context;

    public static final String SESSION_USER_SESSION = "userLoginSession";
    public static final String SESSION_REMEMBER_ME = "rememberMe";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_FULL_NAME = "fullname";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE_NUMBER = "phoneNo";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_MODE = "mode";

    private static final String IS_REMEMBER_ME = "IsRememberMe";
    public static final String KEY_SESSION_PASSWORD = "password";
    public static final String KEY_SESSION_USERNAME = "username";


    public SessionManager(Context context, String session) {

        this.context = context;
        usersSession = context.getSharedPreferences(session, Context.MODE_PRIVATE);
        editor = usersSession.edit();
        editor.putBoolean(IS_LOGIN, false);

    }

    public void createLoginSession(String fullname, String username, String email, String phoneNo, String password, String mode) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_FULL_NAME, fullname);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE_NUMBER, phoneNo);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_MODE, mode);

        editor.commit();
    }

    public void createLoginSession(String fullname, String username, String email, String phoneNo, String password) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_FULL_NAME, fullname);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE_NUMBER, phoneNo);
        editor.putString(KEY_PASSWORD, password);

        editor.commit();
    }

    public void createLoginSession(String fullname, String username, String mode) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_FULL_NAME, fullname);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_MODE, mode);
        editor.commit();
    }

    public HashMap<String, String> getUsersDetailFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_FULL_NAME, usersSession.getString(KEY_FULL_NAME, null));
        userData.put(KEY_USERNAME, usersSession.getString(KEY_USERNAME, null));
        userData.put(KEY_EMAIL, usersSession.getString(KEY_EMAIL, null));
        userData.put(KEY_PHONE_NUMBER, usersSession.getString(KEY_PHONE_NUMBER, null));
        userData.put(KEY_PASSWORD, usersSession.getString(KEY_PASSWORD, null));
        userData.put(KEY_MODE, usersSession.getString(KEY_MODE, null));

        return userData;
    }

    public boolean checkLogin() {
        if (usersSession.getBoolean(IS_LOGIN, false)) {
            return true;
        } else
            return false;
    }

    public void logoutUserFromSession() {
        editor.clear();
        editor.commit();
    }




    public void createRememberMeSession(String username, String password) {

        editor.putBoolean(IS_REMEMBER_ME, true);

        editor.putString(KEY_SESSION_USERNAME, username);
        editor.putString(KEY_SESSION_PASSWORD, password);
        editor.commit();
    }

    public HashMap<String, String> getRememberMeDetailFromSession() {
        HashMap<String, String> userData = new HashMap<>();

        userData.put(KEY_SESSION_USERNAME, usersSession.getString(KEY_SESSION_USERNAME, null));
        userData.put(KEY_SESSION_PASSWORD, usersSession.getString(KEY_SESSION_PASSWORD, null));

        return userData;
    }

    public boolean checkRememberMe() {
        if (usersSession.getBoolean(IS_REMEMBER_ME, false)) {
            return true;
        } else
            return false;
    }

    public void forgetRememberMe() {
        editor.remove(KEY_SESSION_USERNAME);
        editor.remove(KEY_SESSION_PASSWORD);
        editor.putBoolean(IS_REMEMBER_ME, false);
        editor.commit();
    }



}
