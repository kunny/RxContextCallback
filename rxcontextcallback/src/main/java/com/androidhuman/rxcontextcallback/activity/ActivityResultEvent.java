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

import android.content.Intent;

public final class ActivityResultEvent {

    public static ActivityResultEvent create(int requestCode, int resultCode, Intent data) {
        return new ActivityResultEvent(requestCode, resultCode, data);
    }

    private final int requestCode;

    private final int resultCode;

    private final Intent data;

    private ActivityResultEvent(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
    }

    public int requestCode() {
        return requestCode;
    }

    public int resultCode() {
        return resultCode;
    }

    public Intent data() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof ActivityResultEvent)) {
            return false;
        }

        ActivityResultEvent other = (ActivityResultEvent) o;
        return other.data == data()
                && other.requestCode == requestCode()
                && other.resultCode == resultCode();
    }

    @Override
    public int hashCode() {
        int result = 2;
        result = result * 8 + requestCode();
        result = result * 8 + resultCode();
        result = result * 8 + (null != data() ? data().hashCode() : 0);
        return result;
    }
}
