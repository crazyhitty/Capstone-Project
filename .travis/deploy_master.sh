#!/bin/bash

setup_git() {
    git config --global user.email "travis@travis-ci.org"
    git config --global user.name "Travis CI"
}

commit_tag() {
    printf "\n--------------------\nCreating tag for master branch for ${versionName}\n--------------------\n"
    printf "Changelog: \n${changelog}"
    git tag -a "v${versionName}" -m "Changelog:

    ${changelog}"
}

push_tag() {
    git remote rm origin
    git remote add origin https://crazyhitty:${GITHUB_ACCESS_TOKEN}@github.com/crazyhitty/Capstone-Project.git > /dev/null 2>&1
    git push --quiet --set-upstream origin origin "v${versionName}"
}

printf "\n--------------------\nDeploying for master branch\n--------------------\n"

# Get app version via gradle task.
versionName=$(./gradlew -q getVersionName -PpredatorStoreFile=${storeFile} -PpredatorStorePassword=${storePass} -PpredatorKeyAlias=${keyAlias} -PpredatorKeyPassword=${keyPass} -PapiKey=${apiKey} -PapiSecret=${apiSecret} -PsearchUrl=${searchUrl} -PxAngoliaAgent=${xAngoliaAgent} -PxAngoliaApplicationId=${xAngoliaApplicationId} -PxAngoliaApiKey=${xAngoliaApiKey} -PisReleaseBuild=true | tail -1)

# Installing xmlstarlet package for xml parsing.
sudo apt install xmlstarlet

# Get changelog for selected version.
changelog=$(xmlstarlet sel -t -v '/changelog/changelogversion[@versionName="'"$versionName"'"]' app/src/main/res/raw/changelog.xml  | sed 's/^/* /' | awk '{$1=$1};1' | tail -n +2 | head -n -1)

setup_git
commit_tag
push_tag