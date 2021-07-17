package com.example.OsProject.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.OsProject.R;
import com.example.OsProject.fragments.LoginFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findUsersFile();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, LoginFragment.newInstance())
                .commit();

    }
    private void findUsersFile(){
        try {
            File[] files = App.ROOT_DIR.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isFile() && file.getName().equals("Users.txt")) {
                    return;
                }
            }
            File file = new File(App.ROOT_DIR,"Users.txt");
            if (!file.createNewFile()){
                Log.e("user file","file not created");
            }
            PrintWriter writer = new PrintWriter(file);
            InputStream is = getAssets().open("Users.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String s;
            while ((s=reader.readLine())!=null){
                writer.append(s).append("\n");
            }
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}