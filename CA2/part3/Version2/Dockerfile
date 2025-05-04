FROM gradle:jdk21 AS builder

WORKDIR /app

COPY CA1/part2/pssmatos-gradle_basic_demo/pssmatos-gradle_basic_demo-d8cc2d7443c5/build/libs/basic_demo-0.1.0.jar /app/basic_demo-0.1.0.jar

EXPOSE 59001

ENTRYPOINT ["java", "-cp", "/app/basic_demo-0.1.0.jar", "basic_demo.ChatServerApp", "59001"]