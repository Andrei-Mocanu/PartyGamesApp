package com.example.partygamesapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.partygamesapp.databinding.FragmentCreateRoomBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.UUID;


public class CreateRoomFragment extends Fragment {

    FragmentCreateRoomBinding binding;
    FirebaseAuth mAuth;
    boolean isRoomPublic = false, isFirstGame = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_create_room,
                container,
                false
        );

        mAuth = FirebaseAuth.getInstance();

        binding.privateRoomBtn.setOnClickListener(v -> changeHighlightButtonRoomType(binding.privateRoomBtn, binding));
        binding.publicRoomBtn.setOnClickListener(v -> changeHighlightButtonRoomType(binding.publicRoomBtn, binding));
        binding.guessPasswordGameBtn.setOnClickListener(v -> changeHighlightButtonGameType(binding.guessPasswordGameBtn, binding));
        binding.bonusGameBtn.setOnClickListener(v -> changeHighlightButtonGameType(binding.bonusGameBtn, binding));

        changeHighlightButtonRoomType(binding.privateRoomBtn, binding);
        changeHighlightButtonGameType(binding.guessPasswordGameBtn, binding);

        binding.createRoomBtn.setOnClickListener(view -> {
            UUID uuid = UUID.randomUUID();
            HashMap<Object, String> hashMap = new HashMap<>();

            if (binding.roomnameET.getText().toString().equals("")) {
                hashMap.put("roomName", "-");
            } else {
                hashMap.put("roomName", binding.roomnameET.getText().toString());
            }

            if (isRoomPublic) {
                hashMap.put("roomPassword", "-");
                hashMap.put("roomType", "public");
            } else {
                hashMap.put("roomPassword", binding.privateRoomPassword.getText().toString());
                hashMap.put("roomType", "private");
            }

            if (isFirstGame) {
                hashMap.put("gameType", "guessThePassword");
            } else {
                hashMap.put("gameType", "bonusGame");
            }

            hashMap.put(mAuth.getCurrentUser().getUid().toString(), "admin");

            hashMap.put("gameState", "Lobby");

            hashMap.put("choosenWord", "-");


            FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("Camere");
            myRef.child(uuid.toString()).setValue(hashMap);


            HashMap<String,String> dateJocuri = new HashMap<String,String>();
            dateJocuri.put("scorAdmin","-");
            dateJocuri.put("scorPlayer","-");
            dateJocuri.put("timestampAdmin","-");
            dateJocuri.put("timestampPlayer","-");

            DatabaseReference myRefJocuri = database.getReference("Jocuri");
            myRefJocuri.child(uuid.toString()).setValue(dateJocuri);


            Navigation.findNavController(view).navigate(R.id.action_createRoomFragment_to_mainPageFragment);

//            if (isFirstGame)
//                Navigation.findNavController(view).navigate(R.id.action_createRoomFragment_to_guessPasswordGameFragment);
//            else
//                Navigation.findNavController(view).navigate(R.id.action_createRoomFragment_to_bonusGameFragment);

        });

        return binding.getRoot();
    }

    public void changeHighlightButtonRoomType(ImageButton btn, FragmentCreateRoomBinding binding) {
        if (btn.equals(binding.privateRoomBtn)) {

            binding.privateRoomPasswordLayout.setVisibility(View.VISIBLE);
            btn.setBackgroundColor(Color.parseColor("#65E0E5"));
            binding.publicRoomBtn.setBackgroundColor(0);
            isRoomPublic = false;
        } else if (btn.equals(binding.publicRoomBtn)) {

            binding.privateRoomPasswordLayout.setVisibility(View.INVISIBLE);
            btn.setBackgroundColor(Color.parseColor("#65E0E5"));
            binding.privateRoomBtn.setBackgroundColor(0);
            isRoomPublic = true;
        }
    }

    public void changeHighlightButtonGameType(ImageButton btn, FragmentCreateRoomBinding binding) {
        if (btn.equals(binding.guessPasswordGameBtn)) {
            btn.setBackgroundColor(Color.parseColor("#65E0E5"));
            binding.bonusGameBtn.setBackgroundColor(0);
            isFirstGame = true;
        } else if (btn.equals(binding.bonusGameBtn)) {
            btn.setBackgroundColor(Color.parseColor("#65E0E5"));
            binding.guessPasswordGameBtn.setBackgroundColor(0);
            isFirstGame = false;
        }
    }
}