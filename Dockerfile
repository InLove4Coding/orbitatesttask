FROM amazoncorretto:11
COPY target/orbita.jar orbita.jar
ENTRYPOINT java -jar orbita.jar