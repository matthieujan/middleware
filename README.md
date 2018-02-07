# Social Network Project

## Maven projects

* Provided projects
    * `fr.ensibs.socialnetwork.core`
    * `fr.ensibs.socialnetwork.logic`
    * `fr.ensibs.socialnetwork.swing`
    
* *TODO* projects
    * `fr.ensibs.socialnetwork.common`
    * `fr.ensibs.socialnetwork.client`
    * `fr.ensibs.socialnetwork.server`


## Bash scripts

* `prepare.sh`: compile and install the provided projects

* `install.sh`: compile and install the *TODO* projects
    * the `fr.ensibs.socialnetwork.client` build should generate a
      `socialnetwork.client-N.jar executable jar in the `target`
      directory, where N is a version number representing the project
      step
    * the `fr.ensibs.socialnetwork.server` build should generate a
      `socialnetwork.server-N.jar executable jar in the `target`
      directory, where N is a version number representing the project
      step
      
* `clean.sh`: clean the *TODO* projects

* `run_server.sh`: run the server project
    * writes the server hostname in a $HOME/socialnetwork.conf file 

* `run_client.sh`: run the client project
    * reads the server hostname in the $HOME/socialnetwork.conf file
    * this script should be launched on a different host as the server
      script

## Test procedure

### Prepare

Done once at the beginning, from a directory that contains the
provided projects only

    ./prepare.sh

### Project test

Done for each `student.zip` file, with N=1,2,3,4:

In a terminal
    unzip student.zip
    ./install.sh
    ./run_server.sh N

In another terminal (one for each client)
    ssh -X <a_remote_host>
    cd <the_directory_that_contains_student.zip>
    ./run_client.sh N
