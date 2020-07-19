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

package com.saggitt.omega.qsb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.android.launcher3.Launcher;
import com.android.launcher3.R;

public class QsbActionMode implements ActionMode.Callback {
    final AbstractQsbLayout qsbLayout;
    private final String clipboardText;
    private final Intent settingsBroadcast;
    private final Intent settingsIntent;

    public QsbActionMode(AbstractQsbLayout layout, String clipboardText, Intent settingBroadcast, Intent settingsIntent) {
        this.qsbLayout = layout;
        this.clipboardText = clipboardText;
        this.settingsBroadcast = settingBroadcast;
        this.settingsIntent = settingsIntent;
    }

    @SuppressLint("ResourceType")
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.setTitle(null);
        actionMode.setSubtitle(null);
        actionMode.setTitleOptionalHint(true);
        actionMode.setTag(Launcher.AUTO_CANCEL_ACTION_MODE);
        if (clipboardText != null) {
            menu.add(0, 16908322, 0, 17039371).setShowAsAction(1);
        }
        if (settingsBroadcast != null || settingsIntent != null) {
            menu.add(0, R.id.hotseat_qsb_menu_item, 0, R.string.customize).setShowAsAction(8);
        }
        return clipboardText != null || settingsBroadcast != null || settingsIntent != null;
    }

    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return true;
    }

    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        if (menuItem.getItemId() == 16908322 && !TextUtils.isEmpty(this.clipboardText)) {
            this.qsbLayout.startSearch(this.clipboardText, 3);
            actionMode.finish();
            return true;
        } else if (menuItem.getItemId() == R.id.hotseat_qsb_menu_item && !(this.settingsBroadcast == null && this.settingsIntent == null)) {
            if (settingsBroadcast != null) {
                this.qsbLayout.getContext().sendBroadcast(this.settingsBroadcast);
            } else if (settingsIntent != null) {
                this.qsbLayout.getContext().startActivity(settingsIntent);
            }
            actionMode.finish();
            return true;
        }
        return false;
    }

    public void onDestroyActionMode(ActionMode actionMode) {
    }
}
