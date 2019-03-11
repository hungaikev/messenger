FROM openjdk:8-jdk-slim

ENV APP_NAME app-name
ENV APP_VERSION 1.0-SNAPSHOT
ENV PROJECT_DIR tmp/app-name
ENV SBT_VERSION 1.2.8

RUN apt-get update
RUN apt-get install -y wget unzip tar zip

WORKDIR $PROJECT_DIR
RUN wget http://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb -P /tmp/ && \
dpkg -i /tmp/sbt-$SBT_VERSION.deb && \
apt-get update && \
apt-get install sbt && \
sbt sbtVersion

COPY / $PROJECT_DIR/

RUN ls -la $PROJECT_DIR/* 

RUN sbt -no-colors clean
