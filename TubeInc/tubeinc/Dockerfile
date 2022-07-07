FROM ubuntu:20.04

RUN apt-get update

# For removing timezone prompt
ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get install -y apache2 maven cron && apt-get clean

# --------------------- apache webserver -------------------------

# Apache env variables
ENV APACHE_RUN_USER www-data
ENV APACHE_RUN_GROUP www-data
ENV APACHE_LOG_DIR /var/log/apache2 

# Global variables
ARG TOMCAT=apache-tomcat-9.0.59
ARG TOMCAT_PATH=/usr/local/${TOMCAT}

ARG WEBAPP_NAME=SpringBootApp
ARG WEBAPP_PATH=/usr/local/${WEBAPP_NAME}

ARG WEBSITE_DOMAIN=tube
ARG SUBDOMAIN=legacy
ARG WEBSITE_URL=${WEBSITE_DOMAIN}.com
ARG HTML_PATH=/var/www/html

# Setup web pages
WORKDIR /etc/apache2/sites-available

COPY ./web/config/${WEBSITE_DOMAIN}.conf .
RUN a2ensite ${WEBSITE_DOMAIN}.conf
RUN a2dissite 000-default.conf
RUN a2enmod proxy
RUN a2enmod proxy_http
RUN a2enmod headers

RUN rm -Rf ${HTML_PATH}
COPY ./web/html ${HTML_PATH}
RUN chmod -R 755 ${HTML_PATH}


# --------------------- Create CTF user -------------------------

# sha256("ctf_password!")
ARG CTF_PASSWORD=1a41c6a740cb6b1f0a224feb1fa6cd57b6f885073e16635792f493e9c9c9b8f2

RUN useradd -d /home/ctf/ -m -p ${CTF_PASSWORD} -s /bin/bash ctf
RUN echo "ctf:${CTF_PASSWORD}" | chpasswd

# --------------------- Spring4Shell -------------------------

# Install vulnerable Tomcat (9.0.59)
ADD https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.59/bin/${TOMCAT}.tar.gz /tmp/
RUN tar -xf /tmp/${TOMCAT}.tar.gz -C /usr/local
RUN rm -Rf ${TOMCAT_PATH}/webapps/
RUN mkdir ${TOMCAT_PATH}/webapps/
RUN rm /tmp/${TOMCAT}.tar.gz

WORKDIR ${WEBAPP_PATH}
ADD Spring4Shell/src ./src
ADD Spring4Shell/pom.xml .
RUN mvn clean package

#  Deploy to Tomcat
RUN mv target/${WEBAPP_NAME}.war target/ROOT.war
RUN mv target/ROOT.war ${TOMCAT_PATH}/webapps/

RUN chown -R ctf:ctf /home/ctf
RUN chown -R ctf:ctf ${TOMCAT_PATH}
RUN chown -R ctf:ctf ${WEBAPP_PATH}

# --------------------- root cronjob -------------------------

ARG CRON_FILE_PATH=/etc/cron.d/update

RUN touch /var/log/cron.log
COPY ./cron ${CRON_FILE_PATH}
RUN chmod 0644 ${CRON_FILE_PATH}
RUN crontab ${CRON_FILE_PATH}

# --------------------- flags -------------------------

ADD /flags/root.flag /root
ADD /flags/user.flag /home/ctf
RUN chown ctf:ctf /home/ctf/user.flag

WORKDIR /var/www/main

EXPOSE 80
EXPOSE 8080
ENTRYPOINT [ "/bin/sh", "-c", "echo 127.0.0.1 ${WEBSITE_URL} >> /etc/hosts && \
            echo 127.0.0.1 ${SUBDOMAIN}.${WEBSITE_URL} >> /etc/hosts && \
            su - ctf -c '/usr/local/apache-tomcat-9.0.59/bin/catalina.sh start' && \
            service cron start && \
            apachectl -D FOREGROUND" ]