@echo off
cd ../../../../
cd lib
mvn install:install-file -Dfile=autotest-1.0-SNAPSHOT.jar -DgroupId=com.wangxun -DartifactId=autotest -Dversion=1.0-SNAPSHOT -Dpackaging=jar -DgeneratePom=true