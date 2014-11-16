/*
 * Copyright (C) 2013 YROM.NET
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tv.acfun.a63.swipe;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;
import me.imid.swipebacklayout.lib.SwipeBackLayout.SwipeListener;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * @author Yrom
 * 
 */
public class SwipeBackHelper extends SwipeBackActivityHelper {
    private static final int VIBRATE_DURATION = 20;
    private SwipeListener mSwipeListener;
    private boolean mEnabledVibrator;
    SwipeListener mOnSwipe = new SwipeListener() {

        @Override
        public void onScrollStateChange(int state, float scrollPercent) {
            if(mSwipeListener != null)
                mSwipeListener.onScrollStateChange(state, scrollPercent);
        }

        @Override
        public void onScrollOverThreshold() {
            vibrate(VIBRATE_DURATION);
            if(mSwipeListener != null)
                mSwipeListener.onScrollOverThreshold();
        }

        @Override
        public void onEdgeTouch(int edgeFlag) {
            vibrate(VIBRATE_DURATION);
            if(mSwipeListener != null)
                mSwipeListener.onEdgeTouch(edgeFlag);
        }
    };
    private Vibrator mVibrator;

    public SwipeBackHelper(Activity activity) {
        this(activity, false);
    }
    
    public SwipeBackHelper(Activity activity, boolean enabledVibrator){
        super(activity);
        mEnabledVibrator = enabledVibrator;
        try {
            mVibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        } catch (Exception e) {
        }
    }
    @Override
    public void onPostCreate() {
        super.onPostCreate();
        getSwipeBackLayout().addSwipeListener(mOnSwipe);
        getSwipeBackLayout().setScrollThresHold(0.4f);
    }
    public void setVibratorEnabled(boolean enabledVibrator){
        mEnabledVibrator = enabledVibrator;
    }
    
    private void vibrate(int duration) {
        if(mVibrator != null && mEnabledVibrator){
            long[] pattern = { 0, duration };
            mVibrator.vibrate(pattern, -1);
        }
    }

    public void setSwipeListener(SwipeListener l) {
        mSwipeListener = l;
    }

}
