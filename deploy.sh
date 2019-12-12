#!/usr/bin/env sh

Echo'Start Executing Commands'
# Generate static files
Echo'Execute the command: gitbook build. '
gitbook build .

# Enter the generated folder
Echo "Executing commands: cd. /_book n"
cd ./_book

# Initialize a repository, just do an initialization operation, the files in the project have not been tracked.
Echo "Executing commands: git init\n"
git init

# Save all changes
Echo "Executing commands: git add-A"
git add -A

# Submit the revised document
Echo "Execute commands: commit-m'deploy'"
git commit -m 'deploy'

# If published to https://<USERNAME>.github.io/<REPO>
Echo: "Execute commands: git push-f https://github.com/TencentLBS/TencentMapDemo_Android.git master:gh-pages"
git push -f https://github.com/TencentLBS/TencentMapDemo_Android.git master:gh-pages

# Return to the last working directory
Echo "Back to the working directory just now"
cd -