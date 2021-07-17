package com.example.OsProject.fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.example.OsProject.R;
import com.example.OsProject.models.User;
import com.google.android.material.textfield.TextInputEditText;

public class ForgetPassFragment extends Fragment {
    public static ForgetPassFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putParcelable(User.USER_KEY,user);
        ForgetPassFragment fragment = new ForgetPassFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private TextInputEditText IdInput;
    private AppCompatTextView questTv;
    private AppCompatTextView errorTv;
    private TextInputEditText answerInput;
    private AppCompatButton checkBtn;
    private LinearLayoutCompat checkCredLayout;
    private User user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget, container, false);
        IdInput = view.findViewById(R.id.old_password_input);
        questTv = view.findViewById(R.id.question_tv);
        answerInput = view.findViewById(R.id.answer_input);
        checkBtn = view.findViewById(R.id.check_btn);
        errorTv = view.findViewById(R.id.error_view);
        checkCredLayout = view.findViewById(R.id.check_cred_layout);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments()!=null){
            user = getArguments().getParcelable(User.USER_KEY);
        }
        if (user.getPassword().equals("f")) {
            checkCredLayout.setVisibility(View.GONE);
        }
        questTv.setText(user.getBackupQuest());
        checkBtn.setOnClickListener(v -> check());
        IdInput.setOnKeyListener((v, keyCode, event) -> keyCode == KeyEvent.KEYCODE_ENTER);
        answerInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                check();
            }
            return false;
        });
    }

    private void check(){
        if (IdInput.getText()!=null && !IdInput.getText().toString().equals("")){
            if (answerInput.getText()!=null && !answerInput.getText().toString().equals("")){
                String id = IdInput.getText().toString();
                String answer = answerInput.getText().toString();
                if (user.getInitialPassword().equals(id) && user.getBackupAnswer().equals(answer)){
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,ChangePassFragment.newInstance(user,false))
                            .commit();

                }else {
                    errorTv.setText(R.string.incorrect_answer_error);
                }
            }else {
                errorTv.setText(R.string.answer_error);
            }
        }else {
            errorTv.setText(R.string.id_error);
        }
    }
}