FROM tomee:10.0.1-ubuntu-plus

# Install unzip if not already available
RUN apt-get update && apt-get install -y unzip && rm -rf /var/lib/apt/lists/*

# Ensure webapps directory is clean and create the ROOT directory
RUN rm -rf /usr/local/tomee/webapps/* && mkdir -p /usr/local/tomee/webapps/ROOT

# Copy the WAR file, unzip it to ROOT directory, and then remove the WAR
COPY target/primefaces-extensions-showcase-*.war /tmp/primefaces-extensions-showcase.war
RUN unzip -q /tmp/primefaces-extensions-showcase.war -d /usr/local/tomee/webapps/ROOT && rm /tmp/primefaces-extensions-showcase.war

ENV CATALINA_OPTS="-Xms512m -Xmx1024m"

EXPOSE 8080

CMD ["catalina.sh", "run"]
