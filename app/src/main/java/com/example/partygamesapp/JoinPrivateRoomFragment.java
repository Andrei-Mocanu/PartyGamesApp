package com.example.partygamesapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.partygamesapp.databinding.FragmentPrivateRoomPasswordBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JoinPrivateRoomFragment extends Fragment {
    FragmentPrivateRoomPasswordBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_private_room_password,
                container,
                false
        );


        binding.joinRoomBtn.setOnClickListener(view -> {
            String roomId = getArguments().getString("UUidCamera").toString();
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("Camere");
            Bundle bundle = new Bundle();
            bundle.putString("UUidCamera", roomId);
            bundle.putString("gameType", getArguments().getString("gameType"));
            bundle.putString("userType", getArguments().getString("userType"));
            myRef.child(roomId).get()
                    .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            String password = dataSnapshot.child("roomPassword").getValue().toString();
                            if (binding.roomPassword.getText().toString().isEmpty() || !binding.roomPassword.getText().toString().equals(password)) {
                                Toast.makeText(getActivity(), "Incorrect Password!", Toast.LENGTH_SHORT).show();
                            } else {
                                Navigation.findNavController(view).navigate(R.id.action_joinPrivateRoomFragment_to_roomLobbyFragment, bundle);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


        return binding.getRoot();
    }



}
