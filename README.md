# siged

SIGED
Uso do Docker compose
No windows Instalar:

Docker: https://www.docker.com/

Virtual Box: https://www.virtualbox.org/

OpenJDK 8 : https://adoptium.net/temurin/releases/?version=8

Primeiramente, certifique-se de ter o docker e o docker compose instalados no sistema

$ docker version
$ docker compose version
Execute no terminal na pasta do projeto

No windows coloque as a variavel de ambiente no arquivo .env_dev

$ git clone https://tiagojesusbr@bitbucket.org/tiagojesusbr/siged.git
$ cd siged
$ docker-compose -f .\docker-compose_dev.yml up -d --no-deps --build
Web: http://localhost:18080/ pgadmin: http://localhost:28080/ adminer: http://localhost:38080/

Banco usuário e senha: siged
