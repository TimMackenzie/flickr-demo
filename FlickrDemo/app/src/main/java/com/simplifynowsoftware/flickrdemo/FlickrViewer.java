/*
 * Copyright (C) 2015 Simplify Now Software
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

package com.simplifynowsoftware.flickrdemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.api.client.auth.oauth2.Credential;
import com.simplifynowsoftware.apache.FragTabPagerCompat;
import com.simplifynowsoftware.flickrdemo.events.UpdateRequest;
import com.simplifynowsoftware.flickrdemo.oauth.OAuthHandler;
import com.simplifynowsoftware.flickrdemo.retrofit.FlickrClient;
import com.simplifynowsoftware.flickrdemo.retrofit.ServiceGenerator;
import com.simplifynowsoftware.flickrdemo.retrofit.response.username.UsernameResponse;
import com.simplifynowsoftware.flickrdemo.retrofit.response.validate.TokenTest;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.wuman.android.auth.oauth.OAuthHmacCredential;

import retrofit.RetrofitError;
import retrofit.client.Response;

/*
 * Main activity for the FlickrViewer demo
 */
public class FlickrViewer extends FragTabPagerCompat {
    protected final static String TAG = "FlickrViewer";

    protected OAuthHandler mOauthHandler;
    protected FlickrClient mFlickrClient;
    protected Bus mEventBus;
    protected Dialog mDialog;
    protected String mUserId;

    /*
     * Implement required abstact methods from FragTabPager
     */
    protected int getLayoutID() {
        return R.layout.frag_tab;
    }

    protected int getTabHostID() {
        return android.R.id.tabhost;
    }

    protected int getPagerID() {
        return R.id.pager;
    }
    /*
     * END required methods from FragTabPager
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set theme here if needed
        super.onCreate(savedInstanceState);

        // layout set through parent via getLayoutID

        // start shared event bus
        mEventBus = FlickrDemo.getBusInstance();

        // Reload saved userId, in case it isn't the default
        mUserId = (String)getLastCustomNonConfigurationInstance();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setHomeButtonEnabled(true);

        // Add all tabs here
        mTabsAdapter.addTab(mTabHost.newTabSpec(UserFragment.FRAGMENT_TAG).setIndicator(UserFragment.getTitle(this)),
                UserFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec(FavoritesFragment.FRAGMENT_TAG).setIndicator(FavoritesFragment.getTitle(this)),
                FavoritesFragment.class, null);

        // Start data and authorization setup
        mOauthHandler = new OAuthHandler();

        /*
         * Check if already authorized (saved OAuth token)
         * If so create FlickrClient to communicate with Flickr, then start the data initialization
         * If not, prompt user to log in
         */
        if (checkAndInitialize()) {
            if (FlickrDemoConstants.DEBUG_ENABLE) {
                Log.i(TAG, "checkAndInitialize done, now retrieving userId");
            }

            // If both ownUserId and mUserId are not null, we're reloading and don't need to init
            if(null == FlickrDemo.getOwnUserId() || null == mUserId) {
                // Find out ID of logged in user
                mFlickrClient.testToken(validateTokenCallback);
            }
        } else {
            // Future development opportunity: create a non-signed service for logged-out operations
            if (FlickrDemoConstants.DEBUG_ENABLE) {
                Log.i(TAG, "checkAndInitialize not initialized, NOT loading data for default userId");
            }

            mDialog = onPromptLogin();
        }
    }

    @Override
    public void onDestroy() {
        if(null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        super.onDestroy();
    }

    // Save current userId to reload after rotation
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mUserId;
    }

    @Override
    public void onResume() {
        super.onResume();

        mEventBus.register(this);
    }

    @Override
    public void onPause() {
        mEventBus.unregister(this);

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flicker_viewer, menu);

        /*
         * If we're viewing our own user data, don't show the 'user' action because it doesn't do
         *  anything.  It is only needed when viewing another profile.
         */
        final boolean isShowingAnother = mUserId != null && mUserId.compareTo(FlickrDemo.getOwnUserId()) != 0;
        MenuItem item = menu.findItem(R.id.action_user);
        if(null != item) {
            item.setVisible(isShowingAnother);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean retVal;

        int id = item.getItemId();

        if (id == R.id.action_user) {
            onActionUser();
            retVal = true;
        } else if (id == R.id.action_flickr) {
            onActionFlickr();
            retVal = true;
        } else if (id == R.id.action_search) {
            onActionSearch();
            retVal = true;
        } else if (id == R.id.action_logout) {
            onActionLoginLogout();
            retVal = true;
        } else {
            retVal = super.onOptionsItemSelected(item);
        }

        return retVal;
    }

    // Load our own data
    protected void onActionUser() {
        if(null != FlickrDemo.getOwnUserId()) {
            selectUserId(FlickrDemo.getOwnUserId());
        }
    }

    // Load interestingness stream
    protected void onActionFlickr() {
        final Intent intent = new Intent(this, PhotosetViewer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(PhotosetViewer.INTENT_EXTRA_PHOTOSET_ID, PhotosetViewer.INTERESTINGNESS_PHOTOSET_ID);
        intent.putExtra(PhotosetViewer.INTENT_EXTRA_USER_ID, PhotosetViewer.INTERESTINGNESS_USER_ID);
        startActivity(intent);
    }

    protected void onActionSearch() {
        onPromptSearch();
    }


    /*
     * Log in or log out, depending on current state.
     * Note that login is not currently used here because the logged-out state requires immediate login
     * Presume presence of mFlickrClient means we are logged in; change this if adding non-auth mode
     */
    protected void onActionLoginLogout() {
        if(mFlickrClient == null) {
            OauthLoginTask task = new OauthLoginTask();
            task.execute();
        } else {
            oAuthLogout();
        }
    }

    /*
     * Check if OAuth token is stored; if so load it and initialize the FlickrClient
     */
    protected boolean checkAndInitialize() {
        boolean result = mOauthHandler.checkPreviousAuthorization(this, FlickrDemoConstants.SINGLE_USER_ID);

        if(FlickrDemoConstants.DEBUG_ENABLE) {
            Log.i(TAG, "check for OAuth token: " + result);
        }

        if (result) {
            if(!initializeFlickrClient()) {
                result = false;
                if(FlickrDemoConstants.DEBUG_ENABLE) {
                    Log.w(TAG, "Failed to initialize FlickrClient");
                }
            }
        }

        return result;
    }


    /*
     * Ensue FlickrClient is available, using available credentials
     * Return true if it already exists or is successfully created
     */
    protected boolean initializeFlickrClient() {
        if(null == mFlickrClient) {
            Credential credential = mOauthHandler.getCredential();

            if (credential instanceof OAuthHmacCredential) {
                OAuthHmacCredential hmacCredential = (OAuthHmacCredential) credential;

                mFlickrClient = ServiceGenerator.createServiceSigned(FlickrClient.class,
                        ServiceGenerator.FLICKR_URL_BASE,
                        hmacCredential.getAccessToken(),
                        hmacCredential.getTokenSharedSecret());

                FlickrDemo.setFlickrClient(mFlickrClient);
            } else {
                if (FlickrDemoConstants.DEBUG_ENABLE) {
                    Log.w(TAG, "Incorrect credential type, not able to get token secret");
                }
            }
        }

        return (null != mFlickrClient);
    }


    // Login is a network task, so perform it off of the main thread
    private class OauthLoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            boolean success = false;
            try {
                success = mOauthHandler.getOAuthToken(FlickrViewer.this, FlickrDemoConstants.SINGLE_USER_ID);
            } catch (java.util.concurrent.CancellationException e) {
                if(FlickrDemoConstants.DEBUG_ENABLE) {
                    Log.w(TAG, "Failed to get OAuth token due to user cancellation, exiting");
                }
                FlickrViewer.this.finish();
            }
            if(success) {
                if(initializeFlickrClient()) {
                    // Find out ID of logged in user
                    mFlickrClient.testToken(validateTokenCallback);
                }
            } else {
                if(FlickrDemoConstants.DEBUG_ENABLE) {
                    Log.w(TAG, "Failed to get FlickrClient due to failure to get OAuth token");
                }
            }

            return Boolean.toString(success);
        }
    }


    public FlickrClient getFlickrClient() {
        return mFlickrClient;
    }

    protected void oAuthLogout() {
        boolean success = mOauthHandler.removeCredential(FlickrViewer.this, FlickrDemoConstants.SINGLE_USER_ID);
        mFlickrClient = null; // client stores credentials, so need to remove it

        if(FlickrDemoConstants.DEBUG_ENABLE) {
            Log.w(TAG, "oAuthLogout: " + success);
        }

        // Time to log in again
        mDialog = onPromptLogin();
    }

    protected void findUserById(final String userId) {
        FlickrDemo.getFlickrClient().getUserIdFromName(userId, findUserIdCallback);
    }

    protected void selectUserId(final String userId) {
        if(FlickrDemoConstants.DEBUG_ENABLE) {
            Log.w(TAG, "selectUserId starting");
        }

        mUserId = userId;

        // Reload action bar items, in case we've switched between self and another user
        invalidateOptionsMenu();

        mEventBus.post(new UserIdUpdate(userId)); // received by UserFragment & FavoritesFragment
    }

    public class UserIdUpdate {
        protected final String mUserId;

        public UserIdUpdate(final String userId) {
            mUserId = userId;
        }

        public String getUserId() {
            return mUserId;
        }
    }

    /*
     * Send the current userId, without checking if it's valid
     */
    @Subscribe
    public void UpdateRequestEvent(UpdateRequest event) {
        if (FlickrDemoConstants.DEBUG_ENABLE) {
            Log.i("UpdateRequestEvent", "Now providing userId: " + mUserId);
        }

        mEventBus.post(new UserIdUpdate(mUserId));
    }


    public Dialog onPromptLogin() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(this.getString(R.string.login_title));
        alert.setMessage(this.getString(R.string.login_message));

        alert.setPositiveButton(this.getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        OauthLoginTask task = new OauthLoginTask();
                        task.execute();
                    }
                });

        alert.setNegativeButton(this.getString(android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        FlickrViewer.this.finish(); // can't continue without login
                    }
                });

        return alert.show();
    }


    public void onPromptSearch() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(this.getString(R.string.dialog_search_title));
        alert.setMessage(this.getString(R.string.dialog_search_message));

        // Text box to enter search term
        final EditText input = new EditText(this);
        input.setSingleLine();
        input.setInputType(EditorInfo.TYPE_CLASS_TEXT); // needed for Android 2.3.5+
        input.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        alert.setView(input);

        alert.setIcon(R.drawable.ic_action_search_black);

        alert.setPositiveButton(this.getString(android.R.string.search_go),//R.string.search_dlg_button_search),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String term = input.getText().toString().trim(); // trim trailing spaces
                        if (!term.isEmpty()) {
                            findUserById(term);
                        }
                    }
                });

        alert.setNegativeButton(this.getString(android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Hide the keyboard before closing, so text resizing is correct
                        InputMethodManager imm = (InputMethodManager) FlickrViewer.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);

                        // dialog will close automatically
                    }
                });

        final AlertDialog dialog = alert.show();

        // set this listener afterwards so that it can dismiss the dialog
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                boolean retVal = false;

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    final String term = input.getText().toString().trim(); // trim trailing spaces
                    if (!term.isEmpty()) {
                        findUserById(term);
                    }

                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) FlickrViewer.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(input.getWindowToken(), 0);

                    dialog.dismiss();

                    retVal = true;
                }

                return retVal;
            }
        });
    }

    protected retrofit.Callback findUserIdCallback = new retrofit.Callback() {
        @Override
        public void success(Object o, Response response) {
            UsernameResponse usernameResponse = (UsernameResponse)o;
            if(null == usernameResponse || null == usernameResponse.getUser()) {
                if (FlickrDemoConstants.DEBUG_ENABLE) {
                    Log.i("findUserIdCallback", "No userId found for selected username");
                }
                StaticUtil.displayShortToast(FlickrViewer.this, getString(R.string.search_failed));
            } else {
                selectUserId(usernameResponse.getUser().getNsid());
            }
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            if (FlickrDemoConstants.DEBUG_ENABLE) {
                Log.e("findUserIdCallback", "RetrofitError" + retrofitError.getMessage());
            }
        }
    };

    protected retrofit.Callback validateTokenCallback = new retrofit.Callback() {
        @Override
        public void success(Object o, Response response) {
            final TokenTest tokenTest = (TokenTest)o;
            final String userId = tokenTest.getUser().getId();

            if (FlickrDemoConstants.DEBUG_ENABLE) {
                Log.i("validateTokenCallback", "ID = " + userId);
            }

            FlickrDemo.setOwnUserId(userId);
            selectUserId(userId);
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            if (FlickrDemoConstants.DEBUG_ENABLE) {
                Log.e("validateTokenCallback", "RetrofitError" + retrofitError.getMessage());
            }
        }
    };
}
