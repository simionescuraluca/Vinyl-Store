package com.vinyl.helper;

public class AuthenticationHeaderHelper {

    public static String getTokenHashOrNull(String auth) {
        if (auth == null) {
            return null;
        }
        return auth.replace("Bearer ", "");
    }
}
