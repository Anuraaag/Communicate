package com.example.anurag.commute;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.net.URLEncoder;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends AppCompatActivity {

    Button send;
    EditText message;
    EditText phone;
    String sms, contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (isConnected() == false) {
                        Toast.makeText(MainActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    } else {
                        message = findViewById(R.id.message);
                        phone = findViewById(R.id.phone);

                        //String.valueOf(phone.getText().toString().length()) //print count
                        if (null != phone.getText().toString() && phone.getText().toString().length() == 10) {

                            contact = phone.getText().toString();
                            if (null != message.getText().toString()) {
                                sms = message.getText().toString();
                                sendToWhatsapp(contact, sms);
                            } else {
                                Toast.makeText(MainActivity.this, "Can not send a blank message", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Enter a valid number", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }




            }
        });
    }

    private void sendToWhatsapp(String contact, String sms) {

        PackageManager packageManager = getApplicationContext().getPackageManager();

        if (isPackageInstalled("com.whatsapp", getApplicationContext().getPackageManager()) == true) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setFlags(FLAG_ACTIVITY_NEW_TASK);
            contact = "+91" + contact;
            try {
                String url = "https://api.whatsapp.com/send?phone="+ contact +"&text=" + URLEncoder.encode(sms, "UTF-8");
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                if (i.resolveActivity(packageManager) != null) {
                    getApplicationContext().startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(this, "Please install Whatsapp", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            return packageManager.getApplicationInfo(packageName, 0).enabled;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }



    public boolean isConnected() throws InterruptedException, IOException {
        final String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }
}
