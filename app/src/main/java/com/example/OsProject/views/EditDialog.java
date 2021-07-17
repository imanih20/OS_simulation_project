package com.example.OsProject.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.OsProject.R;
import com.example.OsProject.utils.FolderUtils;

import java.util.Objects;

public class EditDialog extends AppCompatDialogFragment {
    public static EditDialog newInstance(String filePath,FolderUtils utils) {
        Bundle args = new Bundle();
        args.putString("path",filePath);
        args.putParcelable("utils",utils);
        EditDialog fragment = new EditDialog();
        fragment.setArguments(args);
        return fragment;
    }
    private AppCompatEditText editPane;
    private String filePath;
    private FolderUtils utils;
    private AppCompatButton saveBtn,cancelBtn;
    private String message;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_dialog_layout,container,false);
        editPane = view.findViewById(R.id.edit_pane);
        saveBtn = view.findViewById(R.id.save_btn);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            if (getArguments()!=null){
                filePath = getArguments().getString("path");
                utils = getArguments().getParcelable("utils");
            }
            if (getDialog()!=null) {
                saveBtn.setOnClickListener(v -> {
                    try {
                        if (editPane.getText() != null) {
                            if (utils.saveFileContent(filePath, editPane.getText().toString())) {
                                message = "change saved";
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                message = "some error happened.";
                            }
                        } else {
                            message = "some error happened.";
                        }
                        getDialog().dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                cancelBtn.setOnClickListener(v -> getDialog().dismiss());
                if (Objects.requireNonNull(getDialog()).getWindow() != null) {
                    getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            }
            editPane.setText(utils.showFileContent(filePath));
            editPane.requestFocus();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
