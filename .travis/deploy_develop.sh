#!/bin/bash

printf "\n--------------------\nDeploying for develop branch\n--------------------\n"

./gradlew assembleRelease -PpredatorStoreFile=${storeFile} -PpredatorStorePassword=${storePass} -PpredatorKeyAlias=${keyAlias} -PpredatorKeyPassword=${keyPass} -PapiKey=${apiKey} -PapiSecret=${apiSecret} -PsearchUrl=${searchUrl} -PxAngoliaAgent=${xAngoliaAgent} -PxAngoliaApplicationId=${xAngoliaApplicationId} -PxAngoliaApiKey=${xAngoliaApiKey} -PisReleaseBuild=true