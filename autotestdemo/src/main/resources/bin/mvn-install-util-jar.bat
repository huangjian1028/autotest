@echo off
cd ../../../../
cd lib
mvn install:install-file -Dfile=util-1.0-SNAPSHOT.jar -DgroupId=com.wangxun -DartifactId=util -Dversion=1.0-SNAPSHOT -Dpackaging=jar -DgeneratePom=true
