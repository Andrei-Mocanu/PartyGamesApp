package com.example.partygamesapp;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.partygamesapp.databinding.FragmentGuessPasswordGameBinding;


public class GuessPasswordGameFragment extends Fragment {

    FragmentGuessPasswordGameBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_guess_password_game,
                container,
                false
        );
        return binding.getRoot();
    }
}