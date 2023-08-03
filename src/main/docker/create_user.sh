#!/bin/bash

#adduser --disabled-password nonrootuser


If the --disabled-password option is not supported in the container's environment, you can use other options that are generally supported in most Linux environments. One common approach is to create the user without specifying a password and then later assign a random password or no password at all.

Here's an example of how you could modify the create_user.sh script:

bash
Copy code
#!/bin/bash

# Create a non-root user inside the container
adduser nonrootuser

# Assigning a random password to the user
password=$(date +%s | sha256sum | base64 | head -c 32)
echo "nonrootuser:$password" | chpasswd

# Modify user's shell to /bin/bash if needed
usermod -s /bin/bash nonrootuser
