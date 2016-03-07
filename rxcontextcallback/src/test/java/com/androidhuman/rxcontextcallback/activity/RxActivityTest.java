package com.androidhuman.rxcontextcallback.activity;

import com.androidhuman.rxcontextcallback.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import rx.Subscription;
import rx.observers.TestSubscriber;

import static org.assertj.android.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RxActivityTest {

    @Test
    public void startsActivityForResult() {
        Activity activity = new Activity() {
            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                RxActivity.onActivityResult(requestCode, resultCode, data);
            }
        };

        Intent requestIntent = new Intent().setType("image/*");
        int requestCode = 100;
        Intent resultIntent = new Intent().setData(Uri.parse("content://foo"));

        TestSubscriber<ActivityResultEvent> sub = new TestSubscriber<>();
        Subscription s = RxActivity.startsActivityForResult(activity, requestIntent, requestCode)
                .subscribe(sub);

        // Set activity result
        Shadows.shadowOf(activity).receiveResult(requestIntent, Activity.RESULT_OK, resultIntent);

        s.unsubscribe();

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        ActivityResultEvent ev = sub.getOnNextEvents().get(0);

        assertThat(ev.requestCode())
                .isEqualTo(requestCode);

        assertThat(ev.resultCode())
                .isEqualTo(Activity.RESULT_OK);

        assertThat(ev.data())
                .isEqualTo(resultIntent)
                .hasData(Uri.parse("content://foo"));
    }
}
