package com.example.partygamesapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.partygamesapp.databinding.FragmentRoomLobbyBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;


public class RoomLobbyFragment extends Fragment {
    FragmentRoomLobbyBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_room_lobby,
                container,
                false
        );

        String roomId = getArguments().getString("UUidCamera").toString();
        showChatMessages(roomId);

        binding.sendChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(roomId);
            }
        });

        return binding.getRoot();
    }

    private void showChatMessages(String roomId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Chats");
        myRef.child(roomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> keyList = new ArrayList<>();
                Iterator it = snapshot.getChildren().iterator();
                while (it.hasNext()) {
                    DataSnapshot dataSnapshot = (DataSnapshot) it.next();
                    keyList.add(dataSnapshot.getKey().toString());
                }
                Collections.sort(keyList);

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference myRef = database.getReference("Nicknames");
                myRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                String chat = "";
                                for(String key: keyList) {
                                    String mesaj = snapshot.child(key).child("mesaj").getValue().toString();
                                    String expeditor = dataSnapshot.child(snapshot.child(key).child("expeditor").getValue().toString()).getValue().toString();
                                    chat = chat + expeditor + ": " + mesaj + "\n";
                                }
                                binding.chatTv.setText(chat);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(String roomId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Chats");

        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("mesaj", binding.chatEt.getText().toString());
        hashMap.put("expeditor", FirebaseAuth.getInstance().getCurrentUser().getUid());
        myRef.child(roomId).child(String.valueOf(System.currentTimeMillis())).setValue(hashMap);

        binding.chatEt.setText("");
    }
}