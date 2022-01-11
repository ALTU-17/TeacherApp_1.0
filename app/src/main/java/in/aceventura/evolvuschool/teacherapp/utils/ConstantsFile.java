package in.aceventura.evolvuschool.teacherapp.utils;

import android.app.Activity;
import android.view.View;

import java.util.ArrayList;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;

public class ConstantsFile{
    public static int flagVersion = 0;

    public static void showGuideView(Activity activity, View view,String title,String subTitle){
        new GuideView.Builder(activity)
                .setTitle(title)
                .setContentText(subTitle)
                .setTargetView(view)
                .setGravity(Gravity.center)
                .setDismissType(DismissType.outside)
                .build()
                .show();

    }
}

//old guideview
/*String allotDeallotCardShowed = SharedClass.getInstance(this).getAllotDeallotNewFeature();
        if (allotDeallotCardShowed.equals("No")){

            //setting that the guideview was shown "Yes"
            SharedClass.getInstance(this).setAllotDeallotNewFeature();
        }else{
            return;
        }*/
