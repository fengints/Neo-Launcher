/*
 *  Copyright (c) 2020 Omega Launcher
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.saggitt.omega;

import android.content.Context;
import android.os.Bundle;

import com.android.launcher3.Launcher;
import com.android.launcher3.LauncherAppState;
import com.android.launcher3.Utilities;

public class OmegaLauncher extends Launcher {

    public static final int REQUEST_PERMISSION_STORAGE_ACCESS = 666;
    public static final int REQUEST_PERMISSION_LOCATION_ACCESS = 667;

    public Context mContext;
    private boolean paused = false;
    private boolean sRestart = false;
    private OmegaPreferences mOmegaPrefs;
    private OmegaPreferencesChangeCallback prefCallback;//= new OmegaPreferencesChangeCallback(this);

    public static OmegaLauncher getLauncher(Context context) {
        if (context instanceof OmegaLauncher) {
            return (OmegaLauncher) context;
        } else {
            return (OmegaLauncher) LauncherAppState.getInstance(context).getLauncher();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mOmegaPrefs = Utilities.getOmegaPrefs(mContext);
        prefCallback = new OmegaPreferencesChangeCallback(this);
        mOmegaPrefs.registerCallback(prefCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        Utilities.getOmegaPrefs(this).unregisterCallback();

        if (sRestart) {
            sRestart = false;
            OmegaPreferences.Companion.destroyInstance();
        }
    }

    public boolean shouldRecreate() {
        return !sRestart;
    }

    public void scheduleRestart() {
        if (paused) {
            sRestart = true;
        } else {
            Utilities.restartLauncher(mContext);
        }
    }
}
