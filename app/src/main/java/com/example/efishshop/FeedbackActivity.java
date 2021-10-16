package com.example.efishshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.efishshop.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class FeedbackActivity extends AppCompatActivity {

    private TextView feedbackForm;
    private EditText feedbackField, feedbackUser;
    private Button submitFeedbackBtn;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedbackForm = findViewById(R.id.feedback_1);
        feedbackField = findViewById(R.id.feedback_field);
        feedbackUser = findViewById(R.id.feedback_username);
        submitFeedbackBtn = findViewById(R.id.submit_feedback);
        loadingBar = new ProgressDialog(this);

        submitFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFeedback();
            }
        });
    }

    private void saveFeedback() {
        String Uname = feedbackUser.getText().toString();
        String feedbacks = feedbackField.getText().toString();

        if (TextUtils.isEmpty(Uname)) {
            Toast.makeText(this, "Please enter your name here", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(feedbacks)) {
            Toast.makeText(this, "Please write your feedback here", Toast.LENGTH_SHORT).show();
        } else {
            dbsavefeedback(Uname, feedbacks);
        }
    }

    private void dbsavefeedback(final String Uname, final String feedbacks) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Feedbacks").child(Uname).exists())) {
                    HashMap<String, Object> userfbMap = new HashMap<>();
                    userfbMap.put("Username", Uname);
                    userfbMap.put("Feedbacks", feedbacks);



                    RootRef.child("Feedbacks").child(Uname).updateChildren(userfbMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(FeedbackActivity.this, "Thank you for your feedback", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(FeedbackActivity.this, HomeActivity.class);
                                        startActivity(intent);

                                    }
                                    else {
                                        loadingBar.dismiss();
                                        Toast.makeText(FeedbackActivity.this, "Network Error!! Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {

                    Toast.makeText(FeedbackActivity.this, "Please try again ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FeedbackActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}