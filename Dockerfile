FROM maven:3.9.1-eclipse-temurin-17 as build
COPY todo-repository/src /home/app/todo-repository/src
COPY todo-service/src /home/app/todo-service/src
COPY todo-rest/src /home/app/todo-rest/src
COPY pom.xml /home/app
COPY todo-repository/pom.xml /home/app/todo-repository
COPY todo-service/pom.xml /home/app/todo-service
COPY todo-rest/pom.xml /home/app/todo-rest
RUN mvn -f /home/app/pom.xml clean package

FROM tomcat:10.1.8-jdk17
COPY --from=build /home/app/todo-rest/target/todo.war /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh", "run"]
