#!/bin/bash
set -ev

VERSION=$(mvn org.apache.maven.plugins:maven-help-plugin:evaluate -Dexpression=project.version -o | grep -Ev '(^\[|Download\w+:)')

echo "updating site for version $VERSION"

rm -rf target/gh-pages
git clone -b gh-pages https://github.com/nelt/codingmatters-code-graph.git target/gh-pages

ant -f jacoco-merged-coverage-report.xml -lib jacoco/lib

mkdir -p target/gh-pages/jacoco-coverage
cp -r target/report target/gh-pages/jacoco-coverage/$VERSION

HERE=$(pwd) 
cd target/gh-pages

git add jacoco-coverage/$VERSION 
#git commit -a -m "update site with jacoco coverage for version $VERSION"
#git push

cd $HERE
echo "done."
