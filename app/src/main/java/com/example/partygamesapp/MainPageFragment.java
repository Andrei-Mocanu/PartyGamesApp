package com.example.partygamesapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.partygamesapp.databinding.FragmentMainPageBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Iterator;

public class MainPageFragment extends Fragment {

    FragmentMainPageBinding binding;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_main_page,
                container,
                false
        );

        mAuth = FirebaseAuth.getInstance();
        afisareDatePersonale();

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                if (mAuth.getCurrentUser() == null) {
                    Navigation.findNavController(view).navigate(R.id.action_mainPageFragment_to_loginFragment);
                } else {

                }
            }
        });

        return binding.getRoot();
    }

    private void afisareDatePersonale() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://itir-9c98e-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Utilizatori");
        myRef.child(mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String text = "";
                        Iterator it = dataSnapshot.getChildren().iterator();
                        while (it.hasNext()) {
                            DataSnapshot snapshot = (DataSnapshot)it.next();
                            String key = snapshot.getKey().toString();
                            String value = snapshot.getValue().toString();
                            if (!key.equals("uid")) {
                                text += key + ": " + value + "\n";
                            }
                        }
                        binding.infoTv.setText(text);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}