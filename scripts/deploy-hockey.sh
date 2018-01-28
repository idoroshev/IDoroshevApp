#!/usr/bin/env bash
curl \
-F "status=2" \
-F "notify=0" \
-F "ipa=@app/build/outputs/apk/release/app-release.apk" \
-H "X-HockeyAppToken: 0b72a455f87e4e478ba9e0a22c9a7681" \
https://rink.hockeyapp.net/api/2/apps/84f0f25bd4974458b7a3c92f5d5bde05/app_versions/upload