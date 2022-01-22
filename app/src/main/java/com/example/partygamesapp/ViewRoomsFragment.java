package com.example.partygamesapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.partygamesapp.databinding.FragmentViewRoomsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;


public class ViewRoomsFragment extends Fragment {

    FragmentViewRoomsBinding binding;
    RecyclerView recyclerView;
    static RecycleViewAdapterViewRooms adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_view_rooms,
                container,
                false
        );

        recyclerView = binding.rvRooms;
        adapter = new RecycleViewAdapterViewRooms(getActivity(),new ArrayList<Room>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getAllRooms();
        return binding.getRoot();
    }


    void getAllRooms() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Camere");
        myRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Iterator it = dataSnapshot.getChildren().iterator();
                while(it.hasNext()) {
                    DataSnapshot snapshot = (DataSnapshot) it.next();
                    showRoomInfoClassic(snapshot.getKey().toString());
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showRoomInfoClassic(String roomId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Camere");
        myRef.child(roomId).get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String roomName = dataSnapshot.child("roomName").getValue().toString();
                        String roomType = dataSnapshot.child("roomType").getValue().toString();
                        String currentNumberOfUsers = String.valueOf(dataSnapshot.getChildrenCount() - 4);
                        String maximumNumberOfUsers = "5";
                        String choosenGame = dataSnapshot.child("gameType").getValue().toString();
                        String currentGameState = dataSnapshot.child("gameState").getValue().toString();
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

                                                /*binding.showRoomInfoTv.setText(
                                                        "Room Name: " + roomName + "\n"
                                                                + "Room Type: " + roomType + "\n"
                                                                + "Admin Name: " + adminName + "\n"
                                                                + "Current Number of Users: " + currentNumberOfUsers + "\n"
                                                                + "Maximum Number of Users: " + maximumNumberOfUsers + "\n"
                                                                + "Choosen Game: " + choosenGame + "\n"
                                                                + "Current Game State: " + currentGameState
                                                );*/
                                                adapter.addNewRoom(new Room(roomId,roomName,roomType,adminName,currentNumberOfUsers,
                                                        maximumNumberOfUsers,choosenGame,currentGameState));

                                                adapter.notifyDataSetChanged();
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

    private void showRoomInfoExtended() {

    }
}