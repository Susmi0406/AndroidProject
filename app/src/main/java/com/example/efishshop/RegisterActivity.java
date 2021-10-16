package com.example.efishshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    //    Password Validation pattern starts here
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    ".{6,}" +
                    "$"
            );
//    Password Validation pattern ends here

    //    Username Validation pattern starts here
    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "$"
            );
    //    Username Validation pattern starts here

    //    Phone Number  Validation pattern starts here
    private static final Pattern PHONE_PATTERN =
            Pattern.compile(
                    "(?=.*[0-9])" +
                            ".{10,}"
            );
    //    Phone Number Validation pattern ends here

    private Button RegisterButton;
    private EditText InputName, InputAddress, InputPhone, InputEmail, InputPassword;

    private TextInputLayout textInputEmail;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterButton = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_fullname);
        InputAddress = (EditText) findViewById(R.id.register_address);
        InputPhone = (EditText) findViewById(R.id.register_phone);
        InputEmail = (EditText) findViewById(R.id.register_email);
        InputPassword = (EditText) findViewById(R.id.register_password);
        loadingBar = new ProgressDialog(this);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String name = InputName.getText().toString();
        String address = InputAddress.getText().toString();
        String phone = InputPhone.getText().toString();
        String email = InputEmail.getText().toString().trim();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(name) || USERNAME_PATTERN.matcher(name).matches()){
            Toast.makeText(this,"Please enter your Full Name",Toast.LENGTH_SHORT).show();
            InputName.setError("Name Field Cannot be Empty");
        }
        else if(TextUtils.isEmpty(address)){
            Toast.makeText(this,"Please enter your Address",Toast.LENGTH_SHORT).show();
            InputAddress.setError("Address Field Cannot be Empty");
        }
        else if(TextUtils.isEmpty(phone) || !PHONE_PATTERN.matcher(phone).matches()){
            Toast.makeText(this,"Phone Number is Required and should not contains letters",Toast.LENGTH_SHORT).show();
            InputPhone.setError("Enter a Valid Phone Number");
        }
        else if(TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Please enter a Valid Email Address",Toast.LENGTH_SHORT).show();
            InputEmail.setError("Please Enter Valid Email");
        }

        else if(TextUtils.isEmpty(password) || !PASSWORD_PATTERN.matcher(password).matches()){
            InputPassword.setError("Password should not be empty and length should be greater than 6 characters");
        }
        else{
            loadingBar.setTitle("Register Now");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatephoneNumber(name, address, phone, email, password);
        }
    }

    private void ValidatephoneNumber(final String name, final String address, final String phone, final String email, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phone).exists())){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("name",name);
                    userdataMap.put("address",address);
                    userdataMap.put("phone",phone);
                    userdataMap.put("email",email);
                    userdataMap.put("password",password);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,"Congratulations, Successfully registered",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);

                                    }
                                    else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this,"Network Error!! Please try again",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(RegisterActivity.this, "This " + phone + "already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}