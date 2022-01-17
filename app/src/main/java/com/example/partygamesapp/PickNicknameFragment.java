package com.example.partygamesapp;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.partygamesapp.databinding.FragmentPickNicknameBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class PickNicknameFragment extends Fragment {

    FragmentPickNicknameBinding binding;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_pick_nickname,
                container,
                false
        );

        mAuth = FirebaseAuth.getInstance();
        binding.pickNicknameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference myRef = database.getReference("Nicknames");

                if(binding.nicknamelET.getText().toString() == "")
                {
                    myRef.child(mAuth.getUid().toString()).setValue("User " + mAuth.getUid().toString());
                }
                else
                {
                    myRef.child(mAuth.getUid().toString()).setValue(binding.nicknamelET.getText().toString());
                }
                Navigation.findNavController(view).navigate(R.id.action_pickNicknameFragment_to_mainPageFragment);

            }
        });
        return binding.getRoot();
    }
}