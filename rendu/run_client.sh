#! /bin/bash

if [ $# -ne 1 ] || [ "$1" == "-h" ]; then
    echo "Usage: $0 [version]"
    echo "Launch the socialnetwork client application with"
    echo "the server hostname loaded from the $HOME/socialnetwork.conf file"
    exit -1
fi

version=$1
project=socialnetwork.client
jar_file=target/$project-$version.jar

# Check the server hostname
if [ -f $HOME/socialnetwork.conf ]; then
    server_host=$(grep "SERVER_HOST=" $HOME/socialnetwork.conf | cut -d= -f2)
    if [ "$server_host" == "" ]; then
	echo "No SERVER_HOST property in $HOME/socialnetwork.conf. Is the server running?"
    else
	echo "Server hostname: $server_host"
	if [ "$server_host" == "$HOSTNAME" ]; then
	    echo "The server seems to be running on the same host. You should try another host"
	fi
    fi
else
    echo "No $HOME/socialnetwork.conf found. Is the server running?"
fi

# Launch the client
if [ -d fr.ensibs.$project ]; then
    if [ -f fr.ensibs.$project/$jar_file ]; then
	cd fr.ensibs.$project
	java -jar $jar_file
	cd ..
    else
	echo "JAR file fr.ensibs.$project/$jar_file not found"
    fi
else
    echo "Project fr.ensibs.$project not found"
fi
