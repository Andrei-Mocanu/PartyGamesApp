package com.example.partygamesapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecycleViewAdapterViewRooms extends RecyclerView.Adapter<RecycleViewAdapterViewRooms.ViewHolder> {
    private final LayoutInflater mInflater;
    public static ArrayList<Room> roomsList;
    public static ItemClickListener mClickListener;

    public RecycleViewAdapterViewRooms(Context context, ArrayList<Room> roomsList) {
        this.mInflater = LayoutInflater.from(context);
        this.roomsList = roomsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_rooms, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.myTextView.setText(roomsList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return roomsList.size();
    }

    public void addNewRoom(Room room) {
        roomsList.add(room);
    }

    public int getRoomListSize() {
        return roomsList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        MaterialButton joinRoomBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.InfoRoomTV);
            joinRoomBtn = itemView.findViewById(R.id.JoinRoomBtn);

            joinRoomBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
            Bundle bundle = new Bundle();
            bundle.putString("UUidCamera", roomsList.get(getAdapterPosition()).getUid());
            bundle.putString("gameType", roomsList.get(getAdapterPosition()).getGameType());

            if (Integer.parseInt(roomsList.get(getAdapterPosition()).getCurrNumberUsers()) < 2 && !roomsList.get(getAdapterPosition()).getAdminUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference myRef = database.getReference("Camere");
                myRef.child(roomsList.get(getAdapterPosition()).getUid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("player");
                bundle.putString("userType", "player");
            } else if (Integer.parseInt(roomsList.get(getAdapterPosition()).getCurrNumberUsers()) >= 2 && !roomsList.get(getAdapterPosition()).getAdminUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://partygamesapp-39747-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference myRef = database.getReference("Camere");
                myRef.child(roomsList.get(getAdapterPosition()).getUid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("spectator");
                bundle.putString("userType", "spectator");
            }

            if (roomsList.get(getAdapterPosition()).getAdminUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                bundle.putString("userType", "admin");
            }

            if (roomsList.get(getAdapterPosition()).getRoomType().equals("private")) {
                Navigation.findNavController(view).navigate(R.id.action_viewRoomsFragment_to_joinPrivateRoomFragment, bundle);
            } else {
                Navigation.findNavController(view).navigate(R.id.action_viewRoomsFragment_to_roomLobbyFragment, bundle);
            }
        }
    }

    void setClickListener(ItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
