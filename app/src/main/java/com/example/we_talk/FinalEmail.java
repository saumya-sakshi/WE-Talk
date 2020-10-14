package com.example.we_talk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class FinalEmail extends AppCompatActivity {
    //Initialize all the widgets
   TextView e1,e2,e3;
    Button btn;
    String semail,spass;
    private TextToSpeech mTTS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_email); e1= findViewById(R.id.et_to);
        e2=findViewById(R.id.et_subject);
        e3=findViewById(R.id.et_message);
        btn = findViewById(R.id.bt_send);
        e1.setText(ComposePage.sent_to);
        e2.setText(SubjectMail.sent_subject);
        e3.setText(MessageMail.sent_msg);
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        Log.e("Status", "Language supported");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
        e3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    float pitch=0.5f;
                    if (pitch < 0.1) pitch = 0.1f;
                    float speed = 0.9f;
                    if (speed < 0.1) speed = 0.1f;
                    mTTS.setPitch(pitch);
                    mTTS.setSpeechRate(speed);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mTTS.speak("Click on end of screen to send sucessfully",TextToSpeech.QUEUE_FLUSH,null,null);
                    } else {
                        mTTS.speak("Click on end of screen to send sucessfully", TextToSpeech.QUEUE_ADD, null);
                    }

                }

        });

        //senders email credentials
        semail="demowetalk.09@gmail.com";
        spass="wetalk1234";

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // initialize properties
                Properties properties = new Properties();
                properties.put("mail.smtp.auth","true");
                properties.put("mail.smtp.starttls.enable","true");
                properties.put("mail.smtp.host","smtp.gmail.com");
                properties.put("mail.smtp.port","587");


                //initialize session
                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(semail,spass);
                    }
                });
                try{
                    Message message= new MimeMessage(session);
                    //sender email
                    message.setFrom(new InternetAddress(semail));
                    // recipient email
                    message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(e1.getText().toString().trim()));
                    //Email subject
                    message.setSubject(e2.getText().toString().trim());
                    //Email message
                    message.setText(e3.getText().toString().trim());

                    //send email
                    new SendMail().execute(message);


                }
                catch (MessagingException e){
                    e.printStackTrace();
                }
            }
        });

    }
    private  class  SendMail extends AsyncTask<Message,String,String> {
        // Initialize progress dialog
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Create and show progress dialog
            progressDialog=ProgressDialog.show(FinalEmail.this,"PLEASE WAIT","Sending Mail.............",true,false);

        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                //when sucess
                Transport.send(messages[0]);
                return "SUCESS";
            } catch (MessagingException e) {
                //when error
                e.printStackTrace();
                return "ERROR";
            }

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Dismiss progress dialog
            progressDialog.dismiss();
            if(s.equals("SUCESS")){
                //WHEN SUCCESS
                // initialize alert dialog

                AlertDialog.Builder builder= new AlertDialog.Builder(FinalEmail.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color = '#509324'>SUCCESS</font>"));
                builder.setMessage("MAIL SENT SUCCESSFULLY");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        //clear all edit text
                        e1.setText("");
                        e2.setText("");
                        e3.setText("");

                    }
                });
                //show alert dialog box
                builder.show();

            }else{
                // when error
                Toast.makeText(getApplicationContext(),"Something went wrong???????",Toast.LENGTH_LONG).show();
            }
        }
    }
}
