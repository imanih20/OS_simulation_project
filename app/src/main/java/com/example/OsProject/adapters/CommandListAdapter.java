package com.example.OsProject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OsProject.MyViewModel;
import com.example.OsProject.R;
import com.example.OsProject.core.CommandExecutor;
import com.example.OsProject.models.CommandInput;

import java.util.ArrayList;

public class CommandListAdapter extends RecyclerView.Adapter<CommandListAdapter.ListViewHolder> {
    private final Context context;
    private final MyViewModel myViewModel;
    public CommandListAdapter(@NonNull Context context, MyViewModel myViewModel){
        this.context = context;
        this.myViewModel = myViewModel;
        if (myViewModel.inputList.size() == 0) {
            myViewModel.inputList.add(new CommandInput(myViewModel.initializer.getUtils().getCurrentDir()));
        }
    }
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.input_list_view,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        CommandInput input = myViewModel.inputList.get(position);
        CommandExecutor executor = new CommandExecutor(myViewModel.initializer.getCommandList());
        String prompt = input.getCurDir() + " >>";
        holder.pathTv.setText(prompt);
        if (position<myViewModel.inputList.size()-1){
            holder.commandInput.setText(input.getCommand());
            holder.resultTv.setText(input.getResult());
            if (holder.resultTv.getText() == null || holder.resultTv.getText().toString().isEmpty())
                holder.resultTv.setVisibility(View.GONE);
            else
                holder.resultTv.setVisibility(View.VISIBLE);
            holder.commandInput.setEnabled(false);
            holder.itemView.setEnabled(false);
        }else {
            holder.resultTv.setText("");
            holder.resultTv.setVisibility(View.GONE);
            holder.commandInput.setText("");
            holder.commandInput.setEnabled(true);
            holder.commandInput.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(holder.commandInput,InputMethodManager.SHOW_FORCED);
        }
        holder.commandInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
        holder.commandInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE){
                if (holder.commandInput.getText()!=null) {
                    String command=holder.commandInput.getText().toString();
                    if (command.equals("clear")){
                        myViewModel.inputList = new ArrayList<>();
                    }else {
                        executor.execute(command);
                        input.setCommand(command);
                        String result = executor.getCommandResult();
                        if (result != null && !result.isEmpty()) {
                            holder.resultTv.setVisibility(View.VISIBLE);
                            input.setResult(result);
                            holder.resultTv.setText(input.getResult());
                        }
                    }
                } else {
                    executor.setCommandResult("failed: some error happened.");
                }
                myViewModel.inputList.add(new CommandInput(myViewModel.initializer.getUtils().getCurrentDir()));
                notifyDataSetChanged();
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return myViewModel.inputList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder{
        AppCompatTextView pathTv,resultTv;
        AppCompatEditText commandInput;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            pathTv = itemView.findViewById(R.id.path_tv);
            resultTv = itemView.findViewById(R.id.command_result_tv);
            commandInput = itemView.findViewById(R.id.command_input);
        }
    }
}
