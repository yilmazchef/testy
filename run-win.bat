
.\mvnw.cmd dependency:go-offline -Pproduction && ^
.\mvnw.cmd clean package -DskipTests $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Pproduction spring-boot:run
