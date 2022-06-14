package com.example.cs4520_inclassassignments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import retrofit2.http.Url;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 08
 */
public class AuthenticationActivity extends AppCompatActivity implements ic08_RegisterFragment.DataManager, ic08_LoginFragment.DataManager {

    public static final String ic08_USER_KEY = "NUMAD_ic08_FirebaseLoggedIn";
    private static final String TAG = "IC08_AUTH";
    public static String userKey = "SignedIn User";
    private TextView title;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
            updateUI(currentUser);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ic08_auth);

        db = FirebaseFirestore.getInstance();

        title = findViewById(R.id.ic08_authen_title);
        mAuth = FirebaseAuth.getInstance();

        getSupportFragmentManager().beginTransaction()
                .addToBackStack("Login")
                .replace(R.id.ic08_authen_fragment_container, new ic08_LoginFragment())
                .commit();

        title.setText("Login");
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    @Override
    public void returnLogin() {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("Register")
                .replace(R.id.ic08_authen_fragment_container, new ic08_LoginFragment())
                .commit();
        title.setText("Login");
    }

    private void createDbUser(String first, String last, String username, String email) {
        Log.d(TAG, "Attempting user addition.");

        Map<String, Object> user = new HashMap<>();
        user.put("first_name", first);
        user.put("last_name", last);
        user.put("username", username);
        user.put("email", email);

        db.collection("users")
                .document(username)
                .set(user).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "FAILED user addition: " + e.getMessage());
                        MainActivity.showToast(AuthenticationActivity.this, "Adding user to Firebase Failed.");
                    }
                });

/*        ArrayList<Conversation> convos = new ArrayList<>();
        Message intro = new Message(
                "Default",
                "press new chat and talk to your friends!");
        List<String> recps = new ArrayList<String>();
        recps.add("Tutorial");
        List<Message> msg = new ArrayList<>();
        msg.add(intro);

        convos.add(new Conversation("Empty", recps, msg));
        Conversations s = new Conversations(convos);*/

        Map<String, String> start = new HashMap<>();
        start.put("conversation", "/default");

        db.collection("users")
                .document(username)
                .collection("conversations")
                .document("DEFAULT")
                .set(start).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,
                                "FAILED user collection conversation init: " + e.getMessage());
                        MainActivity.showToast(
                                AuthenticationActivity.this,
                                "Adding user collection to Firebase Failed.");
                    }
                });
    }

    @Override
    public void postLogin(String username, String password) {
        findEmailFromUsername(username, password);
    }

    private void firebaseLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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
                            Toast.makeText(AuthenticationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void findEmailFromUsername(String username, String password) {
        db.collection("users")
                .document(username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            firebaseLogin(document.getString("email"), password);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                });
    }

    @Override
    public void openRegisterFragment() {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("Register")
                .replace(R.id.ic08_authen_fragment_container, new ic08_RegisterFragment())
                .commit();
        title.setText("Register Here");
    }

    @Override
    public void postRegister(String firstName, String lastName, String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .setPhotoUri(
                                            Uri.parse("https://as1.ftcdn.net/v2/jpg/03/53/11/00/1000_F_353110097_nbpmfn9iHlxef4EDIhXB1tdTD0lcWhG9.jpg"))
                                    .build();
                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");
                                        createDbUser(firstName, lastName, username, email);
                                        updateUI(user);
                                    } else {
                                        Log.d(TAG, "Failed User update: " + task.getResult());
                                        MainActivity.showToast(
                                                AuthenticationActivity.this,
                                                "Something went wrong when adding your user, try again?");
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(AuthenticationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
/*        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.ic07_preferences_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_session_key), session.getToken());
        editor.apply();*/

        if (user != null) {
            MainActivity.saveObjectToSharedPreference(this, getString(R.string.ic08_preferences_file), ic08_USER_KEY, user);

            Intent toConvos = new Intent(this, InClass08Activity.class);
            toConvos.putExtra(userKey, user);
            startActivity(toConvos);
        }
    }

    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}