FROM ubuntu:latest
LABEL authors="nikamkheidze"

ENTRYPOINT ["top", "-b"]