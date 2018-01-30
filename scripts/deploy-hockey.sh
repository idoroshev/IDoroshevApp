#!/usr/bin/env bash
curl \
-F "status=2" \
-F "notify=0" \
-F "ipa=@app/build/outputs/apk/release/app-release.apk" \
-H "X-HockeyAppToken: 9943199494f7446ea5b703ccc9734285" \
https://rink.hockeyapp.net/api/2/apps/84f0f25bd4974458b7a3c92f5d5bde05/app_versions/upload