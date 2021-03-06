package com.example.partygamesapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.partygamesapp.databinding.FragmentGuessPasswordGameBinding;
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


public class GuessPasswordGameFragment extends Fragment {

    FragmentGuessPasswordGameBinding binding;
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
    CountDownTimer timer;
    boolean firstTime = false;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference myRef = database.getReference("Camere");
    String choosenWordFirebase;
    String descriptionFirebase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_guess_password_game,
                container,
                false
        );
        mAuth = FirebaseAuth.getInstance();
        currentUserUid = mAuth.getCurrentUser().getUid();
        userType = getArguments().getString("userType");

        roomId = getArguments().getString("UUidCamera");

        init();


        binding.buttonGuessText1.setOnClickListener(v -> {
            choosenWord = binding.buttonGuessText1.getText().toString().trim();
            binding.buttonGuessText2.setVisibility(View.INVISIBLE);
            binding.buttonGuessText3.setVisibility(View.INVISIBLE);
        });

        binding.buttonGuessText2.setOnClickListener(v -> {
            choosenWord = binding.buttonGuessText2.getText().toString().trim();
            binding.buttonGuessText1.setVisibility(View.INVISIBLE);
            binding.buttonGuessText3.setVisibility(View.INVISIBLE);
        });

        binding.buttonGuessText3.setOnClickListener(v -> {
            choosenWord = binding.buttonGuessText3.getText().toString().trim();
            binding.buttonGuessText1.setVisibility(View.INVISIBLE);
            binding.buttonGuessText2.setVisibility(View.INVISIBLE);
        });

        binding.buttonSubmitDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = Objects.requireNonNull(binding.inputGuessDescription.getText()).toString().trim();

                myRef.child(roomId).child("choosenWord").setValue(choosenWord);
                myRef.child(roomId).child("description").setValue(description);

                binding.inputGuessDescription.setText("");

                if (timer != null) {
                    timer.cancel();
                }
            }
        });

        myRef.child(roomId).child("description").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (rounds == 2 && snapshot.getValue().toString().equals("-")) {
                    binding.buttonGuessText1.setVisibility(View.INVISIBLE);
                    binding.buttonGuessText3.setVisibility(View.INVISIBLE);
                    binding.buttonGuessText2.setVisibility(View.VISIBLE);
                    timer.cancel();

                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
                    DatabaseReference myRef = database.getReference("Jocuri");
                    myRef.child(roomId).child("scorPlayer").get()
                            .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    binding.buttonGuessText2.setText("GAME OVER!\nScor: " + dataSnapshot.getValue().toString());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    desc = snapshot.getValue().toString();

                    if (desc.equals("-") && firstTime == true) {
                        rounds++;
                    }

                    if (desc.equals("-")) {
                        Log.i("ceva", "ceva");
                        init();
                        firstTime = true;
                    }
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
        binding.buttonGuessText1.setVisibility(View.VISIBLE);
        binding.buttonGuessText2.setVisibility(View.VISIBLE);
        binding.buttonGuessText3.setVisibility(View.VISIBLE);
        binding.buttonGuessText1.setText("Piano");
        binding.buttonGuessText2.setText("Snow");
        binding.buttonGuessText3.setText("Sleep");

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