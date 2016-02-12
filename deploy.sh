GIT_HASH=`git rev-parse --short HEAD`

# For α version. Remove this after 1.0.0 release.
./gradlew clean assembleProductionDebug
curl -F "file=@app/build/outputs/apk/app-production-debug.apk" -F "token=${DEPLOY_GATE_API_KEY}" -F "message=https://github.com/konifar/droidkaigi2016/tree/${GIT_HASH} https://circleci.com/gh/konifar/droidkaigi2016/${CIRCLE_BUILD_NUM}" -F "distribution_key=0d34ff6b3ccedf2a76771d9b6002c55b4f0aac1f" -F "release_note=https://github.com/konifar/droidkaigi2016/tree/${GIT_HASH} https://circleci.com/gh/konifar/droidkaigi2016/${CIRCLE_BUILD_NUM}" https://deploygate.com/api/users/konifar/apps

# For β version. Remove comment out after first ci success.
./gradlew clean assembleProductionRelease
curl -F "file=@app/build/outputs/apk/app-production-release.apk" -F "token=${DEPLOY_GATE_API_KEY}" -F "message=https://github.com/konifar/droidkaigi2016/tree/${GIT_HASH} https://circleci.com/gh/konifar/droidkaigi2016/${CIRCLE_BUILD_NUM}" -F "distribution_key=901884d8a27b8bd9c368109b0cc5c941a6821076" -F "release_note=https://github.com/konifar/droidkaigi2016/tree/${GIT_HASH} https://circleci.com/gh/konifar/droidkaigi2016/${CIRCLE_BUILD_NUM}" https://deploygate.com/api/users/konifar/apps
