package com.example.fightandroid.util;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.example.fightandroid.R;
import com.example.fightandroid.response.MessageResponse;

import java.io.IOException;

import retrofit2.Response;

public class Helper {




    public static void showDialog(String message, Context context){
        Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.dialog_notification);
        TextView textView=dialog.findViewById(R.id.tvMessage);
        textView.setText(message);
        Button btnClose=dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();


    }

    public static String removeSpace(String input) {
        return input.replaceAll("\\s+", "");
    }


    public static Dialog createDialogLoad(Context context) {
        Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.dialog_load);
        return dialog;
    }




    public static void handlerErrorResponse(Response response, Context context){
        try {
            MessageResponse errorResponse= GsonObject.instance().fromJson(response.errorBody().string(), MessageResponse.class);
            if (errorResponse != null && "fail".equals(errorResponse.getStatus())) {
               showDialog(errorResponse.getMessage(),context);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
