package com.example.OsProject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.example.OsProject.R;
import com.example.OsProject.activities.App;
import com.example.OsProject.activities.MainActivity;
import com.example.OsProject.models.User;
import com.example.OsProject.utils.LogDb;
import com.example.OsProject.utils.UserDb;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

public class QuestFragment extends Fragment {
    public static QuestFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putParcelable(User.USER_KEY,user);
        QuestFragment fragment = new QuestFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private AppCompatSpinner questSpinner;
    private AppCompatCheckBox addCB;
    private TextInputLayout questInputLayout;
    private TextInputEditText questInput;
    private TextInputEditText answerInput;
    private AppCompatButton saveBtn;
    private AppCompatTextView errorTv;
    private UserDb db;
    private User user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest,container,false);
        questSpinner = view.findViewById(R.id.quest_spinner);
        addCB = view.findViewById(R.id.Add_cb);
        questInputLayout = view.findViewById(R.id.quest_input_layout);
        questInput = view.findViewById(R.id.quest_input);
        answerInput = view.findViewById(R.id.answer_input);
        saveBtn = view.findViewById(R.id.save_btn);
        errorTv = view.findViewById(R.id.error_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments()!=null){
            user = getArguments().getParcelable(User.USER_KEY);
        }
        db = new UserDb();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.quest_spinner_items)
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questSpinner.setAdapter(adapter);
        addCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                questInputLayout.setVisibility(View.VISIBLE);
                questSpinner.setVisibility(View.GONE);
            }else {
                questSpinner.setVisibility(View.VISIBLE);
                questInputLayout.setVisibility(View.GONE);
            }
        });
        saveBtn.setOnClickListener(v -> save());
        questInput.setOnKeyListener((v, keyCode, event) -> keyCode == KeyEvent.KEYCODE_ENTER);
        answerInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                save();
            }
            return false;
        });
    }
    public void save(){
        String question;
        String answer;
        if (addCB.isChecked()){
            if (questInput.getText()!=null && !questInput.getText().toString().equals("")){
                question = questInput.getText().toString();
            }else {
                errorTv.setText(R.string.quest_error);
                return;
            }
        }else {
            question = (String) questSpinner.getSelectedItem();
        }
        if (answerInput.getText()!=null && !answerInput.getText().toString().equals("")){
            answer = answerInput.getText().toString();
            if (answer.equals("f") || question.equals("f")){
                errorTv.setText(R.string.quest_error3);
            }
            user.setBackupQuest(question);
            user.setBackupAnswer(answer);
            if (!createUserHomeDir()) return;
            db.changeUserCredential(user,false);
            LogDb logDb = new LogDb(user.getUsrName());
            logDb.addLog("user entered for the first time.");
            if (getActivity()!=null) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(User.USER_KEY,user);
                startActivity(intent);
                getActivity().finish();
            }
        }else {
            errorTv.setText(R.string.quest_error2);
        }
    }
    public boolean createUserHomeDir(){
        File file = new File(App.HOME_DIR,user.getUsrName());
        if (file.exists()){
            return true;
        }else {
            return file.mkdirs();
        }
    }
}
