package com.example.partygamesapp;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.partygamesapp.databinding.FragmentBonusGameBinding;


public class BonusGameFragment extends Fragment {

    FragmentBonusGameBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_bonus_game,
                container,
                false
        );
        return binding.getRoot();
    }
}