#!/bin/bash
set -ev

touch target/git-credentials.txt
git config credential.helper "store --file=target/git-credentials.txt"
echo "https://${GH_TOKEN}:@github.com" > target/git-credentials.txt
