#! /bin/bash

if [ $# -eq 1 ] && [ "$1" == "-h" ]; then
    echo "Usage: $0"
    echo "Clean the socialnetwork Maven projects"
    exit -1
fi

for project in socialnetwork.common socialnetwork.client socialnetwork.server ; do
    if [ -d "fr.ensibs.$project" ]; then
	cd "fr.ensibs.$project"
	mvn clean
	cd ..
    fi
    rm -rf $HOME/.m2/repository/fr/ensibs/$project
done
