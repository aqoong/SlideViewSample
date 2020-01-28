package com.aqoong.lib.slideview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by andy on 2020-01-15.
 * <p>
 * email : han.andy@huinno.co
 **/
public class SlideView extends RecyclerView {
    private final String TAG = getClass().getSimpleName();
    private SlideViewAdapter adapter;
    private int curScrollPosition = 0;

    private ScrollPositionListener listener;

    public SlideView(Context context) {
        this(context, null);
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlideView);
        try{
            duration = ta.getInt(R.styleable.SlideView_moveDuration, 30);
            pixelsToMove = ta.getInt(R.styleable.SlideView_pixelMove, 50);
        }catch (Exception e){

        }finally {
            ta.recycle();
        }

        setup();
    }

    public void setSlideViewListener(ScrollPositionListener listener){
        this.listener = listener;
    }

    int duration = 30;
    int pixelsToMove = 50;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable SCROLLING_RUNNABLE = new Runnable() {

        @Override
        public void run() {
            smoothScrollBy(pixelsToMove, 0);
            mHandler.postDelayed(this, duration);
        }
    };



    private void setup(){
        adapter = new SlideViewAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        this.setLayoutManager(layoutManager);
        this.setAdapter(adapter);

        this.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                int lastItem = layoutManager.findFirstVisibleItemPosition();

                if(listener != null){
                    listener.nowScrollPosition(curScrollPosition);
                }

//                Log.d(TAG, ""+lastItem);
                if(lastItem != curScrollPosition && layoutManager.findViewByPosition(lastItem) instanceof ImageView){
                    curScrollPosition = lastItem;

                    mHandler.removeCallbacks(SCROLLING_RUNNABLE);
                    mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);

                }



                if(lastItem+1 >= layoutManager.getItemCount()){
                    mHandler.removeCallbacks(SCROLLING_RUNNABLE);
                    curScrollPosition = 0;
                    Handler postHandler = new Handler();
                    postHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "restart scroll");
                            if(listener != null){
                                listener.onEvent(ScrollStatus.LAST);
                            }
                            setAdapter(null);
                            setAdapter(adapter);
                            mHandler.post(SCROLLING_RUNNABLE);
                        }
                    });
                }

                if(listener != null){
                    ScrollStatus status = ScrollStatus.ROLLING;
                    if(curScrollPosition == 0){
                        status = ScrollStatus.FIRST;
                    }
                    listener.onEvent(status);
                }
            }
        });
        mHandler.post(SCROLLING_RUNNABLE);
    }


    public void addFlowObject(FlowObject.Builder builder){
        adapter.addItem(new FlowObject(getContext(), builder));
        adapter.notifyDataSetChanged();
    }

    public void addFlowObject(FlowObject object){
        adapter.addItem(object);
        adapter.notifyDataSetChanged();
    }

    public void clearObjectList(){
        adapter.clearItems(this.adapter);
        adapter.notifyDataSetChanged();
    }

    public void removeItem(int position){
        adapter.removeItem(position, this.adapter);
        this.adapter.notifyDataSetChanged();
    }


    private class SlideViewAdapter extends RecyclerView.Adapter<ViewHolder>{
        private ArrayList<FlowObject> slideList = new ArrayList<>();
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(viewType == 0){
                return new ImageViewHolder(createImageView());
            }else if(viewType == 1){
                return new TextViewHolder(createTextView());
            }else{
                return new EmptyHolder(createEmptyView());
            }
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
            }else if(holder instanceof TextViewHolder){
                object = slideList.get(position);
                ((TextViewHolder)holder).textView.setText(object.getText());
                ((TextViewHolder)holder).textView.setTextColor(object.getTextColor());
                ((TextViewHolder)holder).textView.setBackgroundColor(object.getBackgroundColor());
                ((TextViewHolder)holder).textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, object.getTextSize());
                if(object.isAlpha())
                {
                    ((TextViewHolder)holder).textView.setTextColor(object.getBackgroundColor());
                    ((TextViewHolder)holder).textView.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
                }
            }else{
                object = slideList.get(position);
                ((EmptyHolder)holder).view.setBackgroundColor(object.getBackgroundColor());
                ((EmptyHolder)holder).view.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
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
            LinearLayout.LayoutParams commonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(commonParams);

            return imageView;
        }

        private AppCompatTextView createTextView(){
            LinearLayout.LayoutParams commonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            AppCompatTextView textView = new AppCompatTextView(getContext());
            textView.setLayoutParams(commonParams);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);

            return textView;
        }

        private View createEmptyView(){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            View view = new View(getContext());
            view.setLayoutParams(params);
            return view;
        }


        public void addItem(FlowObject object){
            slideList.add(object);
        }

        public void removeItem(FlowObject object, SlideViewAdapter adapter){
            if(object != null && slideList.contains(object)) {
                setAdapter(null);
                slideList.remove(object);
                setAdapter(this);
            }
        }
        public FlowObject getItem(int position){
            if(position < slideList.size() && position > -1){
                return slideList.get(position);
            }
            return null;
        }

        public void removeItem(int position, SlideViewAdapter adapter){
            if(position < slideList.size() && position > -1) {
                this.removeItem(slideList.get(position), adapter);
            }
        }

        public void clearItems(SlideViewAdapter adapter){
            if(slideList != null && !slideList.isEmpty()) {
                for (int i = slideList.size() - 1; i >= 0; i--) {
                    this.removeItem(i, adapter);
                }
            }
        }

        private class ImageViewHolder extends ViewHolder {
            ImageView imageView;
            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = (ImageView)itemView;
//                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_image);
//                imageView.setAnimation(animation);
            }
        }

        private class TextViewHolder extends ViewHolder{
            AppCompatTextView textView;

            public TextViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = (AppCompatTextView)itemView;
//                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_image);
//                textView.setAnimation(animation);
            }
        }

        private class EmptyHolder extends ViewHolder{
            View view;
            public EmptyHolder(@NonNull View itemView) {
                super(itemView);
                view = itemView;
//                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_image);
//                view.setAnimation(animation);
            }
        }
    }

    public enum ScrollStatus{
        FIRST,
        ROLLING,
        LAST
    }


}
