package com.aqoong.lib.slideviewsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.aqoong.lib.slideview.FlowObject;
import com.aqoong.lib.slideview.SlideView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SlideView slideView = findViewById(R.id.slide);
        slideView.addFlowObject(new FlowObject.Builder());
        slideView.addFlowObject(new FlowObject.Builder(R.drawable.ic_launcher_foreground, "#FFFFFF", false));
        slideView.addFlowObject(new FlowObject.Builder(R.drawable.ic_launcher_foreground, "#FFFFFF", false));
        slideView.addFlowObject(new FlowObject.Builder(R.drawable.ic_launcher_foreground, "#FFFFFF", false));
        slideView.addFlowObject(new FlowObject.Builder("TESTTESTESTESTESTJKDLFNDKLGNDISTJEISLKJFDLFKNSFKLDSNMFLKDNFKLDSNFDSKLFNDKLSFNDKLSFNDSFfdsakdsanvkdlsnavkldsnmvkldsnvkldsanvkldsa2121212121",
                "#FFFFFF",
                50,
                "#000000",
                false
                ));
//        slideView.addFlowObject(new FlowObject.Builder("TESTTESTESTESTESTJKDLFNDKLGNDISTJEISLKJFDLFKNSFKLDSNMFLKDNFKLDSNFDSKLFNDKLSFNDKLSFNDSF",
//                "#FFFFFF",
//                "#000000",
//                false
//        ));
//        slideView.addFlowObject(new FlowObject.Builder("TESTTESTESTESTESTJKDLFNDKLGNDISTJEISLKJFDLFKNSFKLDSNMFLKDNFKLDSNFDSKLFNDKLSFNDKLSFNDSF",
//                "#FFFFFF",
//                "#000000",
//                false
//        ));
        slideView.addFlowObject(new FlowObject.Builder());

        slideView.invalidate();
    }

}
