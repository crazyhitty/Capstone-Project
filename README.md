# Predator for ProductHunt

<a href='https://play.google.com/store/apps/details?id=com.crazyhitty.chdev.ks.predator&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width="200"/></a>

Predator is a minimalistic client for ProductHunt. It shows latest products and collections fetched via ProductHunt api. It also offers other information like media and comments associated with that product.

# Permissions required
* **CONTACTS:** This permission is added due to the usage of **android.permission.GET_ACCOUNTS** as the application uses android's account manager to store the auth token securely (token which is required while communicating with the server).

# Installation instructions
* Clone this repository using `git clone https://github.com/crazyhitty/Capstone-Project.git`
* Download [Authorization.java](https://gist.github.com/crazyhitty/08fcf4b67d3e20a87e2bde8be7e5ead9) and paste it in `producthunt-wrapper\src\main\java\com\crazyhitty\chdev\ks\producthunt_wrapper\rest\`
* Add appropriate **API_KEY** and **API_SECRET** into the **Authorization.java** file. You can generate these from [here](https://www.producthunt.com/v1/oauth/applications). Make sure to login first on [producthunt.com](https://www.producthunt.com/) as the previous link will keep on redirecting you onto the main site until you log in. Also, make sure to put this in redirect uri: `predator://com.crazyhitty.chdev.ks.predator/oauth2redirect`
* Now, from the root directory of the project run `gradlew installRelease` (windows) or `./gradlew installRelease` (linux) as this will install the release build of the application in your device.

# Features
* Minimal and simple user interface, which user can get easily acquainted with.
* View latest posts and collections from ProductHunt.
* Checkout the media and comments of a particular post and share them with anyone.
* Sync data in background even if the app is not running, so you can remain up to date with the latest posts and collections (Optional, can be activated via application settings).
* Delightful animations to encourage the user experience.

# Screenshots
<img src="https://raw.githubusercontent.com/crazyhitty/Capstone-Project/master/screenshots/1.png" alt="alt text" width="400"> <img src="https://raw.githubusercontent.com/crazyhitty/Capstone-Project/master/screenshots/2.png" alt="alt text" width="400">

<img src="https://raw.githubusercontent.com/crazyhitty/Capstone-Project/master/screenshots/3.png" alt="alt text" width="400"> <img src="https://raw.githubusercontent.com/crazyhitty/Capstone-Project/master/screenshots/4.png" alt="alt text" width="400">

<img src="https://raw.githubusercontent.com/crazyhitty/Capstone-Project/master/screenshots/5.png" alt="alt text" width="400"> <img src="https://raw.githubusercontent.com/crazyhitty/Capstone-Project/master/screenshots/6.png" alt="alt text" width="400">

<img src="https://raw.githubusercontent.com/crazyhitty/Capstone-Project/master/screenshots/7.png" alt="alt text" width="400">

# Contributing
You can contribute to the project by either finding out bugs or by requesting new features.

# Community
[Google plus](https://plus.google.com/communities/102250921213849521349)

[Reddit](https://www.reddit.com/r/predator_ph/)

# License
```
MIT License

Copyright (c) 2016 Kartik Sharma

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```