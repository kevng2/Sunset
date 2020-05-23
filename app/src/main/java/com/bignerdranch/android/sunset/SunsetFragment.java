package com.bignerdranch.android.sunset;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.media.MediaTimestamp;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SunsetFragment extends Fragment {
    private static final String TAG = "SunsetFragment";
    private View mSceneView;
    private View mSunView;
    private View mSkyView;
    private int mBlueSky;
    private int mSunsetSkyColor;
    private int mNightSkyColor;
    private boolean mIsSunUp = true;
    private float mSunYStart;

    public static Fragment newInstance() {
        return new SunsetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sunset, container, false);

        mSceneView = v;
        mSunView = v.findViewById(R.id.sun);
        mSkyView = v.findViewById(R.id.sky);

        Resources resources = getResources();
        mBlueSky = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);

        mSunYStart = mSunView.getTop();

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });

        return v;
    }

    private void startAnimation() {
        float sunYEnd = mSkyView.getHeight();
        ObjectAnimator heightAnimator;
        ObjectAnimator sunsetSkyAnimator;
        ObjectAnimator nightSkyAnimator;
        AnimatorSet animatorSet;
        if(mIsSunUp) {
            heightAnimator = ObjectAnimator
                    .ofFloat(mSunView, "y", mSunYStart, sunYEnd)
                    .setDuration(3000);
            heightAnimator.setInterpolator(new AccelerateInterpolator());

            sunsetSkyAnimator = ObjectAnimator
                    .ofInt(mSkyView, "backgroundColor", mBlueSky, mSunsetSkyColor)
                    .setDuration(3000);
            sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

            nightSkyAnimator = ObjectAnimator
                    .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor)
                    .setDuration(1500);
            nightSkyAnimator.setEvaluator(new ArgbEvaluator());

            animatorSet = new AnimatorSet();
            animatorSet
                    .play(heightAnimator)
                    .with(sunsetSkyAnimator)
                    .before(nightSkyAnimator);
            animatorSet.start();
            mIsSunUp = false;
        }
        else {
            nightSkyAnimator = ObjectAnimator
                    .ofInt(mSkyView, "backgroundColor", mNightSkyColor, mSunsetSkyColor)
                    .setDuration(1500);
            nightSkyAnimator.setEvaluator(new ArgbEvaluator());

            sunsetSkyAnimator = ObjectAnimator
                    .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mBlueSky)
                    .setDuration(3000);
            sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

            heightAnimator = ObjectAnimator
                    .ofFloat(mSunView, "y", sunYEnd, mSunYStart)
                    .setDuration(3000);
            heightAnimator.setInterpolator(new AccelerateInterpolator());

            animatorSet = new AnimatorSet();
            animatorSet
                    .play(nightSkyAnimator)
                    .before(sunsetSkyAnimator)
                    .with(heightAnimator);
            animatorSet.start();
            mIsSunUp = true;
        }
    }
}
