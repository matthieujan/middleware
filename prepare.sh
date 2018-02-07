#! /bin/bash

if [ $# -eq 1 ] && [ "$1" == "-h" ]; then
    echo "Usage: $0"
    echo "Compile the socialnetwork provided projects"
    exit -1
fi

# Compile the socialnetwork provided projects
for project in socialnetwork.core socialnetwork.logic socialnetwork.swing ; do
    if [ -d "fr.ensibs.$project" ]; then
	cd "fr.ensibs.$project"
	mvn install
	if [ $? -ne 0 ]; then
	    echo "ERROR while installing the project fr.ensibs.$project"
	    cd ..
	    exit -1
	fi
	cd ..
    else
	echo "Project fr.ensibs.$project not found"
	exit -1
    fi
done
