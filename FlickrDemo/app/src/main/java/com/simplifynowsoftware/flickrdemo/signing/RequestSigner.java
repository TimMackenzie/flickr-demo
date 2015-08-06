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

package com.simplifynowsoftware.flickrdemo.signing;

import android.util.Log;
import android.util.Pair;

import com.simplifynowsoftware.flickrdemo.FlickrDemoConstants;
import com.simplifynowsoftware.flickrdemo.StaticUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 * RequestSigner handles Flickr-specific request signing, separate from OAuth authentication
 */
public class RequestSigner {

    protected static final String SIGNATURE_ARGUMENT   = "&api_sig=";
    protected static final String ARGUMENT_SEPARATOR   = "&"; // signals the start of another argument pair
    protected static final String ARGUMENT_EQUALS      = "="; // The argument value
    protected static final String ARGUMENT_METHOD      = "\\?"; // signals the start of the method pair

    protected static final String FORMAT_ARGUMENT      = "&format=json&nojsoncallback=1"; // argument name and data combined
    protected static final String API_KEY_ARGUMENT     = "&api_key=";


    public static String signRequest(final String unsorted, final String tokenSecret) {
        ArrayList<Pair<String,String>> argList = new ArrayList<>();
        StringBuilder signatureStringBuilder = new StringBuilder();

        String[] kvPairs = unsorted.split(ARGUMENT_SEPARATOR); // 0 is prefix incl method, all else are kv pairs

        // Split out all key-value pairs
        for(int i = 1; i < kvPairs.length; i++) {
            String[] pair = kvPairs[i].split(ARGUMENT_EQUALS);
            argList.add(new Pair<>(pair[0], pair[1]));

            if(FlickrDemoConstants.DEBUG_ENABLE_VERBOSE) {
                Log.i("signRequest", "Adding " + pair[0] + ", " + pair[1]);
            }
        }

        // Sort arguments alphabetically
        Collections.sort(argList, new PairComparator());

        // Also add the method
        String[] methodPair = kvPairs[0].split(ARGUMENT_METHOD)[1].split(ARGUMENT_EQUALS); // all the text after the ?
        argList.add(new Pair<>(methodPair[0], methodPair[1]));

        /*
         * Now, reassemble arguments as a single string
         * Name and value pairs are adjoined with no separators or whitespace
         */
        for(int i = 0; i < argList.size(); i++) {
            signatureStringBuilder.append(argList.get(i).first);
            signatureStringBuilder.append(argList.get(i).second);
        }

        final String signature = generateSignature(signatureStringBuilder.toString(), tokenSecret);

        if(FlickrDemoConstants.DEBUG_ENABLE_VERBOSE) {
            Log.i("signRequest", "signature: " + signature);
        }

        final String signed = unsorted + SIGNATURE_ARGUMENT + signature;

        if(FlickrDemoConstants.DEBUG_ENABLE_VERBOSE) {
            Log.i("signRequest", "final url: " + signed);
        }

        return signed;
    }

    /*
     * The Flickr URL signature is generated from all arguments (including the method), sorted and concatenated.
     * No spaces or separators are included. The OAuth token secret is prepended.
     * e.g. foo=1, bar=2, baz=3 sorts to bar=2, baz=3, foo=1
     * The resulting string is SECRETbar2baz3foo1
     *
     * Note that this is the token secret not the API key secret
     */
    protected static String generateSignature(final String argList, final String tokenSecret) {
        final String signingString =  tokenSecret + argList;

        final String signature = StaticUtil.getMD5Hash(signingString);

        if(FlickrDemoConstants.DEBUG_ENABLE_VERBOSE) {
            Log.i("generateSignature", "signing string: " + signingString);
            Log.i("generateSignature", "signature: " + signature);
        }

        return signature;
    }

    /*
     * Alphabetically sort by the first string in the pair
     */
    protected static class PairComparator implements Comparator<Pair<String,String>> {
        public int compare(Pair<String,String> pair1, Pair<String,String> pair2){
            return pair1.first.compareTo(pair2.first) ;
        }
    }


    /*
     * All calls to Flickr will have the following appended before the signature is calculated
     *  &format=json&nojsoncallback=1&api_key=API_KEY
     */
    public static String addStaticParams(final String url) {
        return url + FORMAT_ARGUMENT + API_KEY_ARGUMENT + FlickrDemoConstants.FLICKR_API_KEY;
    }
}
