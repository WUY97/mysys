FROM amazoncorretto:17

LABEL authors="Yitong"

COPY ./target/admin.jar \
        ./target/auth.jar \
        ./target/inventory.jar \
        ./target/product.jar \
        ./target/order.jar \
        /usr/jar/

ARG SPRING_PROFILES_ACTIVE
ARG GCP_PROJECT_ID
ARG GCP_CREDENTIALS
ARG GCP_SQL_INSTANCE_CONNECTION_NAME
ARG GCP_SQL_DATABASE_NAME
ARG GCP_SQL_USERNAME
ARG GCP_SQL_PASSWORD

ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}
ENV GCP_PROJECT_ID=${GCP_PROJECT_ID}
ENV GCP_CREDENTIALS=${GCP_CREDENTIALS}
ENV GCP_SQL_INSTANCE_CONNECTION_NAME=${GCP_SQL_INSTANCE_CONNECTION_NAME}
ENV GCP_SQL_DATABASE_NAME=${GCP_SQL_DATABASE_NAME}
ENV GCP_SQL_USERNAME=${GCP_SQL_USERNAME}
ENV GCP_SQL_PASSWORD=${GCP_SQL_PASSWORD}

CMD ["java", "-jar", "/usr/jar/admin.jar"]
CMD ["java", "-jar", "/usr/jar/auth.jar"]
CMD ["java", "-jar", "/usr/jar/inventory.jar"]
CMD ["java", "-jar", "/usr/jar/product.jar"]
CMD ["java", "-jar", "/usr/jar/order.jar"]

