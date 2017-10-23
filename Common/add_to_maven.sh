mvn package
mvn install:install-file -Dfile=target/Chat_Common.jar -DgroupId=otus.project -DartifactId=horizontal-scaling-chat.common -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true