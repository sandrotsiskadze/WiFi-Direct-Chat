package com.example.wifidirectchat;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {
    private int owner;
    private String message;

    public Message(int owner, String message) {
        this.owner = owner;
        this.message = message;
    }

    protected Message(Parcel in) {
        owner = in.readInt();
        message = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(owner);
        parcel.writeString(message);
    }
}
