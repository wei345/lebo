## 开发环境

* Java 1.6 以上。 - [JDK 1.6u45下载](http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase6-419409.html#jdk-6u45-oth-JPR)
* Maven 3.0.3 以上。 - [Maven 3.0.5下载](http://apache.etoak.com/maven/maven-3/3.0.5/binaries/apache-maven-3.0.5-bin.zip)
* MongoDB 2.4.4。 - [MongoDB下载](http://www.mongodb.org/downloads)
* Memcached 1.4.5 - [Memcached安装](https://code.google.com/p/memcached/wiki/NewInstallFromPackage)

注意：Maven仓库路径不能有空格，否则会导致build失败。可在`$MAVEN_HOME/conf/settings.xml`中设置：

    <localRepository>/path/to/local/repo</localRepository>

## 第一次build

### SpringSide

执行下面命令从源码构建并安装SpringSide4，初次执行需要下载不少依赖，耐心等待。

    git clone https://github.com/springside/springside4.git
    cd springside4
    git co V4.1.0.GA
    cd modules
    mvn clean install -Dmaven.test.skip=true
    mvn source:jar install

看到“BUILD SUCCESS”，说明SpringSide4安装成功。

### IK Analyzer

    git clone https://github.com/wks/ik-analyzer.git
    cd ik-analyzer
    mvn clean install -Dmaven.test.skip=true
    mvn source:jar install

### Lebo

执行下面命令克隆lebo --> 下载依赖 --> 编译 --> 启动，lebo启动时会连接MongoDB 127.0.0.1:27017、Memcached 127.0.0.1:11211，连不上会报错。

    git clone https://git.oschina.net/weiliu/lebo.git
    cd lebo
    mvn jetty:run

启动完成会看到：

    2013-07-01 23:54:42.427:INFO:oejs.AbstractConnector:Started SelectChannelConnector@0.0.0.0:8080
    [INFO] Started Jetty Server

浏览器访问http://localhost:8080/api.json，会看到

    {"error":{"message":"Unauthorized","code":10401}}

用HTTP客户端工具，如火狐扩展HttpRequester，发送：

    POST http://localhost:8080/api/v1/login.json?provider=weibo&uid=1774156407&token=2.00vHLEwBz7QwTCbafc736d580QUCCY

会看到：

    {"screenName":"法图_麦","name":"法图_麦","profileImageUrl":"http://tp4.sinaimg.cn/1774156407/50/5657962784/0","provider":"weibo","uid":"1774156407","token":"2.00vHLEwBz7QwTCbafc736d580QUCCY"}

发送：

    GET http://localhost:8080/api.json

会看到：

    It works

进入MongoDB shell，

    use test
    db.openUser.count()    #输出 1
    db.openUser.findOne()  #输出 {..."uid" : "1774156407"...}
    db.user.findOne()      #输出 {..."screenName" : "法图_麦"...}

微博登录成功。

要停止lebo，按Ctrl + C。

上面演示了在命令行中快速启动。开发时通常在IDE中启动，方便调试。

## IDE

### IDEA

IDEA对Maven支持良好，选中pom.xml导入。

### Eclispe

执行下面命令会下载依赖包的源码，然后会生成eclipse项目文件。

    cd lebo
    mvn eclipse:eclipse

用eclipse选中lebo目录导入。

### 启动

在IDE中，运行启动类com.lebo.QuickStartServer，也可以以debug方式运行。

### 代码

* 不格式化/src/main/webapp/static

## 依赖包源码 & JavaDoc

执行以下命令下载源码：

    mvn dependency:sources

阿里云SDK没有源码，执行下面命令下载JavaDoc:

    mvn dependency:resolve -Dclassifier=javadoc -DincludeArtifactIds=aliyun-openservices
