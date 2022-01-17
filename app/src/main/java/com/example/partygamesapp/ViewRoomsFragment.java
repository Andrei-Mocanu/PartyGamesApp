package com.example.partygamesapp;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.partygamesapp.databinding.FragmentViewRoomsBinding;


public class ViewRoomsFragment extends Fragment {

    FragmentViewRoomsBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_view_rooms,
                container,
                false
        );

        return binding.getRoot();
    }
}