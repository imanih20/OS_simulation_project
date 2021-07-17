package com.example.OsProject.fragments;

import android.os.Bundle;
import android.view.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OsProject.MyViewModel;
import com.example.OsProject.R;
import com.example.OsProject.adapters.CommandListAdapter;
import com.example.OsProject.core.CommandExecutor;
import com.example.OsProject.core.CommandInitializer;
import com.example.OsProject.models.User;

public class CommandFragment extends Fragment {
    private RecyclerView commandListView2;
    private CommandListAdapter adapter2;
    private MyViewModel myViewModel;
    private Toolbar toolbar;
    public static CommandFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putParcelable(User.USER_KEY,user);
        CommandFragment fragment = new CommandFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_command_list, container, false);
        commandListView2 = view.findViewById(R.id.list2);
        toolbar = view.findViewById(R.id.toolbar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User user = new User();
        if (getArguments()!=null) {
            user = getArguments().getParcelable(User.USER_KEY);
        }
        myViewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        if (getActivity()!=null){
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);
            assert activity.getSupportActionBar()!=null;
            activity.getSupportActionBar().setTitle("");
        }
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
//        commandListview.setDivider(null);
        commandListView2.setLayoutManager(new LinearLayoutManager(requireContext()));
        if (getContext()!=null) {
            if (myViewModel.initializer == null){
                myViewModel.initializer = new CommandInitializer(user);
            }
            myViewModel.initializer.init((AppCompatActivity) requireActivity());
            adapter2 = new CommandListAdapter(requireContext(),myViewModel);
        }
        commandListView2.setAdapter(adapter2);
        setMenuVisibility(true);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.clear_list) {
            clearList();
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void clearList() {
        if (getContext()!=null) {
            myViewModel.inputList.clear();
            adapter2 = new CommandListAdapter(requireContext(),myViewModel);
            commandListView2.setAdapter(adapter2);
        }
    }

}