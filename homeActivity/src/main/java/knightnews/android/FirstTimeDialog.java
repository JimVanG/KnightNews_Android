package knightnews.android;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by James Van Gaasbeck on 6/2/14.
 */
public class FirstTimeDialog extends DialogFragment {

    private boolean mShouldDismiss = false;

    public static FirstTimeDialog newInstance() {
        FirstTimeDialog frag = new FirstTimeDialog();
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        final View view = getActivity().getLayoutInflater().inflate(R.layout
                .dialogue_first_time, null);

        //final Drawable d = new ColorDrawable(getResources().getColor(R.color.Black));

        final TextView tv = (TextView) view.findViewById(R.id.textView);
        final ImageView iv = (ImageView) view.findViewById(R.id.helperImage);

        final Button butt = (Button) view.findViewById(R.id.only_button);
        butt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mShouldDismiss) {
                    butt.setText(android.R.string.ok);
                    tv.setText("To View More Stories...");
                    iv.setImageResource(R.drawable.knswipe);
                    mShouldDismiss = true;
                }else{
                    dialog.dismiss();
                }
            }
        });

        //dialog.getWindow().setBackgroundDrawable(d);
        dialog.getWindow().setContentView(view);

        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;

        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}