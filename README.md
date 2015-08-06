Introduction
------------
FlickrDemo is an example Android app demonstrating a few technologies together:
  * The Flickr API
  * OAuth to authenticate the user to Flickr
  * A message bus to communicate with fragments
  * Management of remotely loaded images
  * REST with JSON data using Retrofit
  * Automatic testing with the Google-supplied framework
  * Automatic Gradle/Android Studio integration of GitHub repositories that are not packaged in a repo

The app is functional, but the automatic testing and the error-checking are not exhaustive by any means.  As such, it's likely that a number of
edge cases are not handled.

Use should hopefully be clear, but this app isn't excessively polished.

Use
------------
This demo does not support any non-authenticated functionality, so as soon as the app is started it is necessary to log-in and authenticate the app with a Flickr account.

Account data and photos are immediately loaded and shown.

The Flickr action (the two dots) shows the interestingness stream, and the search action allows finding a user by username (tricky, since it doesn't allow partial searches).When viewing another user, the user action appears, which returns to viewing the user information connected with the authenticated account.

The logout action causes an immediate log-in request, and cancelling a log-in causes the app to exit as it has nothing to do.

Attribution
------------
It's impossible to make a complete list of the open source software that this app relies on, but the major top-level libraries and sources include:
  * https://github.com/mttkay/signpost
  * https://github.com/pakerfeldt/signpost-retrofit
  * https://github.com/wuman/android-oauth-client
  * https://github.com/square/retrofit
  * https://github.com/square/picasso
  * https://github.com/square/otto

The icons are provided through Android studio:
  * Material Design Icons from Google, licensed under the Creative Common Attribution 4.0 International License (CC-BY 4.0) -- http://creativecommons.org/licenses/by/4.0/
  * AndroidIcons by Opoloo, licensed under the Creative Commons Attribution-ShareAlike 4.0 International CC BY-SA 4.0 -- http://creativecommons.org/licenses/by-sa/4.0/

License
------------
  Copyright (C) 2015 Simplify Now, LLC
 
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
 
       http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 
