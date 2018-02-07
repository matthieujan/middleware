#! /bin/bash

if [ $# -ne 1 ] || [ "$1" == "-h" ]; then
    echo "Usage: $0 [version]"
    echo "Write the local hostname in the $HOME/socialnetwork.conf file and"
    echo "launch the socialnetwork server application"
    exit -1
fi

version=$1
project=socialnetwork.server
jar_file=target/$project-$version.jar

# Launch the server
if [ -d fr.ensibs.$project ]; then
    if [ -f fr.ensibs.$project/$jar_file ]; then
	echo "SERVER_HOST=$HOSTNAME" > $HOME/socialnetwork.conf
	cd fr.ensibs.$project
	java -jar $jar_file 
	cd ..
    else
	echo "JAR file fr.ensibs.$project/$jar_file not found"
    fi
else
    echo "Project fr.ensibs.$project not found"
fi

