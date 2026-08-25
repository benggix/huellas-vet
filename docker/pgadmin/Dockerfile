FROM dpage/pgadmin4:8

USER root

COPY docker/pgadmin/servers.json /pgadmin4/servers.json
RUN chown 5050:5050 /pgadmin4/servers.json

USER 5050