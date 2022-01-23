package com.example.partygamesapp;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

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
import java.util.List;


public class RoomLobbyFragment extends Fragment {
    FragmentRoomLobbyBinding binding;
    String roomId;
    String gameType;
    View view;
    NavController navController;
    boolean navigated = false;
    String userType;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        this.view = view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_room_lobby,
                container,
                false
        );

        roomId = getArguments().getString("UUidCamera").toString();
        gameType = getArguments().getString("gameType").toString();
        userType = getArguments().getString("userType").toString();
        showChatMessages(roomId);
        showRoomInfoExtended(roomId);

        Log.i("usertype", userType);

        binding.sendChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(roomId);
            }
        });

        startGameButton(roomId);

        binding.startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGameForAdmin(gameType, view);
            }
        });

        startGameForPlayersAndSpectators(gameType, view);

        return binding.getRoot();
    }

    private void startGameForPlayersAndSpectators(String gameType, View view) {
        Bundle bundle = new Bundle();
        bundle.putString("UUidCamera", roomId);
        bundle.putBoolean("isAdmin", false);
        bundle.putString("userType", userType);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Camere");
        myRef.child(roomId).child("gameState").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String gameState = snapshot.getValue().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference myRef = database.getReference("Camere");
                myRef.child(roomId).get()
                        .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                String adminUID = "";
                                Iterator it = dataSnapshot.getChildren().iterator();
                                while (it.hasNext()) {
                                    DataSnapshot dataSnapshot1 = (DataSnapshot) it.next();
                                    if (dataSnapshot1.getValue().toString().equals("admin")) {
                                        adminUID = dataSnapshot1.getKey().toString();
                                    }
                                }

                                Log.i("adminnn", FirebaseAuth.getInstance().getCurrentUser().getUid() + "\n" + adminUID);

                                Log.i("ceapaaa", String.valueOf(getVisibleFragment().getId()) + "\n\n" + getVisibleFragment().toString());

                                if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(adminUID)) {

                                } else {
                                    if (gameState.equals("Started")) {
                                        if (gameType.equals("guessThePassword") && String.valueOf(getVisibleFragment().getId()).equals("2131231088") && navigated == false) {
                                            navigated = true;
                                            navController.navigate(R.id.action_roomLobbyFragment_to_guessPasswordGameFragment, bundle);
                                        } else if (gameType.equals("bonusGame") && String.valueOf(getVisibleFragment().getId()).equals("2131231088") && navigated == false) {
                                            navigated = true;
                                            navController.navigate(R.id.action_roomLobbyFragment_to_bonusGameFragment, bundle);
                                        }
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    private void startGameForAdmin(String gameType, View view) {
        Bundle bundle = new Bundle();
        bundle.putString("UUidCamera", roomId);
        bundle.putBoolean("isAdmin", true);
        bundle.putString("userType", userType);
        if (gameType.equals("guessThePassword")) {
            Navigation.findNavController(view).navigate(R.id.action_roomLobbyFragment_to_guessPasswordGameFragment, bundle);
        } else if (gameType.equals("bonusGame")) {
            Navigation.findNavController(view).navigate(R.id.action_roomLobbyFragment_to_bonusGameFragment, bundle);
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Camere");
        myRef.child(roomId).child("gameState").setValue("Started");
    }


    private void startGameButton(String roomId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Camere");
        myRef.child(roomId).get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String adminUID = "";
                        Iterator it = dataSnapshot.getChildren().iterator();
                        while (it.hasNext()) {
                            DataSnapshot snapshot = (DataSnapshot) it.next();
                            if (snapshot.getValue().toString().equals("admin")) {
                                adminUID = snapshot.getKey().toString();
                            }
                        }
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(adminUID)) {
                            binding.startGameBtn.setVisibility(View.VISIBLE);
                        } else {
                            binding.startGameBtn.setVisibility(View.INVISIBLE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

    public void showRoomInfoExtended(String roomId) {

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Camere");
        myRef.child(roomId).get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String roomName = dataSnapshot.child("roomName").getValue().toString();
                        String roomType = dataSnapshot.child("roomType").getValue().toString();
                        String maximumNumberOfUsers = "2";
                        String choosenGame = dataSnapshot.child("gameType").getValue().toString();
                        String currentGameState = dataSnapshot.child("gameState").getValue().toString();
                        int currentNumberOfUsersInt = 0;
                        int numberOfSpectatorsInt = 0;
                        ArrayList<Pair<String, String>> players = new ArrayList<>();
                        Iterator iterator = dataSnapshot.getChildren().iterator();
                        while (iterator.hasNext()) {
                            DataSnapshot dataSnapshot1 = (DataSnapshot) iterator.next();
                            if (dataSnapshot1.getValue().toString().equals("admin")
                                    || dataSnapshot1.getValue().toString().equals("player")
                                    || dataSnapshot1.getValue().toString().equals("spectator")) {
                                currentNumberOfUsersInt++;
                            }

                            if (dataSnapshot1.getValue().toString().equals("spectator")) {
                                numberOfSpectatorsInt++;
                            }

                            if (dataSnapshot1.getValue().toString().equals("player")) {
                                players.add(new Pair<>(dataSnapshot1.getKey().toString(), "0"));
                            }
                        }
                        String currentNumberOfUsers = String.valueOf(currentNumberOfUsersInt);
                        String numberOfSpectators = String.valueOf(numberOfSpectatorsInt);
                        String scoreSpectators = "0";
                        Iterator it = dataSnapshot.getChildren().iterator();
                        while (it.hasNext()) {
                            DataSnapshot snapshot = (DataSnapshot) it.next();
                            if (snapshot.getValue().toString().equals("admin")) {
                                String adminUID = snapshot.getKey().toString();
                                FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
                                DatabaseReference myRef = database.getReference("Nicknames");
                                myRef.child(adminUID).get()
                                        .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                            @Override
                                            public void onSuccess(DataSnapshot dataSnapshot) {
                                                String adminName = dataSnapshot.getValue().toString();

                                                FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
                                                DatabaseReference myRef = database.getReference("Nicknames");
                                                myRef.get()
                                                        .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DataSnapshot dataSnapshot) {

                                                                String playersStr = "Players:\n";
                                                                for (Pair<String, String> pair: players) {
                                                                    String playerUID = pair.first;
                                                                    String score = pair.second;
                                                                    String playerNickname = dataSnapshot.child(playerUID).getValue().toString();
                                                                    playersStr = playersStr + playerNickname + ": " + score + "\n";
                                                                }
                                                                if (players.size() == 0) {
                                                                    playersStr = "Players: -\n";
                                                                }

                                                                binding.gameLobbyTv.setText(
                                                                        "Game Lobby" + "\n"
                                                                                + "Room Name: " + roomName + "\n"
                                                                                + "Room Type: " + roomType + "\n"
                                                                                + "Admin Name: " + adminName + "\n"
                                                                                + "Current Number of Users: " + currentNumberOfUsers + "\n"
                                                                                + "Maximum Number of Users: " + maximumNumberOfUsers + "\n"
                                                                                + "Choosen Game: " + choosenGame + "\n"
                                                                                + "Current Game State: " + currentGameState + "\n"
                                                                                + playersStr
                                                                                + "Number of Spectators: " + numberOfSpectators + "\n"
                                                                                + "Spectators Score: " + scoreSpectators
                                                                );
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
                        //adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}