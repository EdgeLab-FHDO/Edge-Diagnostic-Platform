FROM ubuntu:16.04

MAINTAINER akashafh20 "theodore.lumaksono@yahoo.com"

COPY install.sh install.sh
RUN chmod +x install.sh && sync && ./install.sh && rm install.sh

VOLUME /gen
WORKDIR /gen
CMD bash