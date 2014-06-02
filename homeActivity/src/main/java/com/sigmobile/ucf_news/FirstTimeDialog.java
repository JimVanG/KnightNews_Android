package com.sigmobile.ucf_news;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by jjvg on 6/2/14.
 */
public class FirstTimeDialog extends DialogFragment {

    public static FirstTimeDialog newInstance() {
        FirstTimeDialog frag = new FirstTimeDialog();
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        final View view = getActivity().getLayoutInflater().inflate(R.layout
                .dialogue_first_time, null);

        final Drawable d = new ColorDrawable(getResources().getColor(R.color.Black));

        dialog.getWindow().setBackgroundDrawable(d);
        dialog.getWindow().setContentView(view);

        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;

        dialog.setCanceledOnTouchOutside(true);



        return dialog;
    }
}