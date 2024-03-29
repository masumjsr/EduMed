package com.edu.med.mr;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.edu.med.R;
import com.edu.med.mr.utils.Utils;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.snackbar.Snackbar;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ImageViewActivity extends AppCompatActivity {
ConstraintLayout constraintLayout;
    private long downloadID;

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the img3 id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued img3 by matching img3 id
            if (downloadID == id) {
                Snackbar.make(constraintLayout, "ফাইলটি সেভ হয়েছে দেখুন!", Snackbar.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        PhotoView photoView=findViewById(R.id.image_view);
        Log.i("123321", "51:"+getIntent().getStringExtra(Utils.post_image));
        Picasso.get().load(getIntent().getStringExtra(Utils.post_image)).into(photoView);


    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        {
            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    //  Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    // SaveImage(ViewActivity.this, BASE + image);

                        //  SaveImage(ViewActivity.this, BASE + image);
                        beginDownload();


                }

                @Override
                public void onPermissionDenied(List<String> deniedPermissions) {
                    Toast.makeText(getApplicationContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                }


            };
            TedPermission.with(this)
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .check();

        }
        return super.onOptionsItemSelected(item);

    }
    private void beginDownload() {
        Snackbar.make(constraintLayout, "ডাউনলোড হচ্ছে। অপেক্ষা করুন...", Snackbar.LENGTH_SHORT).show();
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        /*
        Create a DownloadManager.Request with all the information necessary to start the img3
         */
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(getIntent().getStringExtra(Utils.post_image)))
                .setTitle("image")// Title of the Download Notification
                .setDescription("Downloading")// Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)// Visibility of the img3 Notification
                .setDestinationUri(Uri.fromFile(file))// Uri of the destination file

                .setAllowedOverMetered(true)// Set if img3 is allowed on Mobile network
                .setAllowedOverRoaming(true);// Set if img3 is allowed on roaming network
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadID = downloadManager.enqueue(request);// enqueue puts the img3 request in the queue.
    }
}
