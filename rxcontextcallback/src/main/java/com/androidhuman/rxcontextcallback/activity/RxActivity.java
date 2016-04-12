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

package com.androidhuman.rxcontextcallback.activity;

import com.jakewharton.rxrelay.PublishRelay;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Observable;

public final class RxActivity {

    private static PublishRelay<Object> sBus;

    public static Observable<ActivityResultEvent> startsActivityForResult(
            @NonNull Activity activity,
            @NonNull Intent intent, int requestCode) {
        initializeBusIfNeeded();
        activity.startActivityForResult(intent, requestCode);
        return sBus.asObservable().ofType(ActivityResultEvent.class);
    }

    public static Observable<ActivityResultEvent> startsIntentSenderForResult(
            @NonNull Activity activity, @NonNull IntentSender intent, int requestCode,
            @Nullable Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) {
        initializeBusIfNeeded();
        try {
            activity.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask,
                    flagsValues, extraFlags);
        } catch (IntentSender.SendIntentException e) {
            return Observable.error(e);
        }
        return sBus.asObservable().ofType(ActivityResultEvent.class);
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        initializeBusIfNeeded();
        sBus.call(ActivityResultEvent.create(requestCode, resultCode, data));
    }

    private static void initializeBusIfNeeded() {
        if (null == sBus) {
            sBus = PublishRelay.create();
        }
    }
}
