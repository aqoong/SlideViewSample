package com.aqoong.lib.slideview;

/**
 * Created by andy on 2020-01-28.
 * <p>
 * email : han.andy@huinno.co
 **/
public interface ScrollPositionListener {


    void nowScrollPosition(int position);
    void onEvent(SlideView.ScrollStatus status);
}
