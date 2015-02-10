#!/bin/bash
set -ev

git config credential.helper "store --file=target/git-credentials"
echo "https://${GH_TOKEN}:@github.com" > target/git-credentials
