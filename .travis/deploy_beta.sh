#!/bin/bash

printf "\n--------------------\nDeploying for beta branch\n--------------------\n"

./gradlew publishApkRelease -PpredatorStoreFile=${storeFile} -PpredatorStorePassword=${storePass} -PpredatorKeyAlias=${keyAlias} -PpredatorKeyPassword=${keyPass} -PapiKey=${apiKey} -PapiSecret=${apiSecret} -PsearchUrl=${searchUrl} -PxAngoliaAgent=${xAngoliaAgent} -PxAngoliaApplicationId=${xAngoliaApplicationId} -PxAngoliaApiKey=${xAngoliaApiKey} -PisReleaseBuild=true