## 环境

* Java 1.6+, [JDK 1.6u45下载](http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase6-419409.html#jdk-6u45-oth-JPR)
* [Maven](http://maven.apache.org/) 3.0.3+
* [MongoDB](http://www.mongodb.org) 2.4.4
* [MySQL](http://dev.mysql.com/downloads/) 5.5
* [Redis](http://redis.io/) 2.6.14
* [ActiveMQ](http://activemq.apache.org/) 5.7
* 阿里云OSS
* [Tomcat](http://tomcat.apache.org/) 7

host,port配置`src/main/resources/application.properties`

特定环境配置`src/main/portable/portable-xxx.xml`

## 依赖

### SpringSide4

    git clone https://github.com/springside/springside4.git
    cd springside4
    git co V4.1.0.GA
    cd modules
    mvn clean install -Dmaven.test.skip=true
    mvn source:jar install

### IK Analyzer

    git clone https://github.com/wks/ik-analyzer.git
    cd ik-analyzer
    mvn clean install -Dmaven.test.skip=true
    mvn source:jar install

### Logback Redis Appender

    git clone https://github.com/kmtong/logback-redis-appender
    cd logback-redis-appender
    mvn install
    mvn source:jar install

### Alipay SDK

[下载Alipay SDK](https://openhome.alipay.com/doc/docIndex.htm#goto=https://openhome.alipay.com/doc/toSdk.htm)

    mvn install:install-file -Dfile=alipay-sdk-java20131029120045.jar \
                             -Dsources=alipay-sdk-java20131029120045-source.jar \
                             -DgroupId=com.alipay \
                             -DartifactId=alipay-sdk \
                             -Dversion=20131029120045 \
                             -Dpackaging=jar \
                             -DgeneratePom=true \
                             -DcreateChecksum=true

## 源码

下载依赖源码：

    mvn dependency:sources

阿里云SDK没有源码，下载JavaDoc:

    mvn dependency:resolve -Dclassifier=javadoc -DincludeArtifactIds=aliyun-openservices

## Getting Started

### IntelliJ IDEA

IDEA对Maven支持很好，选中pom.xml导入。

### Eclispe

执行下面命令会下载依赖包的源码，然后会生成eclipse项目文件。

    cd lebo
    mvn eclipse:eclipse

用eclipse选中lebo目录导入。

### 启动

在IDE中，运行com.lebo.QuickStartServer，调试时可以以debug方式运行。


