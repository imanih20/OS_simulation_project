package com.example.OsProject.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.OsProject.R;
import com.example.OsProject.fragments.CommandFragment;
import com.example.OsProject.models.User;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent()!=null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, CommandFragment.newInstance(getIntent().getParcelableExtra(User.USER_KEY)))
                    .commit();
        }
    }
}
