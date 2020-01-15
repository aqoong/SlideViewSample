package com.aqoong.lib.slideview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by andy on 2020-01-14.
 * <p>
 * email : han.andy@huinno.co
 **/
public class FlowObject {
    private final String TAG = getClass().getSimpleName();

    private Builder mBuilder;
    private String  text;
    private int     imageRes;
    private int     backgroundColor;
    private int     textColor;
    private boolean isAlpha;

    private Animation.AnimationListener animationListener;

    private View    createdView;

    private enum TYPE{
        TEXT,
        IMAGE
    }
    private TYPE type;
    private Context mContext;

    public FlowObject(Context context, Builder builder, Animation.AnimationListener animationListener){
        this.mBuilder = builder;
        this.mContext = context;
        this.text = builder.text;
        this.imageRes = builder.imageRes;
        this.backgroundColor = builder.backgroundColor;
        this.textColor = builder.textColor;
        this.type = builder.type;
        this.isAlpha = builder.isAlpha;
        this.animationListener = animationListener;
    }


    public View createFlowObjectView(){
        if(mBuilder == null){
            Log.d(TAG, "Object null Exception.");
            return null;
        }
        LinearLayout.LayoutParams commonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        switch (type){
            case IMAGE:
                ImageView imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(commonParams);
                imageView.setImageResource(imageRes);
                if(isAlpha){
                    imageView.setColorFilter(backgroundColor);
                    imageView.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
                }
                imageView.setBackgroundColor(backgroundColor);

                createdView = imageView;
                return imageView;
            case TEXT:
                AppCompatTextView textView = new AppCompatTextView(mContext);
                textView.setLayoutParams(commonParams);
                textView.setText(text);
                textView.setTextColor(textColor);
                textView.setBackgroundColor(backgroundColor);
                if(isAlpha)
                {
                    textView.setTextColor(backgroundColor);
                    textView.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
                }
                createdView = textView;
                return textView;
            default:
                return null;
        }
    }

    public int getType(){
        return this.type == TYPE.IMAGE ? 0 : 1;
    }

    public void startAnimation(){
        if(createdView != null){
            Animation animation = null;
            if(createdView instanceof ImageView){
                animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_image);
            }else if(createdView instanceof AppCompatTextView){
                animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_text);
            }
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    animationListener.onAnimationStart(animation);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    animationListener.onAnimationEnd(animation);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            createdView.startAnimation(animation);
        }
    }

    public String getText() {
        return text;
    }

    public int getImageRes() {
        return imageRes;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public boolean isAlpha() {
        return isAlpha;
    }

    public Animation.AnimationListener getAnimationListener() {
        return animationListener;
    }

    public static class Builder{
        String  text;
        int     imageRes;
        int     backgroundColor;
        int     textColor;
        TYPE    type;
        boolean isAlpha;

        public Builder(String text, String textColor, String backgroundColor, boolean alpha){
            this.text = text;
            this.textColor = Color.parseColor(textColor);
            this.backgroundColor = Color.parseColor(backgroundColor);
            this.type = TYPE.TEXT;
            this.isAlpha = alpha;
        }
        public Builder(int image, String backgroundColor, boolean alpha){
            this.imageRes = image;
            this.backgroundColor = Color.parseColor(backgroundColor);
            this.type = TYPE.IMAGE;
            this.isAlpha = alpha;
        }
    }
}
