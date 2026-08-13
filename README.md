# Tickets

Sistema web de gerenciamento de tickets desenvolvido com **Java, Spring
Boot, Maven, PostgreSQL, Flyway, Thymeleaf e Docker**.

## Pré-requisitos

Instale Java (na versão configurada no `pom.xml`), Maven, Git e Docker
Desktop/Docker Engine.

``` bash
java --version
mvn --version
docker --version
docker compose version
git --version
```

No Windows com Docker Desktop/WSL 2, a virtualização do processador deve
estar habilitada.

## Clonar e iniciar

``` bash
git clone <URL_DO_REPOSITORIO>
cd tickets
docker compose up -d
mvn clean package
mvn spring-boot:run
```

Depois acesse:

``` text
http://localhost:8080
```

Usuários não autenticados devem ser redirecionados para o login.

## PostgreSQL com Docker

O ambiente de desenvolvimento utiliza o PostgreSQL definido em
`compose.yaml`.

``` bash
docker compose up -d
docker ps
```

Atualmente, o PostgreSQL do container é exposto na porta **5433 do
host**, evitando conflito com uma possível instalação local do
PostgreSQL na porta padrão 5432.

A conexão padrão esperada pela aplicação é:

``` text
jdbc:postgresql://localhost:5433/tickets_db
```

Dentro do container, o PostgreSQL continua usando a porta `5432`.

## Persistência do banco

O banco utiliza um **named volume do Docker**, portanto os dados
permanecem entre reinicializações.

Uso normal:

``` bash
docker compose down
docker compose up -d
```

Isso preserva os dados.

Para apagar deliberadamente todo o banco de desenvolvimento:

``` bash
docker compose down -v
docker compose up -d
```

> `-v` remove o volume e todos os dados armazenados nele. Não use no
> fluxo diário.

## Flyway e migrations

As migrations estão em:

``` text
src/main/resources/db/migration/
```

Na primeira execução contra um banco vazio, o Flyway executa as
migrations em ordem. Depois, executa apenas migrations novas.

Após uma migration ter sido compartilhada/aplicada, **não edite o
arquivo antigo**. Crie uma nova migration, por exemplo:

``` text
V10__descricao_da_alteracao.sql
V11__outra_alteracao.sql
```

Isso evita erros de checksum e mantém os bancos dos desenvolvedores
consistentes.

## application.properties

As configurações principais estão em:

``` text
src/main/resources/application.properties
```

Exemplo do ambiente atual:

``` properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5433/tickets_db}
spring.datasource.username=${DB_USERNAME:tickets}
spring.datasource.password=${DB_PASSWORD:tickets_dev_password}

spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
```

Se `DB_URL`, `DB_USERNAME` ou `DB_PASSWORD` existirem no ambiente, elas
sobrescrevem os valores padrão.

**Nunca versione credenciais reais de produção.**

## Build

``` bash
mvn clean package
```

O artefato gerado fica em:

``` text
target/
```

`target/` é gerado localmente e não deve ser versionado.

## Fluxo da primeira execução

``` text
Docker inicia PostgreSQL
        ↓
Spring conecta ao banco
        ↓
Flyway encontra o banco vazio
        ↓
Migrations são executadas
        ↓
Hibernate valida o schema
        ↓
Dados iniciais de desenvolvimento são criados
        ↓
Aplicação inicia
```

Depois disso, o volume Docker preserva o estado do banco.

No dia a dia, normalmente basta:

``` bash
git pull
docker compose up -d
mvn spring-boot:run
```

## Acessar diretamente o banco do container

``` bash
docker exec -it tickets-postgres psql -U tickets -d tickets_db
```

Dentro do `psql`:

``` sql
\dt
SELECT * FROM flyway_schema_history;
```

Para sair:

``` text
\q
```

## Estrutura principal

``` text
tickets/
├── compose.yaml
├── pom.xml
├── README.md
├── .gitignore
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/rodrigo/tickets/
    │   │       ├── controller/
    │   │       ├── dto/
    │   │       ├── entity/
    │   │       ├── repository/
    │   │       └── service/
    │   └── resources/
    │       ├── application.properties
    │       ├── db/migration/
    │       ├── templates/
    │       └── static/
    └── test/
```

-   `controller/`: requisições HTTP, navegação e endpoints.
-   `dto/`: transporte de dados entre camadas/formulários.
-   `entity/`: entidades persistidas pelo JPA/Hibernate.
-   `repository/`: acesso ao banco com Spring Data JPA.
-   `service/`: regras de negócio.
-   `db/migration/`: evolução versionada do banco pelo Flyway.
-   `templates/`: páginas Thymeleaf.
-   `static/`: CSS e demais recursos estáticos.

## Trabalho com Git

Fluxo recomendado:

``` bash
git checkout main
git pull
git checkout -b feature/nome-da-feature
```

Depois da implementação:

``` bash
mvn clean package
git add .
git commit -m "Descrição da alteração"
git push -u origin feature/nome-da-feature
```

Abra um Pull Request para a `main`. Depois que alterações forem
integradas:

``` bash
git checkout main
git pull
```

Migrations novas serão aplicadas automaticamente pelo Flyway na próxima
inicialização.

## .gitignore

Recomendado:

``` gitignore
target/
.env
.idea/
*.iml
.vscode/
*.log
logs/
.DS_Store
Thumbs.db
```

O `compose.yaml` e `src/main/resources/db/migration/` **devem ser
versionados**. O volume PostgreSQL é gerenciado pelo Docker e não fica
no repositório.

## Problemas comuns

### Docker Engine Stopped

No Windows, confira a virtualização na BIOS/UEFI e o funcionamento do
WSL 2.

### Porta 5432 ocupada

Uma instalação local do PostgreSQL pode usar a porta 5432. Por isso, o
PostgreSQL Docker deste projeto está exposto na porta 5433 do host.

### Flyway checksum mismatch

Uma migration já aplicada provavelmente foi alterada. Em um repositório
compartilhado, mantenha migrations antigas imutáveis e crie uma nova
versão.

### Spring não conecta ao banco

Confira:

``` bash
docker ps
```

e verifique se `application.properties`, variáveis de ambiente e
`compose.yaml` utilizam porta e credenciais compatíveis.

## Desenvolvimento e produção

O `compose.yaml` atual atende principalmente ao desenvolvimento:

``` text
Spring Boot → Maven/Java no host
PostgreSQL  → Docker
```

Posteriormente, a própria aplicação Spring Boot pode ser containerizada:

``` text
Docker Compose
├── tickets-app
└── PostgreSQL
```

Configurações de produção --- credenciais, domínio, HTTPS, backups e
volumes --- devem ser tratadas separadamente das configurações locais.
