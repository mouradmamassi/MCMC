package com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.Toast;

import com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.utils.ParseConstant;
import com.l3info.utf.mm.MC.R;
import com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.adapters.SectionsPagerAdapter;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static final String  TAG = MainActivity.class.getSimpleName();
    public static final int TAK_PHOTO_REQUEST = 0;
    public static final int TAK_VIDEO_REQUEST = 1;
    public static final int PICK_PHOTO_REQUEST = 2;
    public static final int PICK_VIDEO_REQUEST = 3;

    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;
    public static final int FILE_SIZE_LIMIT = 1024*1024*10;// THIS IS JUSTE 10 MB :)



    protected Uri mMediaUri;


    public void redirectionPhoto(){
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        if(mMediaUri == null){
            Toast.makeText(MainActivity.this, R.string.error_external_storage, Toast.LENGTH_LONG ).show();
        }
        else
        {
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
            startActivityForResult(takePhotoIntent, TAK_PHOTO_REQUEST);

        }
    }

    public void redirectionChoosePhoto(){
        Intent takeChoosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
        takeChoosePhotoIntent.setType("image/*");
        startActivityForResult(takeChoosePhotoIntent, PICK_PHOTO_REQUEST);

    }

    public void redirectionChooseVideo(){
        Intent takeChooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
        takeChooseVideoIntent.setType("video/*");
        Toast.makeText(MainActivity.this, getString(R.string.warring_video_size), Toast.LENGTH_LONG).show();
        startActivityForResult(takeChooseVideoIntent, PICK_VIDEO_REQUEST);

    }

    public void redirectionVideo(){
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
        if(mMediaUri == null){
            Toast.makeText(MainActivity.this, R.string.error_external_storage, Toast.LENGTH_LONG ).show();
        }
        else
        {
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            startActivityForResult(takeVideoIntent, TAK_VIDEO_REQUEST);

        }
    }

    protected DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (which){
                case 0:
                    redirectionPhoto();
                    break;
                case 1:
                    redirectionVideo();
                    break;
                case 2:
                    redirectionChoosePhoto();
                    break;
                case 3:
                    redirectionChooseVideo();
                    break;
            }
        }
    };

    private Uri getOutputMediaFileUri(int mediaType){

        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing t

        if(isExternalStorageAvailable()){
            //get the Uri
            String appName = MainActivity.this.getString(R.string.app_name);

            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appName);

            if(! mediaStorageDir.exists()){
                if(! mediaStorageDir.mkdirs()){
                    Log.e(TAG, "Filed To Create Directory.");
                    return null;
                }
            }

            File mediaFile ;
            Date now = new Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRANCE).format(now);

            String path = mediaStorageDir.getPath()+ File.separator;
            if(mediaType == MEDIA_TYPE_IMAGE){

                mediaFile = new File(path + "IMG_" + timeStamp + ".jpg" );
            }else if(mediaType == MEDIA_TYPE_VIDEO){
                mediaFile = new File(path+ "VID_" + timeStamp + ".mp4");

            }else{
                return null;
            }

            Log.d(TAG, "File :" + Uri.fromFile(mediaFile));

            return Uri.fromFile(mediaFile);

        }
        else
        {
            return null;
        }


    }

    private boolean isExternalStorageAvailable(){
        String  state = Environment.getExternalStorageState();

        if(state.equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        myToolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.Auth_Background));
        setSupportActionBar(myToolbar);


        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser == null) {
            NavigateToLogin();
        }else {
            Log.i(TAG, " " + currentUser.getUsername());
        }

        //final ActionBar actionBar = getActionBar();
      // actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        //toolbar.add



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        setProgressBarIndeterminateVisibility(true);





        /*************************************************************/


        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);









        /**************************************************************/





    }

    private void NavigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
            switch (id){
                case  R.id.action_logout :
                    ParseUser.logOut();
                    NavigateToLogin();
                    break;
                case R.id.action_edit_friends :
                    Intent intent = new Intent(this, FirendsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.action_camera:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setItems(R.array.camera_choices, mDialogListener);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    break;


            }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){


            if(requestCode == PICK_PHOTO_REQUEST || requestCode == PICK_VIDEO_REQUEST){

                if(data == null){
                    Toast.makeText(this,getString(R.string.general_error), Toast.LENGTH_LONG).show();
                }else{
                    mMediaUri = data.getData();
                }

                if(requestCode == PICK_VIDEO_REQUEST){
                    int filesize = 0;
                    InputStream mInputStream = null;
                    try {
                        mInputStream = getContentResolver().openInputStream(mMediaUri);
                        filesize = mInputStream.available();
                    } catch (FileNotFoundException e) {
                       Toast.makeText(MainActivity.this, getString(R.string.error_opening_file), Toast.LENGTH_LONG).show();
                        return;
                    } catch (IOException e) {
                        Toast.makeText(MainActivity.this, getString(R.string.error_opening_file), Toast.LENGTH_LONG).show();
                        return;
                    }finally {
                        try {
                            mInputStream.close();
                        } catch (IOException e) {
                            /* This is Not Necessairy For My :)*/
                        }
                    }

                    if (filesize >FILE_SIZE_LIMIT){
                        Toast.makeText(MainActivity.this, getString(R.string.error_size_video), Toast.LENGTH_LONG ).show();
                        return;
                    }

                }
            }else{
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                sendBroadcast(mediaScanIntent);
            }


            Intent receptacleIntent = new Intent(this, ReceptacleActivity.class);
            receptacleIntent.setData(mMediaUri);

            String FileType;
            if(requestCode == PICK_PHOTO_REQUEST || requestCode == TAK_PHOTO_REQUEST){
                FileType = ParseConstant.TYPE_IMAGE;
            }else{
                FileType = ParseConstant.TYPE_VIDEO;
            }
            receptacleIntent.putExtra(ParseConstant.KEY_FILE_TYPE, FileType);

            startActivity(receptacleIntent);

        }else if (resultCode != RESULT_CANCELED){
            Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG).show();

        }
    }



}
