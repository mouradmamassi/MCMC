package com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.adapters;

/**
 * Created by mm on 26/12/15.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.l3info.utf.mm.MC.R;
import com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.ui.FriendsFragment;
import com.l3info.utf.mm.MC.com.l3info.utf.mm.MC.ui.InboxFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {


    protected Context mContext;
    private static int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Inbox", "Friends" };
    private int[] imageResId = {
            R.drawable.ic_inbox,
            R.drawable.ic_face,

    };

    public SectionsPagerAdapter(Context context, FragmentManager fm) {

        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        switch (position){
            case 0:
                return new InboxFragment();
            case 1:
                return new FriendsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {

        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {

       // return tabTitles[position];

        Drawable image = ContextCompat.getDrawable(mContext, imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

//    public int getIcons(int position){
//        switch (position) {
//            case 0:
//                return R.drawable.ic_mms ;
//            case 1:
//                return R.drawable.ic_face;
//
//        }
//       return R.drawable.ic_mms ;
//    }
}
