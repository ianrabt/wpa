package net.ianrabt.wpa.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import net.ianrabt.wpa.FBRepository;
import net.ianrabt.wpa.FBRepositoryDelegate;
import net.ianrabt.wpa.R;
import net.ianrabt.wpa.Repository;
import net.ianrabt.wpa.models.HabitModel;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements FBRepositoryDelegate{

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private FBRepository mRepository;
    private ArrayList<HabitModel> habitList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btn = (Button)findViewById(R.id.sign_out_button);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
            }
        };

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

        mRepository = new FBRepository(this);
        //mRepository.createHabit("anotherHabit");
        mRepository.getHabits();

    }

    public void handleHabitResponse(ArrayList<HabitModel> habitResponse){
        this.habitList = habitResponse;

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
