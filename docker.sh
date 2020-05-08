mvn package dockerfile:build -e -DskipTests
docker tag pcs-consult:latest registry.cn-hangzhou.aliyuncs.com/bupt-pcs/pcs:consult
docker push registry.cn-hangzhou.aliyuncs.com/bupt-pcs/pcs:consult