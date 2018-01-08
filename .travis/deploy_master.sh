#!/bin/bash

printf "\n--------------------\nDeploying for master branch\n--------------------\n"

# Get app version via gradle task.
versionName=$(./gradlew -q getVersionName | tail -1)

printf "\n--------------------\nCreating tag for ${versionName}\n--------------------\n"

# Create git tag according to version.
git tag -a versionName
git push origin versionName