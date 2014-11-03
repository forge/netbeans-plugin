netbeans-plugin
===============

NetBeans UI support for Forge 2

Build and Run
==============

    $ mvn install
    $ cd application/
    $ mvn  nbm:cluster-app nbm:run-platform

works from command line. Of course, opening the sources in NetBeans IDE will 
give you more options than a command line. 

At the end of your builder job you can call:

    $ mvn nbm:autoupdate

which will generate target/netbeans_site/updates.xml catalog. If you publish 
it, users who add it into Tools/Plugins/Settings will be able to install your 
work into their existing NetBeans 8.0.x IDE.

