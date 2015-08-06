/*
 * Copyright (C) 2011 The Android Open Source Project [MODIFIED]
 * With modifications Copyright (C) 2012 Simplify Now Software
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

package com.simplifynowsoftware.apache;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

import java.util.ArrayList;
import java.util.List;

/*
 * Modifications:
 * This class contains some code from FragmentTabsPager from compatibility lib v4 demo
 *  ...\extras\android\compatibility\v4\samples\Support4Demos\src\com\example\android\supportv4\app\FragmentTabsPager.java
 *  
 * 
 * Updated to:
 *  Remove hard-coded resource references
 *  Remove hard-coded tabs
 *  Make class abstract, where implementing class will define the resource IDs and add the tabs
 *  Access fragments by index (hiding parent-generated tag names)
 *  Use ActionBarActivity as parent
 *  Fix menu options update when changing fragments with swipe
 */

public abstract class FragTabPagerCompat extends ActionBarActivity {
    protected TabHost mTabHost;
    protected ViewPager  mViewPager;
    protected TabsAdapter mTabsAdapter;
    
    protected final static String SAVETAG_TAB = "tab";

    /*
     * These three methods must be implemented by child classes
     * Return the resource IDs for the layout and two of the items
     *  contained within:  The TabHost and the ViewPager
     */
    protected abstract int getLayoutID();
    protected abstract int getTabHostID();
    protected abstract int getPagerID();

    
    /*
     * Child classes should add tabs after calling super.onCreate.  E.g.
     * mTabsAdapter.addTab(mTabHost.newTabSpec(FRAGMENT_TAG).setIndicator(FRAGMENT_TAG),
                SomeFragment.class, null);
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutID());
        mTabHost = (TabHost)findViewById(getTabHostID());
        mTabHost.setup();

        mViewPager = (ViewPager)findViewById(getPagerID());

        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

        // TODO re-enable this if it is desired to remember tabs 
//        if (savedInstanceState != null) {
//            mTabHost.setCurrentTabByTag(savedInstanceState.getString(SAVETAG_TAB));
//        }
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString(SAVETAG_TAB, mTabHost.getCurrentTabTag());
//    }
    
    
    /**
     * This is a helper class that implements the management of tabs and all
     * details of connecting a ViewPager with associated TabHost.  It relies on a
     * trick.  Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show.  This is not sufficient for switching
     * between pages.  So instead we make the content part of the tab host
     * 0dp high (it is not shown) and the TabsAdapter supplies its own dummy
     * view to show as the tab content.  It listens to changes in tabs, and takes
     * care of switch to the correct paged in the ViewPager whenever the selected
     * tab changes.
     */
    public static class TabsAdapter extends FragmentPagerAdapter
            implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
        public static final int DEFAULT_TAB_HEIGHT = -1;
        
        private int mTabHeight = DEFAULT_TAB_HEIGHT;
        
        private final ActionBarActivity mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
        protected final FragmentManager mFragmentManager;

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabsAdapter(ActionBarActivity activity, TabHost tabHost, ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mFragmentManager = activity.getSupportFragmentManager(); // override the parent with an accessible copy
            mContext = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        // Return index of this tab
        public int addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();
            
            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();

            int addedIndex = mTabHost.getTabWidget().getChildCount() - 1;

            // If an alternate tab height has been set, find the last tab (the one just added) and adjust it
            if(mTabHeight != DEFAULT_TAB_HEIGHT) {
                mTabHost.getTabWidget().getChildAt(addedIndex).getLayoutParams().height = mTabHeight;
            }

            return addedIndex;
        }

        // Set the desired tab height, in pixels.  Be careful to adjust for DPI and screen size
        // Be sure to call this before adding any tabs!
        public void setTabHeight(final int tabHeight) {
            mTabHeight = tabHeight;
        }
        
        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);            
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }
        
        // Return last instantiated fragment (TODO ensure that this can't return stale (invalid) values)
        public Fragment getFragment(final int index) {
            Fragment fragment = null;
            
            // Calculate the fragment tag that would have been used.
            // Why was makeFragmentName private?
            final String tag = "android:switcher:" + mViewPager.getId() + ":" + index;
            
            fragment = mFragmentManager.findFragmentByTag(tag);
            List<Fragment> fragmentList = mFragmentManager.getFragments();
            for(int i = 0; i < fragmentList.size(); i++) {
                Log.i("getFragment: tag", fragmentList.get(i).getTag());
            }
            
            if(null == fragment) {
                Log.w(this.toString(), "Failed to retrieve any fragment for index " + index + " with tag [" + tag + "]");
            }
                
            return fragment;
        }
        
        @Override
        public void onTabChanged(String tabId) {
            int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {            
            // Unfortunately when TabHost changes the current tab, it kindly
            // also takes care of putting focus on it when not in touch mode.
            // The jerk.
            // This hack tries to prevent this from pulling focus out of our
            // ViewPager.
            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            mTabHost.getTabContentView().requestFocus(View.FOCUS_FORWARD);
            widget.setDescendantFocusability(oldFocusability);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // Update the menu once when scroll is almost done, and once when completely done
            if(ViewPager.SCROLL_STATE_IDLE == state || ViewPager.SCROLL_STATE_SETTLING == state) {
                mContext.supportInvalidateOptionsMenu();
            }
        }
    }
}