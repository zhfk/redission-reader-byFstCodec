# docker login docker-registry.bigdata.ytx.com:5000
# docker build -t docker-registry.bigdata.ytx.com:5000/bigdata/redission-reader:1.0 .
# docker push docker-registry.bigdata.ytx.com:5000/bigdata/redission-reader:1.0

FROM frekele/gradle

MAINTAINER Zhang Hefeng <hefeng.zhang@yintech.cn>

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt-get update && apt-get install -y git

WORKDIR /root

RUN git clone https://github.com/zhfk/redission-reader-byFstCodec.git

WORKDIR /root/redission-reader-byFstCodec

RUN gradle bootJar

EXPOSE 8080

ENTRYPOINT  java -jar build/libs/redission-reader-0.0.1-SNAPSHOT.jar