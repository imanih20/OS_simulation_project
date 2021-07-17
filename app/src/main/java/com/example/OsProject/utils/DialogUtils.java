package com.example.OsProject.utils;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;

public class DialogUtils {
    public static void transportBackGround(Dialog dialog){
        if (dialog!=null){
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    }
    public static void removeShadow(Dialog dialog){
        if (dialog!=null){
            if (dialog.getWindow() != null) {
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.dimAmount = 0;
                dialog.getWindow().setAttributes(params);
            }
        }
    }
}
