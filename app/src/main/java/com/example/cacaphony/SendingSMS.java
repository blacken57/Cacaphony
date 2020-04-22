package com.example.cacaphony;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendingSMS {
    private static final String TAG ="OCDDDDDDDDDDD" ;
    public static void sendSms(String otp, String phone) {
        try {
            // Construct data
            String apiKey = "apikey=" + "SCBTJWPkGvM-mzqpfE0fC4KUKERmDH3qVgYHJ5qyrZ";
            String message = "&message=" + "Your OTP is " + otp;
            String sender = "&sender=" + "ETR";
            String numbers = "&numbers=" + phone;

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
                Log.d(TAG, "I'm HEREEEEEEEEEEEEEEEEE");
            }
            rd.close();
//            return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS "+e);
//            return "Error "+e;
        }
    }

}
