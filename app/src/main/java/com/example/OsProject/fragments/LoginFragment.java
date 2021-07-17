package com.example.OsProject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
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
import com.example.OsProject.utils.UserDb;
import com.google.android.material.textfield.TextInputEditText;


public class LoginFragment extends Fragment {
    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private AppCompatButton loginBtn;
    private AppCompatTextView forgetTv;
    private AppCompatTextView errorTv;
    private TextInputEditText userNameInput;
    private TextInputEditText PasswordInput;
    private User user;
    UserDb db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        loginBtn = view.findViewById(R.id.login_btn);
        forgetTv = view.findViewById(R.id.forget_tv);
        userNameInput = view.findViewById(R.id.usr_name_input);
        PasswordInput = view.findViewById(R.id.password_input);
        errorTv = view.findViewById(R.id.error_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new HandlerThread("db") {
            @Override
            public void start() {
                super.start();
                db = new UserDb();
            }
        }.start();
        loginBtn.setOnClickListener(v -> login());
        forgetTv.setOnClickListener(v -> forgetPasswordAction());
        PasswordInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login();
            }
            return false;
        });
    }

    private void forgetPasswordAction() {
        if (userNameInput.getText() == null || userNameInput.getText().toString().equals("")){
            errorTv.setText(R.string.switch_forget_frag_error);
            return;
        }
        user = db.findUser(userNameInput.getText().toString());
        if (user==null){
            errorTv.setText(R.string.userName_error);
            return;
        }
        if (user.getBackupQuest().equals("f")){
            errorTv.setText(R.string.username_err2);
            return;
        }
        gotoNextFragment(ForgetPassFragment.newInstance(user));
        removeViewContents();
    }

    public void gotoNextFragment(Fragment fragment){
        if (getActivity()!=null) {
            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("login");
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }

    }
    private void removeViewContents(){
        PasswordInput.setText("");
        userNameInput.setText("");
        errorTv.setText("");
    }
    public void login(){
        if (db!=null) {
            if (userNameInput.getText()!=null){
                if (PasswordInput.getText()!=null){
                    String userName = userNameInput.getText().toString();
                    String password = PasswordInput.getText().toString();
                    user = db.findUser(userName);
                    if (userName.equals("") || password.equals("")){
                        errorTv.setText(R.string.error_1);
                        return;
                    }
                    if (user==null){
                        errorTv.setText(R.string.userName_error);
                        return;
                    }
                    if (user.getPassword().equals("f")){
                        if (user.getInitialPassword().equals(password)){
                            gotoNextFragment(ChangePassFragment.newInstance(user,user.getBackupQuest().equals("f")));
                            removeViewContents();
                        }else {
                            errorTv.setText(R.string.password_error);
                        }
                    }else {
                        if (user.getPassword().equals(password)){
                            LogDb logDb = new LogDb(user.getUsrName());
                            logDb.addLog("user entered.");
                            if (getActivity()!=null) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.putExtra(User.USER_KEY, user);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }else {
                            errorTv.setText(R.string.password_error);
                        }
                    }
                }
            }
        }
    }
}
