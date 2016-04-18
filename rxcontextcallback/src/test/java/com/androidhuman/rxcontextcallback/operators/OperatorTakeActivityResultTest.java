package com.androidhuman.rxcontextcallback.operators;

import com.androidhuman.rxcontextcallback.BuildConfig;
import com.androidhuman.rxcontextcallback.activity.ActivityResultEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import android.app.Activity;
import android.content.Intent;

import rx.Observable;
import rx.Subscription;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class OperatorTakeActivityResultTest {

    @Test
    public void testTakesActivityResult() {
        int requestCode = 10;
        Intent data = new Intent();
        data.putExtra("text", "foobar");

        TestSubscriber<ActivityResultEvent> sub = new TestSubscriber<>();

        // Test with ActivityResultEvents:
        // 1,2: Events with different request code: Should ignore and continue waiting for result
        // 3:   Event with matching request code. Should finish taking the events after all
        // 4:   Event with matching request code but after finished the taking procedure, no-op.
        Subscription s = Observable.from(
                new ActivityResultEvent[]{
                        newEvent(11, Activity.RESULT_CANCELED, null),
                        newEvent(9, Activity.RESULT_OK, data),
                        newEvent(requestCode, Activity.RESULT_OK, data),
                        newEvent(requestCode, Activity.RESULT_CANCELED, data)
                })
                .lift(new OperatorTakeActivityResult(requestCode))
                .subscribe(sub);
        s.unsubscribe();

        sub.assertNoErrors();
        sub.assertCompleted();
        sub.assertValueCount(1);

        ActivityResultEvent ev = sub.getOnNextEvents().get(0);
        assertThat(ev.requestCode())
                .isEqualTo(10);
        assertThat(ev.resultCode())
                .isEqualTo(Activity.RESULT_OK);
        assertThat(ev.data())
                .isNotNull();
        assertThat(ev.data().getStringExtra("text"))
                .isEqualTo("foobar");
    }

    private ActivityResultEvent newEvent(int requestCode, int resultCode, Intent data) {
        return ActivityResultEvent.create(requestCode, resultCode, data);
    }

}
