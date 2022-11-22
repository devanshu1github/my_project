FROM openjdk:8-jre-alpine

EXPOSE 8080

COPY target/springboot-crud-k8s-example-*.jar /usr/app/
WORKDIR /usr/app

CMD java -jar springboot-crud-k8s-example-*.jar