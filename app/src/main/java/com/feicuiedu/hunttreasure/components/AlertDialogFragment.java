package com.feicuiedu.hunttreasure.components;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.feicuiedu.hunttreasure.R;

/**
 * Created by gqq on 17/1/1.
 */

/**
 * 自定义一个对话框，用于提示用户名和密码错误的提示
 */
// http://www.cnblogs.com/mercuryli/archive/2016/04/09/5372496.html
public class AlertDialogFragment extends DialogFragment{


    private static final String KEY_TITLE = "key_title";
    private static final String KEY_MESSAGE = "key_message";

    public static AlertDialogFragment getInstances(String title, String message){

        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE,title);
        bundle.putString(KEY_MESSAGE,message);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString(KEY_TITLE);
        String message = getArguments().getString(KEY_MESSAGE);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                // 设置中立的按钮
                .setNeutralButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }
}
