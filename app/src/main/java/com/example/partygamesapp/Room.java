package com.example.partygamesapp;

public class Room {

    private String roomName,roomType,adminName,currNumberUsers,maxNumberUsers, gameType,currState,uid;

    public  Room() {
        this.uid = "";
        this.roomName = "";
        this.roomType = "";
        this.adminName = "";
        this.currNumberUsers = "";
        this.maxNumberUsers = "";
        this.gameType = "";
        this.currState = "";

    }

    public  Room(String uid,String roomName,String roomType,String adminName,String currNumberUsers,String maxNumberUsers,String gameType,String currState) {
        this.uid = uid;
        this.roomName = roomName;
        this.roomType = roomType;
        this.adminName = adminName;
        this.currNumberUsers = currNumberUsers;
        this.maxNumberUsers = maxNumberUsers;
        this.gameType = gameType;
        this.currState = currState;
    }

    public  String toString() {
        return "Room Name: " + this.roomName + "\n"
                + "Room Type: " + roomType + "\n"
                + "Admin Name: " + adminName + "\n"
                + "Current Number of Users: " + currNumberUsers + "\n"
                + "Maximum Number of Users: " + maxNumberUsers + "\n"
                + "Choosen Game: " + gameType + "\n"
                + "Current Game State: " + currState;
    }

    public String getUid()
    {
        return this.uid;
    }


    public String getRoomName() {
        return roomName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getCurrNumberUsers() {
        return currNumberUsers;
    }

    public String getMaxNumberUsers() {
        return maxNumberUsers;
    }

    public String getGameType() {
        return gameType;
    }

    public String getCurrState() {
        return currState;
    }
}
