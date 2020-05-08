
# 打包文件
## 修改 application.yml 配置文件

服务器IP  
39.105.25.16 (公网)
172.17.127.72 (内网)

## 需要Dockerfile 文件
```
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```
## 需要pom.xml 插件
```
            <plugin>
                <groupId>com.spotify</groupId>
                <artifactId>dockerfile-maven-plugin</artifactId>
                <version>1.3.6</version>
                <configuration>
                    <repository>${project.artifactId}</repository>
                    <buildArgs>
                        <JAR_FILE>target/${project.build.finalName}.jar</JAR_FILE>
                    </buildArgs>
                </configuration>
            </plugin>
```

## 打包

sudo mvn package dockerfile:build -e -DskipTests

# 登录
 sudo docker login --username=pwalan registry.cn-hangzhou.aliyuncs.com

# 打tag
sudo docker tag [ImageId] registry.cn-hangzhou.aliyuncs.com/xnt-pcs/pcs:[镜像版本号]

例如 ：
sudo docker tag 9c6aead4e315 registry.cn-hangzhou.aliyuncs.com/xnt-pcs/pcs:evaluation

ImageId为镜像id 通过 sudo docker images 查看

# push 到私有云
sudo docker push registry.cn-hangzhou.aliyuncs.com/xnt-pcs/pcs:[镜像版本号]

例如 ： 
sudo docker push registry.cn-hangzhou.aliyuncs.com/xnt-pcs/pcs:evaluation

# 阿里云 启动
```
ssh root@39.105.25.16
Yang1290
cd pcs
ll
./eureka.sh
./evaluation.sh
./exam.sh
./gateway.sh
./homepage.sh
./org.sh
./orgui.sh
./platform.sh
./scale.sh
./statistic.sh
./system.sh
./sysui.sh
```

