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
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.text.util.Linkify;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class StaticUtil {
    public static Dialog showOkAlert(final Context context, String message){
        if(null == message) {
            message = "";
        }
        return showOkAlert(context, new SpannableString(message));
    }

    public static Dialog showOkAlert(Context context, SpannableString message) {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
        alt_bld.setCancelable(false);

        final SpannableString linkMessage = new SpannableString(message);
        Linkify.addLinks(linkMessage, Linkify.ALL); //Linkify.WEB_URLS

        alt_bld.setMessage(linkMessage);

        alt_bld.setPositiveButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Action for 'OK' Button
                dialog.dismiss();
            }
        });

        AlertDialog alert = alt_bld.create();
        alert.show();

        // Enable clickable links
        TextView text = (TextView) alert.findViewById(android.R.id.message);
        if(null != text) {
            text.setMovementMethod(LinkMovementMethod.getInstance());
            text.setLinksClickable(true);
            text.setAutoLinkMask(Linkify.ALL);
        }

        return alert;
    }


    /*
     * Generate an MD5 hash from an input string
     */
    public static String getMD5Hash(String encTarget){
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while ( md5.length() < 32 ) {
            md5 = "0"+md5;
        }

        return md5;
    }

    public static void displayShortToast(final Context context, final String message) {
        final CharSequence text = message;
        final int duration = Toast.LENGTH_SHORT;
        final Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void addPairToSpannable(final SpannableStringBuilder builder, final String label, final String value) {
        int start = builder.length(); // start bold

        builder.append('\n');
        builder.append(label);
        builder.append('\n');

        int end = builder.length(); // end bold
        builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // To use colors:
        //        builder.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append('\t');
        builder.append(value);
        builder.append('\n');
    }
}
