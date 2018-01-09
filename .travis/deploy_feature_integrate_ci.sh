#!/bin/bash

printf "\n--------------------\nDeploying for feature-integrate_ci branch\n--------------------\n"

# Get app version via gradle task.
versionName=$(./gradlew -q getVersionName -PpredatorStoreFile=${storeFile} -PpredatorStorePassword=${storePass} -PpredatorKeyAlias=${keyAlias} -PpredatorKeyPassword=${keyPass} -PapiKey=${apiKey} -PapiSecret=${apiSecret} -PsearchUrl=${searchUrl} -PxAngoliaAgent=${xAngoliaAgent} -PxAngoliaApplicationId=${xAngoliaApplicationId} -PxAngoliaApiKey=${xAngoliaApiKey} -PisReleaseBuild=true | tail -1)

printf "\n--------------------\nCreating test branch for ${versionName}\n--------------------\n"

# Create test branch according to version.
git checkout -b "test_branch_${versionName}"
git push origin "test_branch_${versionName}"