package com.example.partygamesapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.partygamesapp.databinding.FragmentGuessPasswordGameBinding;
import com.example.partygamesapp.databinding.FragmentGuessPasswordGamePlayerBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class GuessPasswordGamePlayerFragment extends Fragment {

    FragmentGuessPasswordGamePlayerBinding binding;
    FirebaseAuth mAuth;
    HashMap<Object, String> hashMap = new HashMap<>();
    UUID uuid = UUID.randomUUID();
    String currentUserUid;
    String description;
    String choosenWord;
    String roomId;
    String userType;
    String desc;
    int rounds = 0;
    int score = 0;
    String word;
    CountDownTimer timer;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference myRef = database.getReference("Camere");
    String choosenWordFirebase;
    String descriptionFirebase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_guess_password_game_player,
                container,
                false
        );
        mAuth = FirebaseAuth.getInstance();
        currentUserUid = mAuth.getCurrentUser().getUid();
        userType = getArguments().getString("userType");
        roomId = getArguments().getString("UUidCamera");

        init();

        binding.buttonSubmitDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("abecede", binding.inputGuessDescription.getText().toString() + " " + word);
                if (binding.inputGuessDescription.getText().toString().toLowerCase().equals(word.toLowerCase())) {
                    score++;
                }

                myRef.child(roomId).child("choosenWord").setValue("-");
                myRef.child(roomId).child("description").setValue("-");

                binding.inputGuessDescription.setText("");

                rounds++;
                init();
            }
        });


        myRef.child(roomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("aiintratba", "da");
                if (rounds == 3) {
                    timer.cancel();

                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
                    DatabaseReference myRef = database.getReference("Jocuri");
                    myRef.child(roomId).child("scorPlayer").setValue(score);
                    binding.buttonGuessText2.setText("GAME OVER!\nScor: " + score);

                } else {
                    desc = snapshot.child("description").getValue().toString();
                    word = snapshot.child("choosenWord").getValue().toString();

                    binding.buttonGuessText2.setText(desc);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return binding.getRoot();
    }

    private void init() {
        binding.buttonGuessText1.setVisibility(View.INVISIBLE);
        binding.buttonGuessText3.setVisibility(View.INVISIBLE);
        binding.buttonGuessText2.setText(desc);

        if (timer != null) {
            timer.cancel();
        }

        timer = new CountDownTimer(50000, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.buttonTimeLeft.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                binding.buttonTimeLeft.setText("done!");
            }
        }.start();
    }
}