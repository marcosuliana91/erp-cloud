# ERP Cloud Backend

Sistema ERP backend moderno desenvolvido com **Java 25**, **Spring Boot 4** e **Arquitetura Hexagonal**.

## Tecnologias

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| Java | 25 LTS | Linguagem principal |
| Spring Boot | 4.0.0 | Framework principal |
| Spring Data JPA | 4.0.0 | Persistência |
| PostgreSQL | 16.x | Banco de dados |
| Liquibase | 5.x | Migrations |
| SpringDoc OpenAPI | 2.8.0 | Documentação API |
| Maven | 3.x | Build tool |
| Lombok | Latest | Redução de boilerplate |

## Arquitetura

O projeto segue a **Arquitetura Hexagonal** (Ports and Adapters) com separação clara de responsabilidades:

```
src/main/java/com/erp/
├── domain/           # Entidades e regras de negócio (Pure Java)
├── application/      # Casos de uso e portas
├── adapters/         # Adaptadores (REST, Persistência)
├── infrastructure/   # Configurações e implementações técnicas
└── shared/           # Componentes compartilhados
```

### Princípios

- **Domain-Driven Design (DDD)**: Modelo de domínio rico com Value Objects
- **Clean Architecture**: Dependências apontam para dentro
- **SOLID**: Código extensível e manutenível
- **Pure Domain**: Camada de domínio sem dependências de frameworks

## Pré-requisitos

- Java 25+
- Maven 3.8+
- PostgreSQL 16+ ou Docker

## Configuração Rápida

### 1. Banco de Dados (Docker)

```bash
docker run -d \
  --name erp-postgres \
  -e POSTGRES_DB=erp_cloud \
  -e POSTGRES_USER=erp_user \
  -e POSTGRES_PASSWORD=erp_password \
  -p 5432:5432 \
  postgres:16
```

### 2. Compilar e Executar

```bash
# Compilar
mvn clean compile

# Executar
mvn spring-boot:run
```

### 3. Acessar

| Recurso | URL |
|---------|-----|
| API Base | http://localhost:8080/api/v1 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/api-docs |
| Health Check | http://localhost:8080/actuator/health |

## API Endpoints

### Products

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/v1/products` | Criar produto |
| `GET` | `/api/v1/products` | Listar produtos (paginado) |
| `GET` | `/api/v1/products/{id}` | Buscar por ID |
| `PUT` | `/api/v1/products/{id}` | Atualizar produto |
| `DELETE` | `/api/v1/products/{id}` | Deletar produto |

### Exemplos

**Criar Produto:**
```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "code": "PROD-001",
    "description": "Notebook Dell Inspiron",
    "unit": "UN",
    "ncmCode": "8471.30.19",
    "type": "PRODUCT",
    "brand": "Dell",
    "family": "Notebooks",
    "unitPrice": 3999.99,
    "stockQuantity": 50,
    "minimumStock": 5
  }'
```

**Listar Produtos:**
```bash
curl "http://localhost:8080/api/v1/products?page=0&size=10&brand=Dell"
```

**Buscar por ID:**
```bash
curl http://localhost:8080/api/v1/products/550e8400-e29b-41d4-a716-446655440000
```

## Modelo de Dados

### Product

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | UUID | Identificador único |
| code | String(60) | Código do produto (único) |
| description | String(200) | Descrição |
| unit | String(6) | Unidade (UN, KG, M, etc.) |
| ncmCode | String(10) | Código NCM fiscal |
| eanCode | String(14) | Código EAN/GTIN |
| type | Enum | PRODUCT, SERVICE, RAW_MATERIAL, CONSUMABLE, ASSET |
| status | Enum | ACTIVE, INACTIVE, DISCONTINUED |
| brand | String(100) | Marca |
| model | String(100) | Modelo |
| family | String(100) | Família/Categoria |
| unitPrice | Decimal(19,4) | Preço de venda |
| costPrice | Decimal(19,4) | Preço de custo |
| grossWeight | Decimal(15,4) | Peso bruto (kg) |
| netWeight | Decimal(15,4) | Peso líquido (kg) |
| height/width/depth | Decimal(10,2) | Dimensões (cm) |
| stockQuantity | Integer | Quantidade em estoque |
| minimumStock | Integer | Estoque mínimo |

## Estrutura do Projeto

```
erp-cloud/
├── src/
│   ├── main/
│   │   ├── java/com/erp/
│   │   │   ├── domain/product/        # Entidade Product e Value Objects
│   │   │   ├── application/
│   │   │   │   ├── port/in/           # Use Cases (interfaces)
│   │   │   │   ├── port/out/          # Repository (interfaces)
│   │   │   │   └── service/           # Implementações
│   │   │   ├── adapters/
│   │   │   │   ├── in/rest/           # Controllers e DTOs
│   │   │   │   └── out/persistence/   # Implementação do repositório
│   │   │   ├── infrastructure/
│   │   │   │   ├── config/            # Configurações Spring
│   │   │   │   └── persistence/       # JPA Entities e Repositories
│   │   │   └── shared/                # Classes base e exceções
│   │   └── resources/
│   │       ├── application.yaml       # Configurações
│   │       └── db/changelog/          # Migrations Liquibase
│   └── test/java/                     # Testes unitários
├── docs/
│   └── SPECIFICATIONS.md              # Documentação técnica
├── pom.xml                            # Dependências Maven
└── README.md                          # Este arquivo
```

## Configuração

### application.yaml

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/erp_cloud
    username: erp_user
    password: erp_password

  jpa:
    hibernate:
      ddl-auto: validate

  liquibase:
    change-log: classpath:db/changelog/db.changelog-master.yaml
```

### Variáveis de Ambiente

| Variável | Descrição | Default |
|----------|-----------|---------|
| `SPRING_DATASOURCE_URL` | URL do banco | jdbc:postgresql://localhost:5432/erp_cloud |
| `SPRING_DATASOURCE_USERNAME` | Usuário | erp_user |
| `SPRING_DATASOURCE_PASSWORD` | Senha | erp_password |
| `SERVER_PORT` | Porta HTTP | 8080 |

## Testes

```bash
# Executar todos os testes
mvn test

# Executar teste específico
mvn test -Dtest=ProductTest

# Executar com cobertura
mvn test jacoco:report
```

## Build e Deploy

### Build

```bash
# Compilar JAR
mvn clean package -DskipTests

# O JAR será gerado em:
# target/erp-cloud-1.0.0-SNAPSHOT.jar
```

### Executar JAR

```bash
java -jar target/erp-cloud-1.0.0-SNAPSHOT.jar
```

### Docker (opcional)

```dockerfile
FROM eclipse-temurin:25-jdk
COPY target/erp-cloud-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```bash
docker build -t erp-cloud .
docker run -p 8080:8080 erp-cloud
```

## Desenvolvimento

### Adicionar Novo Domínio

1. Criar entidade em `domain/`
2. Criar Value Objects necessários
3. Criar portas em `application/port/`
4. Implementar services em `application/service/`
5. Criar adaptadores em `adapters/`
6. Adicionar migration Liquibase
7. Criar testes

### Convenções

- **Commits**: Conventional Commits (feat:, fix:, docs:, etc.)
- **Branches**: feature/, bugfix/, hotfix/
- **Código**: Clean Code, SOLID
- **Testes**: TDD quando possível

## Documentação Adicional

- [Especificações Técnicas](docs/SPECIFICATIONS.md)
- [Swagger UI](http://localhost:8080/swagger-ui.html) (quando a aplicação estiver rodando)

## Roadmap

- [ ] Autenticação JWT com Spring Security
- [ ] Cache com Redis
- [ ] Mensageria com RabbitMQ
- [ ] Módulo de Clientes
- [ ] Módulo de Pedidos
- [ ] Módulo de Notas Fiscais
- [ ] Integração com Omie API
- [ ] Dashboard com métricas

## Licença

Projeto proprietário - Todos os direitos reservados.

## Contato

Para dúvidas ou sugestões, entre em contato com a equipe de desenvolvimento.
