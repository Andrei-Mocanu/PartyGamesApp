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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    boolean isAdmin;
    String roomId;

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

        roomId = getArguments().getString("UUidCamera");
        isAdmin = getArguments().getBoolean("isAdmin");

        myRef.child(roomId).child("choosenWord").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                choosenWordFirebase = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                Log.d("TAG", (String) dataSnapshot.getValue());
            }
        }).addOnFailureListener(e -> Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show());

        myRef.child(roomId).child("description").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                descriptionFirebase = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                Log.d("TAG", (String) dataSnapshot.getValue());
                binding.buttonGuessText2.setText(descriptionFirebase);
            }
        }).addOnFailureListener(e -> Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show());

        if (isAdmin == false) {
            binding.buttonGuessText1.setVisibility(View.INVISIBLE);
            binding.buttonGuessText3.setVisibility(View.INVISIBLE);
            binding.buttonGuessText2.setText(descriptionFirebase);
        } else {
            binding.buttonGuessText1.setText("Piano");
            binding.buttonGuessText2.setText("Snow");
            binding.buttonGuessText3.setText("Sleep");
        }
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.buttonTimeLeft.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                binding.buttonTimeLeft.setText("done!");
            }
        }.start();

//        String uid = getArguments().getString("UUidCamera");
//        Log.d("UUidCamera",uid);
//        Toast.makeText(getActivity(), uid.toString(), Toast.LENGTH_SHORT).show();


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
                hashMap.put("snow", description);
                //String roomId = getArguments().getString("UUidCamera").toString();
                //TODO  sa parsez roomID ul prin fragmente sau sa vad cum pot sa ii fac retrieve direct din firebase
                // TODO sa fac faza cu fraze si sa nu aiba voie sa fie obligat sa descrie cuvantul cu alte cuvinte
                // TODO cele 3 cuvinte de dificultate sa le aleg din 3 dictionare ( easy: 10 cuvinte super common, medium: 100 cuvinte, hard: lista aia cu 3k cuvinte)

                if (isAdmin == true) {
                    myRef.child(roomId).child("choosenWord").setValue(choosenWord);
                    myRef.child(roomId).child("description").setValue(description);
                } else {
                    if (description.equals(choosenWordFirebase.toLowerCase(Locale.ROOT))) {
                        Toast.makeText(getActivity(), "Correct!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Incorrect!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return binding.getRoot();
    }
}