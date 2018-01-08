#!/bin/bash

# If this is fork - just exit
if [[ -n "${TRAVIS_PULL_REQUEST}" && "${TRAVIS_PULL_REQUEST}" != "false"  ]]; then
  echo -e '\n============== deploy will not be started (from the fork) ==============\n'
  exit 0
fi

# Choose necessary argument according to the current branch.
if [[ $TRAVIS_BRANCH == 'develop' ]]; then
    echo "assembleDebug"
elif [[ $TRAVIS_BRANCH == 'beta' ]]; then
    echo "publishApkRelease"
elif [[ $TRAVIS_BRANCH == 'master' ]]; then
    echo "publishRelease"
else
    exit 0
fi