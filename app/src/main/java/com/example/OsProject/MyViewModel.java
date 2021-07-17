package com.example.OsProject;

import androidx.lifecycle.ViewModel;

import com.example.OsProject.core.CommandExecutor;
import com.example.OsProject.core.CommandInitializer;
import com.example.OsProject.models.CommandInput;
import com.example.OsProject.models.User;

import java.util.ArrayList;

public class MyViewModel extends ViewModel {
    public User user;
    public CommandInitializer initializer;
    public ArrayList<CommandInput> inputList = new ArrayList<>();
}
