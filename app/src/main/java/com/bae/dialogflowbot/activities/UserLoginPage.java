package com.bae.dialogflowbot.activities;

import static android.content.ContentValues.TAG;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bae.dialogflowbot.MainActivity;
import com.bae.dialogflowbot.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class UserLoginPage extends AppCompatActivity {
    private TextView traverse_pg, forgot_pass_txt;
    private TextInputLayout email_et, pass_et;
    private Button login_btn;
    private LinearLayout google_ll;
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private int RC_SIGN_IN = 20;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateUI(currentUser);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_page);

        call_all_ids();
        traversing();
        login();

    }
    private void call_all_ids() {

        login_btn = findViewById(R.id.login_btn_login);
        forgot_pass_txt = findViewById(R.id.forgot_pass_txt_login);
        traverse_pg = findViewById(R.id.return_reg_pg);
        email_et = findViewById(R.id.email_txt_log);
        pass_et = findViewById(R.id.pass_txt_log);
        google_ll = findViewById(R.id.google_ll_log);
    }

    private void login() {

        mAuth = FirebaseAuth.getInstance();
        // Log in using email and password
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = email_et.getEditText().getText().toString().trim();
                String password = pass_et.getEditText().getText().toString().trim();

                if (email_validator()) {
                    if (password_validator()) {

                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(UserLoginPage.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            updateUI(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                                            Toast.makeText(UserLoginPage.this, "Invalid credentials.", Toast.LENGTH_SHORT).show();
                                            updateUI(null);
                                        }
                                    }
                                });

                    }
                }

            }


        });

        // Log in using google
        google_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserLoginPage.this, "Login using Google", Toast.LENGTH_SHORT).show();

                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail().
                        build();

                googleSignInClient = GoogleSignIn.getClient(UserLoginPage.this, googleSignInOptions);

                googleSignIn();
            }
        });


    }
    private void googleSignIn() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //callbackManager.onActivityResult(requestCode, resultCode, data); // For fb

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuth(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithGoogle:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(UserLoginPage.this, "Google signed in successfully!", Toast.LENGTH_SHORT).show();
                            linkAccount(credential);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithGoogle:failure", task.getException());
                            Toast.makeText(UserLoginPage.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }
    private void linkAccount(AuthCredential credential) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.linkWithCredential(credential)
                    .addOnCompleteListener(UserLoginPage.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Account linking successful
                                FirebaseUser linkedUser = task.getResult().getUser();
                                Toast.makeText(UserLoginPage.this, "Account linked successfully!", Toast.LENGTH_SHORT).show();
                                updateUI(linkedUser);
                            } else {
                                // If account linking fails, handle the error
                                Exception exception = task.getException();
                                if (exception instanceof FirebaseAuthUserCollisionException) {
                                    // There is already an account with the linked credential
                                    // Handle the conflict as needed
                                    Toast.makeText(UserLoginPage.this, "Email is already associated with another account.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // For other authentication failures, display a generic message
                                    Toast.makeText(UserLoginPage.this, "Account linking failed", Toast.LENGTH_SHORT).show();
                                }
                                updateUI(null);
                            }
                        }
                    });
        }
    }

    private boolean email_validator() {
        if (email_et.getEditText().getText().toString().trim().isEmpty()) {
            email_et.setError("Please Enter Your Email Id");
            Toast.makeText(this, "Please Enter Your Email Id", Toast.LENGTH_SHORT).show();
            email_et.setFocusable(true);
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email_et.getEditText().getText().toString().trim()).matches()) {
            email_et.setError("Please Enter Valid Email Id");
            Toast.makeText(this, "Please Enter Valid Email Id", Toast.LENGTH_SHORT).show();
            email_et.setFocusable(true);
            return false;
        } else {
            email_et.setError(null);
            return true;
        }
    }

    private boolean password_validator() {
        String password = pass_et.getEditText().getText().toString().trim();
        if (password.isEmpty()) {
            pass_et.setError("Please Enter Your Password");
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
            pass_et.setFocusable(true);
            return false;
        } else {
            pass_et.setError(null);
            return true;
        }

    }

    private void traversing() {
        // Get Login Page
        traverse_pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserLoginPage.this, UserRegisterPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        // Get Forgot Password Page
        forgot_pass_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLoginPage.this, ForgotPassword.class);
                intent.putExtra("FROM_ACTIVITY","UserLoginPage");
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // User is authenticated, extract information and customize UI
            String userName = user.getDisplayName();
            String userEmail = user.getEmail();
            SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
            String status = sharedPreferences.getString("Status", "");

            // If the user's profile has been updated, retrieve the updated username
            if (status.equals("Updated")) {
                userName = sharedPreferences.getString("USER_NAME", "");
            } else {
                // If it's a new user or the profile hasn't been updated, use the username from Firebase
                if (userName == null || userName.isEmpty()) {
                    userName = "New User";
                } else {
                    // Save the user's data to SharedPreferences if it's a new user or the profile hasn't been updated
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("USER_NAME", userName);
                    editor.putString("USER_EMAIL", userEmail);
                    editor.apply();
                }
                // Not Sure
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("USER_NAME", userName);
                editor.putString("USER_EMAIL", userEmail);
                editor.apply();

            }

            // Pass user information to the MainDashboard activity if needed
            Intent intent = new Intent(UserLoginPage.this, HomePage.class);
            intent.putExtra("USER_NAME", userName);
            intent.putExtra("USER_EMAIL", userEmail);
            startActivity(intent);

            Toast.makeText(this, userName + " " + userEmail, Toast.LENGTH_SHORT).show();


            // Apply transition animation and finish the current activity
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {
            // Handle the case when user is null (not authenticated)
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}