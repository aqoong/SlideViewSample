package com.aqoong.lib.slideview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

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
    private float   textSize;
    private int     fontRes;

    public enum TYPE{
        TEXT,
        IMAGE,
        EMPTY
    }
    private TYPE type;
    private Context mContext;

    public FlowObject(Context context, Builder builder){
        this.mBuilder = builder;
        this.mContext = context;
        this.text = builder.text;
        this.imageRes = builder.imageRes;
        this.backgroundColor = builder.backgroundColor;
        this.textColor = builder.textColor;
        this.type = builder.type;
        this.isAlpha = builder.isAlpha;
        this.textSize = builder.textSize;
        this.fontRes = builder.fontRes;
    }

    public int getType(){
        switch (type){
            case IMAGE:
                return 0;
            case TEXT:
                return 1;
            default:
                return 3;

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

    public float getTextSize() {
        return textSize;
    }

    public Builder getBuilder() {
        return mBuilder;
    }

    public void setAlpha(boolean alpha) {
        isAlpha = alpha;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public void setFontRes(int fontResID) {
        this.fontRes = fontResID;
    }

    public Typeface getFontRes(){
        return ResourcesCompat.getFont(mContext, this.fontRes);
    }

    public static class Builder{
        String  text;
        int     imageRes;
        int     backgroundColor;
        int     textColor;
        TYPE    type;
        boolean isAlpha;
        float   textSize;
        int     fontRes;

        public Builder(String text, String textColor, float textSize, String backgroundColor, boolean alpha, int fontResID){
            this.fontRes = fontResID;
            this.text = text;
            this.textColor = Color.parseColor(textColor);
            this.backgroundColor = Color.parseColor(backgroundColor);
            this.type = TYPE.TEXT;
            this.isAlpha = alpha;
            this.textSize = textSize;
        }
        public Builder(int image, String backgroundColor, boolean alpha){
            this.imageRes = image;
            this.backgroundColor = Color.parseColor(backgroundColor);
            this.type = TYPE.IMAGE;
            this.isAlpha = alpha;
        }
        public Builder(){
            this.type = TYPE.EMPTY;
            this.backgroundColor = Color.parseColor("#000000");
            this.isAlpha = true;
        }
    }
}
