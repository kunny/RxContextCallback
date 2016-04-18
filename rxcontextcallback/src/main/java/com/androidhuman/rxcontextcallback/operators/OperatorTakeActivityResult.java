/*
 * Copyright (C) 2016 Taeho Kim <jyte82@gmail.com>
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

package com.androidhuman.rxcontextcallback.operators;

import com.androidhuman.rxcontextcallback.activity.ActivityResultEvent;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.internal.operators.OperatorFilter;
import rx.internal.operators.OperatorTakeUntilPredicate;

public class OperatorTakeActivityResult
        implements Observable.Operator<ActivityResultEvent, ActivityResultEvent> {

    private final OperatorTakeUntilPredicate<ActivityResultEvent> mOperatorTakeUntil;

    private final OperatorFilter<ActivityResultEvent> mOperatorFilter;

    public OperatorTakeActivityResult(final int requestCode) {
        mOperatorTakeUntil = new OperatorTakeUntilPredicate<>(
                new Func1<ActivityResultEvent, Boolean>() {
                    @Override
                    public Boolean call(ActivityResultEvent ev) {
                        return requestCode == ev.requestCode();
                    }
                });

        mOperatorFilter = new OperatorFilter<>(new Func1<ActivityResultEvent, Boolean>() {
            @Override
            public Boolean call(ActivityResultEvent ev) {
                return requestCode == ev.requestCode();
            }
        });
    }

    @Override
    public Subscriber<? super ActivityResultEvent> call(
            Subscriber<? super ActivityResultEvent> subscriber) {
        return mOperatorTakeUntil.call(mOperatorFilter.call(subscriber));
    }
}
