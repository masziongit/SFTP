# SFTP

Configuration
---------------
```sh
#SFTP server
sftp.host=192.168.99.100
sftp.port=2222

sftp.user=foo
#sftp.pass=pass
sftp.src.path=/upload/
sftp.dest.path=D:\\Users\\
sftp.ssh.keyfile=nopp
sftp.ssh.passphrase=
sftp.delete.file=true
sftp.ls.path=*CRE*

#Log4j
log.config.file=log4j.properties
```

Build jar command
---------------
*Please install gradle download from https://gradle.org/

Shell
```sh
./gradlew clean build
```
CMD
```sh
gradlew.bat clean build
```

Usage command
---------------
```sh
java -Dconfig.file=${config.properties} -jar ${JAVAPACK.jar} ${mode}
```
  Use -Dconfig.file=${config.properties} to get your config
	
  Use -jar ${JAVAPACK.jar} to get your jarfile to run


