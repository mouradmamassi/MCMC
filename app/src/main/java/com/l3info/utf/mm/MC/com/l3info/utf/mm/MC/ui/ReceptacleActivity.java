package com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.ui;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.adapters.ItemAdapter;
import com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.utils.FileHelper;
import com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.utils.ParseConstant;
import com.l3info.utf.mm.MC.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ReceptacleActivity extends AppCompatActivity  {

    public static final String TAG = ReceptacleActivity.class.getSimpleName();

    protected List<ParseUser> mFriends;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    protected GridView mgridView;
    protected MenuItem mSendMenu;
    protected Uri mMediaUri;
    protected String mFileType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modefy_firends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mgridView = (GridView)findViewById(R.id.friendGrid);
        mgridView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        mMediaUri = getIntent().getData();
        mFileType = getIntent().getExtras().getString(ParseConstant.KEY_FILE_TYPE);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.receptacle, menu);
        mSendMenu = menu.getItem(0);//????
//        Log.d(TAG, "size Menu is " + menu.size());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_send:
                ParseObject message = createMessage();
                if(message == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.error_file_selected);
                    builder.setTitle(R.string.error_file_selected_title);
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog =builder.create();
                    dialog.show();
                }else {
                    Send(message);
                    finish();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {

        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstant.KEY_FRIENDS_RELATION);

       setProgressBarIndeterminateVisibility(true);
        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();

        query.addAscendingOrder(ParseConstant.KEY_USERNAME);
        query.findInBackground(new FindCallback<ParseUser>() {


            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                setProgressBarIndeterminateVisibility(false);
                if (e == null) {
                    mFriends = friends;
                    String[] username = new String[mFriends.size()];
                    int i = 0;
                    for (ParseUser friend : mFriends) {
                        username[i] = friend.getUsername();
                        i++;
                    }

                    if(mgridView.getAdapter() == null){
                        ItemAdapter adapter = new ItemAdapter(getBaseContext(), mFriends);
                        mgridView.setAdapter(adapter);
                    }else{
                        ((ItemAdapter)mgridView.getAdapter()).refill(mFriends);
                    }



                } else {
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReceptacleActivity.this);
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });

        mgridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                ImageView CheckImageView = (ImageView)view.findViewById(R.id.checkimageView);
                if (mgridView.getCheckedItemCount() > 0) {
                    mSendMenu.setVisible(true);
                } else {
                    mSendMenu.setVisible(false);

                }

                if(mgridView.isItemChecked(position)){
                    CheckImageView.setVisibility(View.VISIBLE);
                }else{
                     CheckImageView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }




    protected ParseObject createMessage(){


        ParseObject message = new ParseObject(ParseConstant.CLASS_MESSAGES);
        message.put(ParseConstant.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
        message.put(ParseConstant.KEY_SENDER_NAME, ParseUser.getCurrentUser().getUsername());
        message.put(ParseConstant.KEY_RECEPCTACLE_IDS, getRecepctacleIds());
        message.put(ParseConstant.KEY_FILE_TYPE, mFileType);

        byte[] fileBytes = FileHelper.getByteArrayFromFile(this, mMediaUri);

        if(fileBytes == null){
            return null;
        }
        else{
            if(mFileType.equals(ParseConstant.TYPE_IMAGE)){
                fileBytes = FileHelper.reduceImageForUpload(fileBytes);
            }
            String fileName = FileHelper.getFileName(this, mMediaUri, mFileType);
            ParseFile file = new ParseFile(fileName, fileBytes);
            message.put(ParseConstant.KEY_FILE, file);
            return message;
        }


    }

    protected void Send(final ParseObject message){
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(getApplicationContext(), R.string.sucess_message, Toast.LENGTH_LONG).show();
                    SendPushNotification();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReceptacleActivity.this);
                    builder.setMessage("Error , please Resending You Message !")
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            }
        });
    }

//    //protected ListView getmListeView(){
//        return mListeView;
//    }

    protected ArrayList<String> getRecepctacleIds(){

        ArrayList<String> recpectale = new ArrayList<String>();
        for(int i = 0 ; i <mgridView.getCount(); i++){

            if(mgridView.isItemChecked(i)){
                recpectale.add(mFriends.get(i).getObjectId());

            }
        }

        return recpectale;

    }

    public void SendPushNotification(){


        ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
        query.whereContainedIn(ParseConstant.KEY_USER_ID, getRecepctacleIds());


        ParsePush mpParsePush = new ParsePush();
        mpParsePush.setQuery(query);
        mpParsePush.setMessage("You have a New MC Message from " + ParseUser.getCurrentUser().getUsername() );
        mpParsePush.sendInBackground();



    }

}
