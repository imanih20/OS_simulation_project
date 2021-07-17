package com.example.OsProject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.example.OsProject.R;
import com.example.OsProject.activities.MainActivity;
import com.example.OsProject.models.User;
import com.example.OsProject.utils.LogDb;
import com.example.OsProject.utils.TextUtils;
import com.example.OsProject.utils.UserDb;
import com.google.android.material.textfield.TextInputEditText;

public class ChangePassFragment extends Fragment {
    public static ChangePassFragment newInstance(User user,boolean isNewUser) {
        Bundle args = new Bundle();
        args.putBoolean("new",isNewUser);
        args.putParcelable(User.USER_KEY,user);
        ChangePassFragment fragment = new ChangePassFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private TextInputEditText newPasswordInput;
    private TextInputEditText confirmPasswordInput;
    private AppCompatTextView errorTv;
    private AppCompatButton nextBtn;
    private User user;
    private UserDb db;
    private boolean isNewUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_pass,container,false);
        newPasswordInput = view.findViewById(R.id.new_password_input);
        confirmPasswordInput = view.findViewById(R.id.confirm_password_input);
        nextBtn = view.findViewById(R.id.next_btn);
        errorTv = view.findViewById(R.id.error_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new UserDb();
        if (getArguments()!=null) {
            user = getArguments().getParcelable(User.USER_KEY);
            isNewUser = getArguments().getBoolean("new");
        }
        if (!isNewUser) nextBtn.setText(R.string.save_btn);
        nextBtn.setOnClickListener(v-> savePassword());

        confirmPasswordInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE){
                savePassword();
            }
            return false;
        });
    }

    private void savePassword() {
        if (newPasswordInput.getText() == null || confirmPasswordInput.getText() == null) return;
        String newPass=newPasswordInput.getText().toString();
        String conPass=confirmPasswordInput.getText().toString();
        if (newPass.isEmpty()||conPass.isEmpty()){
            errorTv.setText(R.string.change_pass_error3);
            return;
        }
        if (newPass.equals(conPass)){
            if (TextUtils.isValidPass(newPass)){
                user.setPassword(newPass);
                if (isNewUser){
                    gotoNextFragment(QuestFragment.newInstance(user));
                }else{
                    db.changeUserCredential(user,false);
                    LogDb logDb = new LogDb(user.getUsrName());
                    logDb.addLog("user changed his password.");
                    if (getActivity()!=null) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra(User.USER_KEY,user);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
            }else {
                errorTv.setText(R.string.change_pass_error2);
            }
        }else {
            errorTv.setText(R.string.change_pass_error1);

        }
    }
    public void gotoNextFragment(Fragment fragment){
        if (getActivity()!=null)
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commit();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (newPasswordInput.getText()!=null && confirmPasswordInput.getText()!=null){
            outState.putString("newPass",newPasswordInput.getText().toString());
            outState.putString("confPass",confirmPasswordInput.getText().toString());
        }
    }

}
