package com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.adapters.ItemAdapter;
import com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.utils.ParseConstant;
import com.l3info.utf.mm.MC.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class FirendsActivity extends AppCompatActivity {

    public static final String TAG = FirendsActivity.class.getSimpleName();

    protected List<ParseUser> mUsers;
    protected ListView mListeView;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    protected GridView mgriGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modefy_firends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mListeView = (ListView)findViewById(android.R.id.list);

        mgriGridView = (GridView)findViewById(R.id.friendGrid);
        mgriGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

        TextView emptyTextView = (TextView)findViewById(R.id.emailField);
        mgriGridView.setEmptyView(emptyTextView);
        mgriGridView.setOnItemClickListener(onItemClickListener);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstant.KEY_FRIENDS_RELATION);

       // mListeView.setOnItemClickListener(onItemClickListener);
        setProgressBarIndeterminateVisibility(true);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstant.KEY_USERNAME);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                setProgressBarIndeterminateVisibility(false);
                if( e == null){

                        mUsers = users;
                    String [] username = new String[mUsers.size()];
                    int i = 0;
                    for(ParseUser user : mUsers){
                        username[i] = user.getUsername();
                        i++;
                    }


                    if(mgriGridView.getAdapter() == null){
                        ItemAdapter adapter = new ItemAdapter(getBaseContext(), mUsers);
                        mgriGridView.setAdapter(adapter);
                    }else{
                        ((ItemAdapter)mgriGridView.getAdapter()).refill(mUsers);
                    }

                    addFriendsCheckmarks();

                    }else{
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(FirendsActivity.this);
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

    }


    private void addFriendsCheckmarks(){
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {

                if(e == null){

                    for(int i = 0; i<mUsers.size(); i++){
                        ParseUser user = mUsers.get(i);
                        for(ParseUser friend : friends){
                            if(friend.getObjectId().equals(user.getObjectId())){
                                mgriGridView.setItemChecked(i, true);
                            }
                        }
                    }
                }else
                {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }
    protected AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            ImageView CheckImageView = (ImageView)view.findViewById(R.id.checkimageView);
            if(mgriGridView.isItemChecked(position)){

               //add Friends

               mFriendsRelation.add(mUsers.get(position));

                CheckImageView.setVisibility(View.VISIBLE);


           }else{
                //remove Friends
               mFriendsRelation.remove(mUsers.get(position));
                CheckImageView.setVisibility(View.INVISIBLE);


           }
            mCurrentUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if(e != null){
                        Log.e(TAG, e.getMessage());
                    }
                }
            });




        }


    };

}
