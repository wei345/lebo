## 环境

* Java 1.6 以上。 - [JDK 1.6u45下载](http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase6-419409.html#jdk-6u45-oth-JPR)
* Maven 3.0.3 以上。 - [Maven 3.0.5下载](http://apache.etoak.com/maven/maven-3/3.0.5/binaries/apache-maven-3.0.5-bin.zip)
* MongoDB 2.4.4。 - [MongoDB下载](http://www.mongodb.org/downloads)

### Windows设置Maven仓库路径

在Windows XP上，Maven本地仓库默认为：

    C:/Documents and Settings/用户名/.m2/repository

这个路径中含有空格，会导致build失败。

编辑`$MAVEN_HOME/conf/settings.xml`，找到这行：

    <localRepository>/path/to/local/repo</localRepository>

去掉注释，改为不带空格的路径，如：

    <localRepository>D:\maven_repository</localRepository>

## 开始

### SpringSide

执行下面命令从源码构建并安装SpringSide4，初次执行需要下载不少依赖，耐心等待。

    git clone https://github.com/springside/springside4.git
    cd springside4
    git co V4.0.1.GA
    cd modules
    mvn clean install -Dmaven.test.skip=true

看到“BUILD SUCCESS”，说明SpringSide4安装成功。

### Lebo

执行下面命令克隆lebo --> 下载依赖 --> 编译 --> 启动，lebo启动时会连接MongoDB 127.0.0.1:27017，连不上会报错。

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

## 开发

### 导入到IDE中

#### Eclispe

    cd lebo
    mvn eclipse:eclipse

会下载所有依赖包的源码，方便查阅。完成后会生成eclipse项目文件，eclipse可直接导入lebo目录。

#### IDEA

IDEA原生支持Maven，可直接导入。

### 启动

在IDE中，启动com.lebo.QuickStartServer，可以debug方式启动。
