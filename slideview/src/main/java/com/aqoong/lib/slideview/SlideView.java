package com.aqoong.lib.slideview;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by andy on 2020-01-15.
 * <p>
 * email : han.andy@huinno.co
 **/
public class SlideView extends RecyclerView {
    public SlideViewAdapter adapter;
    public SlideView(Context context) {
        this(context, null);
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setup();
    }

    private void setup(){
        adapter = new SlideViewAdapter();

        this.setAdapter(adapter);
    }

    @Override
    public void smoothScrollToPosition(int position) {

    }

    public void addFlowObject(FlowObject.Builder builder){
        adapter.addItem(new FlowObject(getContext(), builder, null));
        adapter.notifyDataSetChanged();
    }




    private class SlideViewAdapter extends RecyclerView.Adapter<ViewHolder>{
        private ArrayList<FlowObject> slideList = new ArrayList<>();
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(viewType == 0){
                View v = createImageView();
                return new ImageViewHolder(v);
            }else if(viewType == 1){
                View v = createTextView();
                return new TextViewHolder(v);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            FlowObject object = null;

            if(holder instanceof ImageViewHolder){
                object = slideList.get(position);
                ((ImageViewHolder)holder).imageView.setImageResource(object.getImageRes());
                if(object.isAlpha()){
                    ((ImageViewHolder)holder).imageView.setColorFilter(object.getBackgroundColor());
                    ((ImageViewHolder)holder).imageView.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
                }
                ((ImageViewHolder)holder).imageView.setBackgroundColor(object.getBackgroundColor());
            }else{
                object = slideList.get(position);
                ((TextViewHolder)holder).textView.setText(object.getText());
                ((TextViewHolder)holder).textView.setTextColor(object.getTextColor());
                ((TextViewHolder)holder).textView.setBackgroundColor(object.getBackgroundColor());

                if(object.isAlpha())
                {
                    ((TextViewHolder)holder).textView.setTextColor(object.getBackgroundColor());
                    ((TextViewHolder)holder).textView.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
                }
            }
        }

        @Override
        public int getItemCount() {
            return slideList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return slideList.get(position).getType();
        }

        private ImageView createImageView(){
            LinearLayout.LayoutParams commonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(commonParams);

            return imageView;
        }

        private AppCompatTextView createTextView(){
            LinearLayout.LayoutParams commonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            AppCompatTextView textView = new AppCompatTextView(getContext());
            textView.setLayoutParams(commonParams);

            return textView;
        }


        public void addItem(FlowObject object){
            slideList.add(object);
            notifyDataSetChanged();
        }

        public void removeItem(FlowObject object, SlideViewAdapter adapter){

        }

        private class ImageViewHolder extends ViewHolder {
            ImageView imageView;
            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = (ImageView)itemView;
            }
        }

        private class TextViewHolder extends ViewHolder{
            AppCompatTextView textView;

            public TextViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = (AppCompatTextView)itemView;
            }
        }
    }
}
