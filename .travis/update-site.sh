#!/bin/bash
set -ev

git config --global user.email "nel.taurisson@gmail.com"
git config --global user.name "nelt"

mvn org.apache.maven.plugins:maven-help-plugin:evaluate -Dexpression=project.version -o
VERSION=$(mvn org.apache.maven.plugins:maven-help-plugin:evaluate -Dexpression=project.version | grep -Ev '(^\[|Download\w+:)')


echo "updating site for version $VERSION"

rm -rf target/gh-pages
git clone -b gh-pages https://github.com/nelt/codingmatters-code-graph.git target/gh-pages

ant -f jacoco-merged-coverage-report.xml -lib jacoco/lib

mkdir -p target/gh-pages/jacoco-coverage
cp -r target/report target/gh-pages/jacoco-coverage/$VERSION

HERE=` pwd`  
cd target/gh-pages
git remote rm origin
git remote add origin https://${GH_TOKEN}@github.com/nelt/codingmatters-code-graph.git

git add jacoco-coverage/$VERSION 
git commit -a -m "update site with jacoco coverage for version $VERSION"

git push

cd $HERE
echo "done."
