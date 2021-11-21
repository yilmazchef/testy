
.\mvnw.cmd dependency:go-offline -Pproduction && ^
.\mvnw.cmd clean install -DskipTests $JAVA_OPTS -Pproduction spring-boot:run
