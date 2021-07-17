package com.example.OsProject.utils;

import android.util.Log;
import com.example.OsProject.activities.App;
import com.example.OsProject.models.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserDb {
    List<User> userList;

    File file;
    public UserDb(){
        setFile();
        userList = new ArrayList<>();
        loudUsers();
    }
    private void setFile(){
        File[] files = App.ROOT_DIR.listFiles();
        assert files!=null;
        for (File f : files){
            if (f.getName().equals("Users.txt")){
                file = f;
            }
        }
    }

    public void loudUsers(){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String s ;
            while ((s = reader.readLine())!=null){
                String[] cred = s.split(",");
                User user = new User();
                user.setUserData(cred);
                userList.add(user);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public User findUser(String userName){
        for (User user : userList) {
            if (user.getUsrName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    public void changeUserCredential(User newUserCred, boolean deleteFlag){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            File tempFile = new File(App.ROOT_DIR,"temp.txt");
            PrintWriter writer = new PrintWriter(tempFile);
            String line;
            while ((line = reader.readLine())!=null){
                String[] cred = line.split(",");
                if (cred.length>0 && cred[0].equals(newUserCred.getUsrName())){
                    if (deleteFlag) {
                        continue;
                    }
                    String[] newCred = newUserCred.getUserData();
                    StringBuilder builder = new StringBuilder();
                    for (String s : newCred){
                        builder.append(s).append(",");
                    }
                    builder.deleteCharAt(builder.length()-1);
                    line = builder.toString();
                }
                writer.append(line).append("\n");
            }
            writer.flush();
            writer.close();
            if (!file.delete()) {
                Log.e("database", "file not deleted");
                return;
            }
            if (!tempFile.renameTo(file)){
                Log.e("database","file not renamed");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addUser(String userName, String password) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(file));
        File tempFile = new File(App.ROOT_DIR,"temp.txt");
        PrintWriter writer = new PrintWriter(tempFile);
        String line;
        while ((line = reader.readLine())!=null){
            writer.append(line).append("\n");
        }
        writer.append(userName).append(",").append(password).append(",f,f,f,f");
        writer.flush();
        writer.close();
        if (!file.delete()) {
            Log.e("database", "file not deleted");
            return;
        }
        if (!tempFile.renameTo(file)){
            Log.e("database","file not renamed");
        }
    }
}
