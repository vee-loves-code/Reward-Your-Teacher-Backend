FROM openjdk:18
RUN touch /env.txt
RUN printenv > /env.txt
MAINTAINER PodD
COPY target/reward-your-teacher-d.jar reward-your-teacher-d.jar
ENTRYPOINT ["java","-jar","/reward-your-teacher-d.jar"]