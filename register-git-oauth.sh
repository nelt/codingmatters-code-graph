#!/bin/bash
set -ev

git config credential.helper "store --file=~/.git/credentials"
echo "https://${GH_TOKEN}:@github.com" > ~/.git/credentials
