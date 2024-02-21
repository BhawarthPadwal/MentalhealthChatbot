package com.bae.dialogflowbot.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

public class UserRegisterPage extends AppCompatActivity {
    TextView traverse_pg;
    ImageView back_btn;
    LinearLayout google_ll;
    TextInputLayout email_et, pass_et, confirm_pass_et;
    Button signup_btn;
    FirebaseAuth mAuth;
    GoogleSignInClient googleSignInClient;
    int RC_SIGN_IN = 20;

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
        setContentView(R.layout.activity_user_register_page);

        call_all_ids();
        traversing();
        sign_in();
    }
    private void call_all_ids() {
        back_btn = (ImageView) findViewById(R.id.back_btn_reg);
        traverse_pg = (TextView) findViewById(R.id.return_login_pg);
        email_et = (TextInputLayout) findViewById(R.id.email_txt_reg);
        pass_et = (TextInputLayout) findViewById(R.id.pass_txt_reg);
        confirm_pass_et = (TextInputLayout) findViewById(R.id.confirm_pass_txt_reg);
        signup_btn = (Button) findViewById(R.id.signup_btn_reg);
        google_ll = (LinearLayout) findViewById(R.id.google_ll_reg);
    }

    private void sign_in() {

        mAuth = FirebaseAuth.getInstance();

        // Create account using email and password
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = email_et.getEditText().getText().toString().trim();
                String password = confirm_pass_et.getEditText().getText().toString().trim();

                if (email_validator()) {
                    if (password_validator()) {
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(UserRegisterPage.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Toast.makeText(UserRegisterPage.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                                            linkAccount(EmailAuthProvider.getCredential(email, password)); // Link the email/password account
                                            updateUI(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                Toast.makeText(UserRegisterPage.this, "Email already exists. Try signing in.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // For other authentication failures, display a generic message
                                                Toast.makeText(UserRegisterPage.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                            }
                                            updateUI(null);
                                        }
                                    }
                                });

                    }
                }
            }
        });

        // Continue using google
        google_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserRegisterPage.this, "Create acc by google", Toast.LENGTH_SHORT).show();

                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail().
                        build();

                googleSignInClient = GoogleSignIn.getClient(UserRegisterPage.this, googleSignInOptions);

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
                            Toast.makeText(UserRegisterPage.this, "Google signed in successfully!", Toast.LENGTH_SHORT).show();
                            linkAccount(credential);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithGoogle:failure", task.getException());
                            Toast.makeText(UserRegisterPage.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }

    private void linkAccount(AuthCredential credential) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.linkWithCredential(credential)
                    .addOnCompleteListener(UserRegisterPage.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Account linking successful
                                FirebaseUser linkedUser = task.getResult().getUser();
                                Toast.makeText(UserRegisterPage.this, "Account linked successfully!", Toast.LENGTH_SHORT).show();
                                updateUI(linkedUser);
                            } else {
                                // If account linking fails, handle the error
                                Exception exception = task.getException();
                                if (exception instanceof FirebaseAuthUserCollisionException) {
                                    // There is already an account with the linked credential
                                    // Handle the conflict as needed
                                    Toast.makeText(UserRegisterPage.this, "Email is already associated with another account.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // For other authentication failures, display a generic message
                                    Toast.makeText(UserRegisterPage.this, "Account linking failed", Toast.LENGTH_SHORT).show();
                                }
                                updateUI(null);
                            }
                        }
                    });
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // User is authenticated, extract information and customize UI
            String userName = user.getDisplayName();
            String userEmail = user.getEmail();

            // Check if the user signed up using email and password
            boolean isEmailAndPassword = false;
            for (UserInfo profile : user.getProviderData()) {
                if (profile.getProviderId().equals("password")) {
                    isEmailAndPassword = true;
                    break;
                }
            }

            Intent intent;
            if (isEmailAndPassword) {
                // User signed up using email and password, redirect to login page
                intent = new Intent(UserRegisterPage.this, UserLoginPage.class);
            } else {
                // User signed up using Google sign-in, redirect to MainActivity
                intent = new Intent(UserRegisterPage.this, HomePage.class);
            }
            intent.putExtra("USER_NAME", userName);
            intent.putExtra("USER_EMAIL", userEmail);
            startActivity(intent);

            // Apply transition animation and finish the current activity
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {
            // Handle the case when user is null (not authenticated)
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
        String confirm_password = confirm_pass_et.getEditText().getText().toString().trim();

        if (password.isEmpty()) {
            pass_et.setError("Please Enter Your Password");
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
            pass_et.setFocusable(true);
            return false;
        } else if (password.length() < 8) {
            pass_et.setError("Password must have at least 8 characters");
            Toast.makeText(this, "Password must have at least 8 characters", Toast.LENGTH_SHORT).show();
            pass_et.setFocusable(true);
            return false;
        }else if (confirm_password.isEmpty()) {
            confirm_pass_et.setError("Please Confirm Your Password");
            Toast.makeText(this, "Please Confirm Your Password", Toast.LENGTH_SHORT).show();
            confirm_pass_et.setFocusable(true);
            return false;
        } else if (!password.equals(confirm_password)) {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            email_et.setError(null);
            confirm_pass_et.setError("Passwords don't match");
            confirm_pass_et.setFocusable(true);
            return false;
        } else {
            email_et.setError(null);
            pass_et.setError(null);
            confirm_pass_et.setError(null);
            return true;
        }

    }
    private void traversing() {
        traverse_pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserRegisterPage.this, UserLoginPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserRegisterPage.this, UserLoginPage.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

    }
}