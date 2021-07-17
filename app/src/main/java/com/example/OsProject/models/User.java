package com.example.OsProject.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class User implements Parcelable {
    public static final String USER_KEY = "user";
    private String usrName = "";
    private String initialPassword = "";
    private String password = "";
    private boolean isManager;
    private String backupQuest = "";
    private String backupAnswer = "";
    public User(){}
    public User(String userName,boolean isManager){
        this(userName,"",isManager);
    }
    public User(String userName,String password,boolean isManager){
        setUsrName(userName);
        setPassword(password);
        setManager(isManager);
    }
    protected User(Parcel in) {
        usrName = in.readString();
        password = in.readString();
        isManager = in.readByte() != 0;
        initialPassword = in.readString();
        backupQuest = in.readString();
        backupAnswer = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    public String getBackupQuest() {
        return backupQuest;
    }

    public void setBackupQuest(String backupQuest) {
        this.backupQuest = backupQuest;
    }

    public String getBackupAnswer() {
        return backupAnswer;
    }

    public void setBackupAnswer(String backupAnswer) {
        this.backupAnswer = backupAnswer;
    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public String getInitialPassword() {
        return initialPassword;
    }

    public void setInitialPassword(String initialPassword) {
        this.initialPassword = initialPassword;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(usrName);
        dest.writeString(password);
        dest.writeByte((byte) (isManager ? 1 : 0));
        dest.writeString(initialPassword);
        dest.writeString(backupQuest);
        dest.writeString(backupAnswer);
    }
    public void setUserData(String[] userData){
        if (userData.length<6) return;
        setUsrName(userData[0]);
        setInitialPassword(userData[1]);
        setPassword(userData[2]);
        setManager(!userData[3].equals("f"));
        setBackupQuest(userData[4]);
        setBackupAnswer(userData[5]);
    }
    public String[] getUserData(){
        String[] cred = new String[6];
        cred[0] = getUsrName();
        cred[1] = getInitialPassword();
        cred[2] = getPassword();
        cred[3] = isManager()?"t":"f";
        cred[4] = getBackupQuest();
        cred[5] = getBackupAnswer();
        return cred;
    }

    @NonNull
    @Override
    public String toString() {
        return "user name: "+getUsrName()+
                "\npassword: "+getPassword()+
                "\nid: "+getInitialPassword()+
                "\nbackup question: "+getBackupQuest()+
                "\nbackup answer: "+getBackupAnswer()+
                "\nAccessibility: "+(isManager?"admin user":"user");
    }
}
