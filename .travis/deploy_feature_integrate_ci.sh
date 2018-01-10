#!/bin/bash

setup_git() {
    git config --global user.email "travis@travis-ci.org"
    git config --global user.name "Travis CI"
}

commit_test() {
    printf "\n--------------------\nCreating test branch for ${versionName}\n--------------------\n"
    git checkout -b "test_branch_${versionName}"
    #git commit --message "Travis build: $TRAVIS_BUILD_NUMBER"
}

push_git() {
    git remote rm origin
    git remote add origin https://crazyhitty:${GITHUB_ACCESS_TOKEN}@github.com/crazyhitty/Capstone-Project.git > /dev/null 2>&1
    git push origin "test_branch_${versionName}"
    git push --quiet --set-upstream origin "test_branch_${versionName}"
}

printf "\n--------------------\nDeploying for feature-integrate_ci branch\n--------------------\n"

# Get app version via gradle task.
versionName=$(./gradlew -q getVersionName -PpredatorStoreFile=${storeFile} -PpredatorStorePassword=${storePass} -PpredatorKeyAlias=${keyAlias} -PpredatorKeyPassword=${keyPass} -PapiKey=${apiKey} -PapiSecret=${apiSecret} -PsearchUrl=${searchUrl} -PxAngoliaAgent=${xAngoliaAgent} -PxAngoliaApplicationId=${xAngoliaApplicationId} -PxAngoliaApiKey=${xAngoliaApiKey} -PisReleaseBuild=true | tail -1)

setup_git
commit_test
push_git