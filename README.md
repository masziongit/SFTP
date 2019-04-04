# SFTP

Configuration
---------------
```sh
#SFTP server
sftp.host=192.168.99.100
sftp.port=2222
sftp.user=foo
#sftp.pass=pass
sftp.ssh.keyfile=nopp
sftp.ssh.passphrase=

#Must have / in last char
sftp.src.path=/upload/
sftp.dest.path=D:\\Users\\

sftp.ls.path=*
sftp.recursive.download.file=false

#Don't have / in last char
sftp.backup.path=/upload/back_up_
sftp.backup.dateformat=yyyyMMdd
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


