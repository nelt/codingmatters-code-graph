#!/bin/bash
set -ev

touch git-credentials.txt
git config credential.helper "store --file=git-credentials.txt"
echo "https://${GH_TOKEN}:@github.com" > git-credentials.txt
