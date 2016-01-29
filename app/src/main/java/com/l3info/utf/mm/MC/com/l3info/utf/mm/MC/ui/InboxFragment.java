package com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.ui;



import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityUnitTestCase;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.adapters.MessageAdapter;
import com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.utils.ParseConstant;
import com.l3info.utf.mm.MC.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


/**
 * Created by mm on 12/26/15.
 */
public class InboxFragment extends ListFragment {
    protected List<ParseObject> mMessages;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected  ListView mListView;
    protected ImageView CheckImageView;
    protected ActionMode mActionMode;
    protected    Toolbar myToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefrechListener);
//        mListView = (ListView)rootView.findViewById(android.R.id.list);
        //make change Listo Grid okk :):):)
        mListView = (ListView)rootView.findViewById(android.R.id.list);
        //mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        registerForContextMenu(mListView);


       // mListView.setOnItemLongClickListener(onItemLongClickListener);


//        mSwipeRefreshLayout.setFocusableInTouchMode(true);
//        mSwipeRefreshLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                v.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(getContext(), "OKK", Toast.LENGTH_LONG).show();
//                    }
//                });
//
//            }
//        });




        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        getActivity().setProgressBarIndeterminateVisibility(true);
        retriveMessages();
    }

    private void retriveMessages() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstant.CLASS_MESSAGES);
        query.whereEqualTo(ParseConstant.KEY_RECEPCTACLE_IDS, ParseUser.getCurrentUser().getObjectId());
        query.addDescendingOrder(ParseConstant.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {

                getActivity().setProgressBarIndeterminateVisibility(false);

                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                if (e == null) {

                    mMessages = messages;
                    String[] username = new String[mMessages.size()];
                    int i = 0;
                    for (ParseObject object : mMessages) {
                        username[i] = object.getString(ParseConstant.KEY_SENDER_NAME);
                        i++;
                    }

                    if(getListView().getAdapter() == null) {
                        MessageAdapter adapter = new MessageAdapter(getContext(), mMessages);
                        setListAdapter(adapter);
                    }
                    else{
                        ((MessageAdapter)getListView().getAdapter()).refill(mMessages);
                    }

                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ParseObject message = mMessages.get(position);
        String messageType = message.getString(ParseConstant.KEY_FILE_TYPE);
        ParseFile file = message.getParseFile(ParseConstant.KEY_FILE);
        Uri fileUri = Uri.parse(file.getUrl());

        if(messageType.equals(ParseConstant.TYPE_IMAGE)){

            Intent intent = new Intent(getContext(), ViewImage.class);
            intent.setData(fileUri);
            startActivity(intent);
        }else{

            Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
            intent.setDataAndType(fileUri, "video/*");
            startActivity(intent);

        }






        /*
            For Supp My Message :)

            List<String> ids = message.getList(ParseConstant.KEY_RECPECTACLE_IDS);

            if(ids.size() == 1){
            message.deleteInBackground();
            }else{


            ids.remove(ParseUser.getCurrent().getObjectId();
            ArrayList<String> idsToRemove = new ArrayList<String>();
            idsToRemove.add(ParseUser.getCurrent().getObjectId());

            message.removeAll(ParseConstant.KEY_RECPECTACLE_IDS, idsToRemove);
            message.saveInBackground();
            }


         */



    }

    protected SwipeRefreshLayout.OnRefreshListener mOnRefrechListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            retriveMessages();
        }
    };


//
//    AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
//        @Override
//        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText(getContext(), "OKK" + position, Toast.LENGTH_LONG).show();
//
//                    CheckImageView = (ImageView) view.findViewById(R.id.MessageimageView);
//
//            mListView.setItemChecked(position,true);
//                    if (mListView.isItemChecked(position) ) {
//                        CheckImageView.setVisibility(View.VISIBLE);
//                        //Is Work :):)
////                        ParseObject MyMessage = mMessages.get(position);
////                        MyMessage.deleteInBackground();
//
//                       /// ( (InboxFragment) ContextMenu).startActionMode();
//                      //  startActionMode(mActionModeCallback);
//                        getActivity().startActionMode(mActionModeCallback);
//
//                    } else {
//                        CheckImageView.setVisibility(View.INVISIBLE);
//                    }
//
//
//                    return true;
//                }
//
//
//
//    };
//
//    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
//
//        // Called when the action mode is created; startActionMode() was called
//        @Override
//        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            // Inflate a menu resource providing context menu items
//           // getActivity().getMenuInflater().inflate(R.menu.remove_item, myToolbar.inflateMenu());
//
//
//
//          //MenuInflater inflater = mode.getMenuInflater();
//
//            MenuInflater inflater = mode.getMenuInflater();
//
////            MenuItem searchItem = menu.findItem(R.id.remove_item);
////            searchItem.setVisible(true);
////
////             myToolbar.getMenu().clear();
////            menu = myToolbar.getMenu();
////            myToolbar.setVisibility(View.INVISIBLE);
//           inflater.inflate(R.menu.remove_item, menu);
//
//            return true;
//        }
//
//        // Called each time the action mode is shown. Always called after onCreateActionMode, but
//        // may be called multiple times if the mode is invalidated.
//        @Override
//        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//            return false; // Return false if nothing is done
//        }
//
//
//
//        // Called when the user selects a contextual menu item
//        @Override
//        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//
//            switch (item.getItemId()) {
//                case R.id.remove_item:
//                   // shareCurrentItem();
//                    mode.finish(); // Action picked, so close the CAB
//                    return true;
//                default:
//                    return false;
//            }
//
//        }
//
//        // Called when the user exits the action mode
//        @Override
//        public void onDestroyActionMode(ActionMode mode) {
//            mActionMode = null;
//        }
//    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        menu.setHeaderTitle("Settings Message");
        menu.setHeaderIcon(R.drawable.ic_remove);
        inflater.inflate(R.menu.remove_item, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.remove_item:
             //  editNote(info.id);
                ParseObject MyMessage = mMessages.get((int) info.id);
                MyMessage.deleteInBackground();
                retriveMessages();
                Toast.makeText(getContext(), "Message Deleted", Toast.LENGTH_LONG).show();
                return true;
//            case R.id.delete:
//                deleteNote(info.id);
//                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
