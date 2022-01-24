package com.example.partygamesapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.partygamesapp.databinding.FragmentBonusGameBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;


public class BonusGameFragment extends Fragment {
    TextView num1, num2, answer, result;
    String uid;
    FragmentBonusGameBinding binding;
    int rounds = 0;
    int score = 0;
    String userType;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_bonus_game,
                container,
                false
        );

        num1 = binding.firstNumber;
        num2 = binding.secondNumber;
        answer = binding.answer;
        result = binding.trueOrFalse;
        userType = getArguments().getString("userType");
        init();

        uid = getArguments().getString("UUidCamera");

        Log.d("UUidCamera", uid);
        //Toast.makeText(getActivity(), uid.toString(), Toast.LENGTH_SHORT).show();

        binding.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one(v);
            }
        });
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                two(v);
            }
        });
        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                three(v);
            }
        });
        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                four(v);
            }
        });
        binding.button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                five(v);
            }
        });
        binding.button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                six(v);
            }
        });
        binding.button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seven(v);
            }
        });
        binding.button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eight(v);
            }
        });
        binding.button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nine(v);
            }
        });
        binding.button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zero(v);
            }
        });
        binding.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer.setText("");
            }
        });
        binding.ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rounds++;
                if (rounds < 3) {

                    if (Integer.valueOf(String.valueOf(num1.getText())) + Integer.valueOf(String.valueOf(num2.getText())) ==Integer.valueOf(String.valueOf(answer.getText()))) {
                        score++;
                    }
                    init();
                }
                else {
                    if (Integer.valueOf(String.valueOf(num1.getText())) + Integer.valueOf(String.valueOf(num2.getText())) ==Integer.valueOf(String.valueOf(answer.getText()))) {
                        //result.setText("AI CASTIGAT !!!");
                        score++;
                        result.setText("Gata jocu !!! Scorul e " + score);
                        //Salvare user castigator in bd

                    } else {
                        result.setText("Gata jocu !!! Scorul e " + score);
                        //result.setText("INCORECT");
                    }
                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
                    DatabaseReference myRef = database.getReference("Jocuri");
                    if(userType.equals("admin")) {
                        myRef.child(uid).child("scorAdmin").setValue(String.valueOf(score));
                        myRef.child(uid).child("timestampAdmin").setValue(String.valueOf(System.currentTimeMillis()));
                    }
                    else if(userType.equals("player")) {
                        myRef.child(uid).child("scorPlayer").setValue(String.valueOf(score));
                        myRef.child(uid).child("timestampPlayer").setValue(String.valueOf(System.currentTimeMillis()));
                    }
                    else if(userType.equals("spectator")){
                        myRef.child(uid).child("scorSpectator").setValue(String.valueOf(score));
                    }

                    myRef.child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!snapshot.child("scorAdmin").getValue().toString().equals("-")
                            && !snapshot.child("scorPlayer").getValue().toString().equals("-")
                            && !snapshot.child("timestampAdmin").getValue().toString().equals("-")
                            && !snapshot.child("timestampPlayer").getValue().toString().equals("-")) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
                                DatabaseReference myRef = database.getReference("Camere");
                                myRef.child(uid).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                    @Override
                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                        String adminUid = "",playerUid = "";
                                        Iterator it = dataSnapshot.getChildren().iterator();
                                        while(it.hasNext())
                                        {
                                            DataSnapshot snapshot1 = (DataSnapshot) it.next();
                                            if(snapshot1.getValue().toString().equals("admin")) {
                                                adminUid = snapshot1.getKey().toString();
                                            }
                                            else if(snapshot1.getValue().toString().equals("player")) {
                                                playerUid = snapshot1.getKey().toString();
                                            }
                                        }
                                        Log.d("admin Uid inainte ",adminUid);
                                        DatabaseReference myRefNickname = database.getReference("Nicknames");
                                        String finalAdminUid = adminUid;
                                        String finalPlayerUid = playerUid;
                                        myRefNickname.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                            @Override
                                            public void onSuccess(DataSnapshot dataSnapshot) {
                                                Log.d("admin Uid ",finalAdminUid);
                                                Log.d("player Uid ",finalPlayerUid);

                                                String adminName = dataSnapshot.child(finalAdminUid).getValue().toString();
                                                String playerName = dataSnapshot.child(finalPlayerUid).getValue().toString();
                                                Log.d("admin name ",adminName);
                                                Log.d("player name ",playerName);
                                                if(Integer.parseInt(snapshot.child("scorAdmin").getValue().toString()) >
                                                        Integer.parseInt(snapshot.child("scorPlayer").getValue().toString())) {
                                                    result.setText("A castigat " + adminName);
                                                }

                                                else if(Integer.parseInt(snapshot.child("scorPlayer").getValue().toString()) >
                                                        Integer.parseInt(snapshot.child("scorAdmin").getValue().toString())) {
                                                    result.setText("A castigat " + playerName);
                                                }
                                                else if(Integer.parseInt(snapshot.child("scorPlayer").getValue().toString()) ==
                                                        Integer.parseInt(snapshot.child("scorAdmin").getValue().toString())) {
                                                    if(Integer.parseInt(snapshot.child("timestampPlayer").getValue().toString()) <
                                                            Integer.parseInt(snapshot.child("timestampAdmin").getValue().toString())) {
                                                        result.setText("A castigat " + playerName);
                                                    }
                                                    else {
                                                        result.setText("A castigat " + adminName);
                                                    }

                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });




                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }


            }
        });

        return binding.getRoot();
    }

    void printAns(String a){
        String text = answer.getText().toString();
        answer.setText(text + a);
    }

    public void one(View view) {
        printAns("1");
    }

    public void two(View view) {
        printAns("2");
    }

    public void three(View view) {
        printAns("3");
    }

    public void four(View view) {
        printAns("4");
    }

    public void five(View view) {
        printAns("5");
    }

    public void six(View view) {
        printAns("6");
    }

    public void seven(View view) {
        printAns("7");
    }

    public void eight(View view) {
        printAns("8");
    }

    public void nine(View view) {
        printAns("9");
    }

    public void zero(View view) {
        printAns("0");
    }

    void init(){
        Random myRandom = new Random();

        int random1 = myRandom.nextInt(101);
        int random2 = myRandom.nextInt(101);

        num1.setText("" + random1);
        num2.setText("" + random2);

        answer.setText("");
        result.setText("");
    }
}