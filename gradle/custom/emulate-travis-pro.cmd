@echo off

cls

:: save current directory
pushd .
cd ..\..

set TRAVIS=true
set TRAVIS_JOB_ID=54078341
set SERVICE_NAME=travis-ci
set COVERALLS_REPO_TOKEN=7mBDEXUP7nzDjqza6ZRjakgIvy10aXQDt

set CI_BUILD_NUMBER=42
set CI_BUILD_URL=https://travis-ci.org/OleksandrKucherenko/binding-tc/builds/74676002
::set CI_NAME=

::gradlew coveralls --info %1 %2 %3 %4 %5 %6 %7
gradlew :binder:coveralls --stacktrace --info 2>&1 | tee info.log

:: recover directory
popd
