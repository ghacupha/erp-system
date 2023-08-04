#!/bin/bash

# Create a non-root user inside the container
adduser nonrootuser

# Assigning a random password to the user
password=$(date +%s | sha256sum | base64 | head -c 32)
echo "nonrootuser:$password" | chpasswd

# Modify user's shell to /bin/bash if needed
usermod -s /bin/bash nonrootuser
