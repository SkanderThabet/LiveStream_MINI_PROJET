/*
 * Copyright (C) 2021 pedroSG94.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.projet.miniprojet.androidVox.activities.BroadcastStreaming;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.projet.miniprojet.androidVox.R;
import com.projet.miniprojet.androidVox.activities.BroadcastStreaming.customexample.RtmpActivity;
import com.projet.miniprojet.androidVox.activities.BroadcastStreaming.customexample.RtspActivity;
import com.projet.miniprojet.androidVox.activities.BroadcastStreaming.defaultexample.ExampleRtmpActivity;
import com.projet.miniprojet.androidVox.activities.BroadcastStreaming.defaultexample.ExampleRtspActivity;
import com.projet.miniprojet.androidVox.activities.BroadcastStreaming.utils.ActivityLink;
import com.projet.miniprojet.androidVox.activities.BroadcastStreaming.utils.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class BroadcastMain extends AppCompatActivity implements AdapterView.OnItemClickListener {

  private GridView list;
  private List<ActivityLink> activities;

  private final String[] PERMISSIONS = {
      Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
      Manifest.permission.WRITE_EXTERNAL_STORAGE
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    overridePendingTransition(R.transition.slide_in, R.transition.slide_out);
//    TextView tvVersion = findViewById(R.id.tv_version);
//    tvVersion.setText(getString(R.string.version, BuildConfig.VERSION_NAME));

    list = findViewById(R.id.list);
    createList();
    setListAdapter(activities);

    if (!hasPermissions(this, PERMISSIONS)) {
      ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
    }
  }

  private void createList() {
    activities = new ArrayList<>();
    activities.add(
        new ActivityLink(new Intent(this, RtmpActivity.class), getString(R.string.rtmp_streamer),
            JELLY_BEAN));
    activities.add(
        new ActivityLink(new Intent(this, RtspActivity.class), getString(R.string.rtsp_streamer),
            JELLY_BEAN));
    activities.add(new ActivityLink(new Intent(this, ExampleRtmpActivity.class),
        getString(R.string.default_rtmp), JELLY_BEAN));
    activities.add(new ActivityLink(new Intent(this, ExampleRtspActivity.class),
        getString(R.string.default_rtsp), JELLY_BEAN));
  }

  private void setListAdapter(List<ActivityLink> activities) {
    list.setAdapter(new ImageAdapter(activities));
    list.setOnItemClickListener(this);
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    if (hasPermissions(this, PERMISSIONS)) {
      ActivityLink link = activities.get(i);
      int minSdk = link.getMinSdk();
      if (Build.VERSION.SDK_INT >= minSdk) {
        startActivity(link.getIntent());
        overridePendingTransition(R.transition.slide_in, R.transition.slide_out);
      } else {
        showMinSdkError(minSdk);
      }
    } else {
      showPermissionsErrorAndRequest();
    }
  }

  private void showMinSdkError(int minSdk) {
    String named;
    switch (minSdk) {
      case JELLY_BEAN_MR2:
        named = "JELLY_BEAN_MR2";
        break;
      case LOLLIPOP:
        named = "LOLLIPOP";
        break;
      default:
        named = "JELLY_BEAN";
        break;
    }
    Toast.makeText(this, "You need min Android " + named + " (API " + minSdk + " )",
        Toast.LENGTH_SHORT).show();
  }

  private void showPermissionsErrorAndRequest() {
    Toast.makeText(this, "You need permissions before", Toast.LENGTH_SHORT).show();
    ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
  }

  private boolean hasPermissions(Context context, String... permissions) {
    if (context != null && permissions != null) {
      for (String permission : permissions) {
        if (ActivityCompat.checkSelfPermission(context, permission)
            != PackageManager.PERMISSION_GRANTED) {
          return false;
        }
      }
    }
    return true;
  }
}