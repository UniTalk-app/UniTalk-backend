docker run -p 5432:5432 --name postgres -e POSTGRES_PASSWORD=passwd -d --rm postgres
while ! timeout 1 bash -c "echo > /dev/tcp/localhost/5432"; do echo "Waiting for Postgres..."; sleep 1; done
mvn spring-boot:run
