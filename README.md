# siged

SIGED
Uso do Docker compose<br>
Instalar no windows :

* Docker: https://www.docker.com/

* Virtual Box: https://www.virtualbox.org/

* OpenJDK 8 : https://adoptium.net/temurin/releases/?version=8

Primeiramente, certifique-se de ter o docker e o docker compose instalados no sistema

```
$ docker version
$ docker compose version
```

Execute no terminal na pasta do projeto

No windows coloque as a variavel de ambiente no arquivo .env_dev

```
$ git clone https://github.com/carolcrisostomo/siged.git
$ cd siged
$ docker-compose -f .\docker-compose_dev.yml up -d --no-deps --build
```

> - adminer: http://localhost:38080/
>      - System: PostgreSQL
>      - Server: db
>      - Username: siged
>      - Password: siged
>      - database: siged

```
#Criação do usuário do sistema

INSERT INTO public.usuario(id, ativo, cpf, dataalteracao, datainclusao, email, forcaratualizacaoemail, nome, observacao,
                           senha, senhaexpirada, tipo, login)
VALUES (1, true, 99999999999, NOW(), NOW(), 'postgre@postgre.local', false, 'ipcconsulta', '',
        'e8d95a51f3af4a3b134bf6bb680a213a', false, 1, 'ipcconsulta');

```
        
#SIGED
> - Web: http://localhost:18080/ 
>      - Login: ipcconsulta
>      - Senha: senha


