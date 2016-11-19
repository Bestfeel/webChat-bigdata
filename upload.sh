#!/usr/bin/env bash

mvn clean compile  package  -DskipTests=true


#scp  /Users/feel/githome/gizwits/webChat-bigdata/target/webChat-bigdata.war   root@report:/opt/tomcat/webapps/


#rsync -a -z -v --progress --delete -e ssh  /Users/feel/githome/gizwits/webChat-bigdata/target/webChat-bigdata.war   root@report:/opt/tomcat/webapps/

#rsync -a -z -v --progress --delete -e ssh  /Users/feel/githome/gizwits/webChat-bigdata/target/webChat-bigdata.war   feel@google:/home/feel/tomcat/webapps/
