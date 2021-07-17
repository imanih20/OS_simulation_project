package com.example.OsProject.models;


import android.os.Parcel;
import android.os.Parcelable;
import com.example.OsProject.activities.App;

public class CommandInput implements Parcelable {
    private String curDir;
    private String command = "";
    private String result = "";
    public CommandInput(String curDir){
        setCurDir(curDir);
    }
    protected CommandInput(Parcel in) {
        curDir = in.readString();
        command = in.readString();
        result = in.readString();
    }

    public static final Creator<CommandInput> CREATOR = new Creator<CommandInput>() {
        @Override
        public CommandInput createFromParcel(Parcel in) {
            return new CommandInput(in);
        }

        @Override
        public CommandInput[] newArray(int size) {
            return new CommandInput[size];
        }
    };

    public String getCurDir() {
        StringBuilder builder = new StringBuilder();
        boolean root = false;
        for (String s:curDir.split("/")){
            if (s.equals(App.ROOT_DIR.getName())) {
                root = true;
                builder.append("/");
                continue;
            }
            if (root){
                if (!builder.toString().equals("/"))
                    builder.append("/");
                builder.append(s);
            }
        }
        return builder.toString();
    }
    public void setCurDir(String curDir) {
        this.curDir = curDir;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(curDir);
        dest.writeString(command);
        dest.writeString(result);
    }
}
