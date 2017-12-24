# Predator for ProductHunt

[![Google play badge](https://img.shields.io/badge/Google%20play-v0.41-green.svg)](https://play.google.com/store/apps/details?id=com.crazyhitty.chdev.ks.predator)

Predator is a minimalistic client for ProductHunt. It shows latest products and collections fetched via ProductHunt api. It also offers other information like media and comments associated with that product.

# Installation instructions

* Clone this repository using `git clone https://github.com/crazyhitty/Capstone-Project.git`
* Create api key and api secret for your project via [product hunt api console](https://www.producthunt.com/v1/oauth/applications). Make sure to login first on [producthunt.com](https://www.producthunt.com/) as the previous link will keep on redirecting you onto the main site until you log in. Also, make sure to put this in redirect uri: `predator://com.crazyhitty.chdev.ks.predator/oauth2redirect`
* Add those keys in producthunt-wrapper's build.gradle file:

```
    debug {
            if (!project.hasProperty("isReleaseBuild") || "$isReleaseBuild" == "false") {
                def apiKey = "\"your_api_key_here\""
                def apiSecret = "\"your_api_secret_here\""
                def searchUrl = "\"\""
                def xAngoliaAgent = "\"\""
                def xAngoliaApplicationId = "\"\""
                def xAngoliaApiKey = "\"\""

                buildConfigField "String", "API_KEY", apiKey
                buildConfigField "String", "API_SECRET", apiSecret
                buildConfigField "String", "SEARCH_URL", searchUrl
                buildConfigField "String", "X_ANGOLIA_AGENT", xAngoliaAgent
                buildConfigField "String", "X_ANGOLIA_APPLICATION_ID", xAngoliaApplicationId
                buildConfigField "String", "X_ANGOLIA_API_KEY", xAngoliaApiKey
            }

            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
```

* Now, just hit the run button on your android studio or execute `installDebug` task to install debug variant of the application.

# Features
* Minimal and simple user interface, which user can get easily acquainted with.
* View and search through latest posts and collections from ProductHunt.
* Checkout the media and comments of a particular post and share them with anyone.
* Sync data in background even if the app is not running, so you can remain up to date with the latest posts and collections (Optional, can be activated via application settings).
* Notification support.
* Multiple font support.
* Delightful animations to encourage the user experience.

# Screenshots
<img src="https://raw.githubusercontent.com/crazyhitty/Capstone-Project/master/screenshots/1.png" alt="alt text" width="400"> <img src="https://raw.githubusercontent.com/crazyhitty/Capstone-Project/master/screenshots/2.png" alt="alt text" width="400">

<img src="https://raw.githubusercontent.com/crazyhitty/Capstone-Project/master/screenshots/3.png" alt="alt text" width="400"> <img src="https://raw.githubusercontent.com/crazyhitty/Capstone-Project/master/screenshots/4.png" alt="alt text" width="400">

<img src="https://raw.githubusercontent.com/crazyhitty/Capstone-Project/master/screenshots/5.png" alt="alt text" width="400"> <img src="https://raw.githubusercontent.com/crazyhitty/Capstone-Project/master/screenshots/6.png" alt="alt text" width="400">

<img src="https://raw.githubusercontent.com/crazyhitty/Capstone-Project/master/screenshots/7.png" alt="alt text" width="400"> <img src="https://raw.githubusercontent.com/crazyhitty/Capstone-Project/master/screenshots/8.png" alt="alt text" width="400">

<img src="https://raw.githubusercontent.com/crazyhitty/Capstone-Project/master/screenshots/9.png" alt="alt text" width="400">

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