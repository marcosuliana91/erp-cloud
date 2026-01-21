# ERP Cloud - Especificações Técnicas

## 1. Visão Geral

**Nome do Projeto:** ERP Cloud Backend
**Versão:** 1.0.0-SNAPSHOT
**Data:** Janeiro 2026

Sistema ERP backend desenvolvido com arquitetura hexagonal (Ports and Adapters), focado em escalabilidade, manutenibilidade e separação de responsabilidades.

---

## 2. Stack Tecnológica

| Componente | Tecnologia | Versão |
|------------|------------|--------|
| Linguagem | Java | 25 (LTS) |
| Framework | Spring Boot | 4.0.0 |
| Persistência | Spring Data JPA | 4.0.0 |
| ORM | Hibernate | 7.x |
| Banco de Dados | PostgreSQL | 16.x |
| Migrations | Liquibase | 5.x |
| Build | Maven | 3.x |
| API Docs | SpringDoc OpenAPI | 2.8.0 |

---

## 3. Arquitetura

### 3.1 Hexagonal Architecture (Ports and Adapters)

```
┌─────────────────────────────────────────────────────────────────┐
│                        ADAPTERS (IN)                            │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  REST Controllers  │  DTOs  │  Mappers                  │   │
│  └─────────────────────────────────────────────────────────┘   │
└───────────────────────────────┬─────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                     APPLICATION LAYER                           │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  Use Cases (Services)  │  Input Ports  │  Output Ports  │   │
│  └─────────────────────────────────────────────────────────┘   │
└───────────────────────────────┬─────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                       DOMAIN LAYER                              │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  Entities  │  Value Objects  │  Domain Services         │   │
│  │  (Pure Java - No Framework Dependencies)                │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      ADAPTERS (OUT)                             │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  Persistence Adapters  │  JPA Entities  │  Repositories │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
```

### 3.2 Estrutura de Pacotes

```
com.erp/
├── domain/                          # Camada de Domínio (Pure Java)
│   └── product/
│       ├── Product.java             # Aggregate Root
│       ├── ProductId.java           # Value Object
│       ├── ProductCode.java         # Value Object
│       ├── NcmCode.java             # Value Object
│       ├── EanCode.java             # Value Object
│       ├── Money.java               # Value Object
│       ├── Weight.java              # Value Object
│       ├── Dimensions.java          # Value Object
│       ├── UnitOfMeasure.java       # Value Object
│       ├── ProductType.java         # Enum
│       └── ProductStatus.java       # Enum
│
├── application/                     # Camada de Aplicação
│   ├── port/
│   │   ├── in/                      # Input Ports (Use Cases)
│   │   │   ├── CreateProductUseCase.java
│   │   │   ├── UpdateProductUseCase.java
│   │   │   ├── GetProductUseCase.java
│   │   │   ├── ListProductsUseCase.java
│   │   │   └── DeleteProductUseCase.java
│   │   └── out/                     # Output Ports (Repositories)
│   │       └── ProductRepository.java
│   └── service/                     # Use Case Implementations
│       ├── CreateProductService.java
│       ├── UpdateProductService.java
│       ├── GetProductService.java
│       ├── ListProductsService.java
│       └── DeleteProductService.java
│
├── adapters/
│   ├── in/rest/                     # Input Adapters (REST API)
│   │   ├── controller/
│   │   │   └── ProductController.java
│   │   ├── dto/
│   │   │   ├── CreateProductRequest.java
│   │   │   ├── UpdateProductRequest.java
│   │   │   └── ProductResponse.java
│   │   └── mapper/
│   │       └── ProductDtoMapper.java
│   └── out/persistence/             # Output Adapters (Database)
│       ├── ProductPersistenceAdapter.java
│       └── mapper/
│           └── ProductPersistenceMapper.java
│
├── infrastructure/                  # Infraestrutura
│   ├── config/
│   │   ├── JpaConfig.java
│   │   ├── WebConfig.java
│   │   └── OpenApiConfig.java
│   └── persistence/
│       ├── entity/
│       │   └── ProductEntity.java
│       └── repository/
│           └── ProductJpaRepository.java
│
└── shared/                          # Componentes Compartilhados
    ├── domain/
    │   └── DomainEntity.java
    ├── application/
    │   ├── UseCase.java
    │   └── port/out/Repository.java
    ├── adapter/
    │   ├── Mapper.java
    │   ├── in/BaseController.java
    │   └── out/BaseJpaRepository.java
    ├── api/
    │   ├── ApiError.java
    │   ├── PageResponse.java
    │   └── GlobalExceptionHandler.java
    └── exception/
        ├── DomainException.java
        ├── EntityNotFoundException.java
        ├── DuplicateEntityException.java
        ├── BusinessRuleException.java
        └── ValidationException.java
```

---

## 4. Modelo de Domínio

### 4.1 Product (Aggregate Root)

A entidade `Product` representa o cadastro de produtos do sistema ERP.

#### Campos

| Campo | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| id | UUID | Sim | Identificador único |
| code | String(60) | Sim | Código do produto (único) |
| integrationCode | String(60) | Não | Código de integração externa |
| description | String(200) | Sim | Descrição do produto |
| detailedDescription | Text | Não | Descrição detalhada |
| unit | String(6) | Sim | Unidade de medida (UN, KG, etc.) |
| ncmCode | String(10) | Sim | Código NCM (fiscal) |
| eanCode | String(14) | Não | Código EAN/GTIN |
| type | Enum | Sim | Tipo do produto |
| status | Enum | Sim | Status do produto |
| brand | String(100) | Não | Marca |
| model | String(100) | Não | Modelo |
| family | String(100) | Não | Família/Categoria |
| unitPrice | Decimal(19,4) | Não | Preço de venda |
| costPrice | Decimal(19,4) | Não | Preço de custo |
| grossWeight | Decimal(15,4) | Não | Peso bruto (kg) |
| netWeight | Decimal(15,4) | Não | Peso líquido (kg) |
| height | Decimal(10,2) | Não | Altura (cm) |
| width | Decimal(10,2) | Não | Largura (cm) |
| depth | Decimal(10,2) | Não | Profundidade (cm) |
| internalNotes | Text | Não | Observações internas |
| stockQuantity | Integer | Não | Quantidade em estoque |
| minimumStock | Integer | Não | Estoque mínimo |
| createdAt | Timestamp | Sim | Data de criação |
| updatedAt | Timestamp | Sim | Data de atualização |

#### Enums

**ProductType:**
- `PRODUCT` - Produto acabado
- `SERVICE` - Serviço
- `RAW_MATERIAL` - Matéria-prima
- `CONSUMABLE` - Material de consumo
- `ASSET` - Ativo imobilizado

**ProductStatus:**
- `ACTIVE` - Ativo (pode ser vendido)
- `INACTIVE` - Inativo (temporariamente indisponível)
- `DISCONTINUED` - Descontinuado (permanentemente removido)

### 4.2 Value Objects

#### ProductId
```java
// Identificador único do produto baseado em UUID
ProductId.generate()       // Gera novo ID
ProductId.of(UUID uuid)    // Cria a partir de UUID existente
```

#### ProductCode
```java
// Código do produto (máx 60 caracteres)
ProductCode.of("PROD-001")
```

#### NcmCode
```java
// Código NCM (Nomenclatura Comum do Mercosul)
// Formatos: "XXXX.XX.XX" ou "XXXXXXXX"
NcmCode.of("8471.30.19")
NcmCode.of("84713019")
```

#### EanCode
```java
// Código EAN/GTIN (8, 12, 13 ou 14 dígitos)
EanCode.of("7891234567890")
EanCode.empty()  // Produto sem EAN
```

#### Money
```java
// Valor monetário com 4 casas decimais
Money.of(new BigDecimal("199.99"))
Money.zero()
money.add(other)
money.subtract(other)
money.multiply(quantity)
money.isZero()
```

#### Weight
```java
// Peso em quilogramas
Weight.of(new BigDecimal("1.5"))
Weight.zero()
```

#### Dimensions
```java
// Dimensões em centímetros
Dimensions.of(height, width, depth)
Dimensions.zero()
dimensions.volume()  // Calcula volume
```

#### UnitOfMeasure
```java
// Unidade de medida (máx 6 caracteres)
UnitOfMeasure.of("UN")
UnitOfMeasure.of("KG")
UnitOfMeasure.of("M")
```

### 4.3 Comportamentos do Domínio

```java
// Ativação/Desativação
product.activate()      // Ativa o produto
product.deactivate()    // Inativa o produto
product.discontinue()   // Descontinua o produto

// Preços
product.updatePrice(newPrice)       // Atualiza preço de venda
product.updateCostPrice(newCost)    // Atualiza preço de custo
product.calculateProfit()           // Calcula margem de lucro

// Estoque
product.adjustStock(quantity)       // Ajusta estoque (+/-)
product.isLowStock()               // Verifica estoque baixo
product.canBeSold()                // Verifica se pode ser vendido

// Atualizações
product.updateDetails(...)          // Atualiza descrição, marca, etc.
product.updatePhysicalAttributes(...) // Atualiza peso e dimensões
product.updateCodes(...)            // Atualiza códigos
product.updateUnit(...)             // Atualiza unidade
product.updateMinimumStock(...)     // Atualiza estoque mínimo
```

---

## 5. API REST

### 5.1 Base URL

```
http://localhost:8080/api/v1
```

### 5.2 Endpoints

#### Products

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/products` | Criar produto |
| GET | `/products` | Listar produtos (paginado) |
| GET | `/products/{id}` | Buscar produto por ID |
| PUT | `/products/{id}` | Atualizar produto |
| DELETE | `/products/{id}` | Deletar produto (logical delete) |

### 5.3 Criar Produto

**Request:**
```http
POST /api/v1/products
Content-Type: application/json

{
  "code": "PROD-001",
  "integrationCode": "EXT-001",
  "description": "Produto Exemplo",
  "detailedDescription": "Descrição detalhada do produto",
  "unit": "UN",
  "ncmCode": "8471.30.19",
  "eanCode": "7891234567890",
  "type": "PRODUCT",
  "brand": "Marca X",
  "model": "Modelo Y",
  "family": "Categoria A",
  "unitPrice": 199.99,
  "costPrice": 100.00,
  "grossWeight": 1.5,
  "netWeight": 1.2,
  "height": 10.0,
  "width": 20.0,
  "depth": 15.0,
  "internalNotes": "Observações internas",
  "stockQuantity": 100,
  "minimumStock": 10
}
```

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "code": "PROD-001",
  "integrationCode": "EXT-001",
  "description": "Produto Exemplo",
  "detailedDescription": "Descrição detalhada do produto",
  "unit": "UN",
  "ncmCode": "8471.30.19",
  "eanCode": "7891234567890",
  "type": "PRODUCT",
  "status": "ACTIVE",
  "brand": "Marca X",
  "model": "Modelo Y",
  "family": "Categoria A",
  "unitPrice": 199.99,
  "costPrice": 100.00,
  "grossWeight": 1.5,
  "netWeight": 1.2,
  "dimensions": {
    "height": 10.0,
    "width": 20.0,
    "depth": 15.0
  },
  "internalNotes": "Observações internas",
  "stockQuantity": 100,
  "minimumStock": 10,
  "lowStock": false,
  "createdAt": "2026-01-21T10:30:00",
  "updatedAt": "2026-01-21T10:30:00"
}
```

### 5.4 Listar Produtos

**Request:**
```http
GET /api/v1/products?page=0&size=20&sortBy=description&sortDirection=asc&description=exemplo&family=categoria&brand=marca
```

**Query Parameters:**

| Parâmetro | Tipo | Default | Descrição |
|-----------|------|---------|-----------|
| page | int | 0 | Número da página (0-based) |
| size | int | 20 | Tamanho da página |
| sortBy | string | description | Campo para ordenação |
| sortDirection | string | asc | Direção (asc/desc) |
| description | string | - | Filtro por descrição (contains) |
| family | string | - | Filtro por família |
| brand | string | - | Filtro por marca |

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "code": "PROD-001",
      "description": "Produto Exemplo",
      ...
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5,
  "first": true,
  "last": false
}
```

### 5.5 Buscar Produto por ID

**Request:**
```http
GET /api/v1/products/550e8400-e29b-41d4-a716-446655440000
```

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "code": "PROD-001",
  ...
}
```

**Response (404 Not Found):**
```json
{
  "timestamp": "2026-01-21T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found with id: 550e8400-e29b-41d4-a716-446655440000",
  "path": "/api/v1/products/550e8400-e29b-41d4-a716-446655440000"
}
```

### 5.6 Atualizar Produto

**Request:**
```http
PUT /api/v1/products/550e8400-e29b-41d4-a716-446655440000
Content-Type: application/json

{
  "code": "PROD-001-UPDATED",
  "description": "Produto Atualizado",
  ...
}
```

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "code": "PROD-001-UPDATED",
  "description": "Produto Atualizado",
  ...
}
```

### 5.7 Deletar Produto

**Request:**
```http
DELETE /api/v1/products/550e8400-e29b-41d4-a716-446655440000
```

**Response (204 No Content):**
```
(empty body)
```

> **Nota:** A exclusão é lógica (soft delete). O produto é marcado como `DISCONTINUED`.

---

## 6. Validações

### 6.1 Validações de Entrada (DTO)

| Campo | Validações |
|-------|------------|
| code | Obrigatório, máx 60 caracteres |
| description | Obrigatório, máx 200 caracteres |
| unit | Obrigatório, máx 6 caracteres |
| ncmCode | Obrigatório, formato XXXX.XX.XX ou 8 dígitos |
| eanCode | Máx 14 caracteres |
| brand | Máx 100 caracteres |
| model | Máx 100 caracteres |
| family | Máx 100 caracteres |
| unitPrice | >= 0, máx 15 inteiros + 4 decimais |
| costPrice | >= 0, máx 15 inteiros + 4 decimais |
| grossWeight | >= 0, máx 11 inteiros + 4 decimais |
| netWeight | >= 0, máx 11 inteiros + 4 decimais |
| height/width/depth | >= 0, máx 8 inteiros + 2 decimais |
| stockQuantity | >= 0 |
| minimumStock | >= 0 |

### 6.2 Validações de Domínio

- Código do produto único
- Descrição não pode ser vazia
- NCM deve ter formato válido
- EAN deve ter 8, 12, 13 ou 14 dígitos (quando informado)
- Preços não podem ser negativos
- Estoque não pode ser negativo
- Produto descontinuado não pode ser reativado

---

## 7. Banco de Dados

### 7.1 Tabela: products

```sql
CREATE TABLE products (
    id UUID PRIMARY KEY,
    code VARCHAR(60) NOT NULL UNIQUE,
    integration_code VARCHAR(60),
    description VARCHAR(200) NOT NULL,
    detailed_description TEXT,
    unit VARCHAR(6) NOT NULL,
    ncm_code VARCHAR(10) NOT NULL,
    ean_code VARCHAR(14),
    type VARCHAR(20) NOT NULL DEFAULT 'PRODUCT',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    brand VARCHAR(100),
    model VARCHAR(100),
    family VARCHAR(100),
    unit_price DECIMAL(19,4) DEFAULT 0,
    cost_price DECIMAL(19,4) DEFAULT 0,
    gross_weight DECIMAL(15,4) DEFAULT 0,
    net_weight DECIMAL(15,4) DEFAULT 0,
    height DECIMAL(10,2) DEFAULT 0,
    width DECIMAL(10,2) DEFAULT 0,
    depth DECIMAL(10,2) DEFAULT 0,
    internal_notes TEXT,
    stock_quantity INTEGER DEFAULT 0,
    minimum_stock INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_products_type CHECK (type IN ('PRODUCT', 'SERVICE', 'RAW_MATERIAL', 'CONSUMABLE', 'ASSET')),
    CONSTRAINT chk_products_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'DISCONTINUED')),
    CONSTRAINT chk_products_unit_price_non_negative CHECK (unit_price >= 0),
    CONSTRAINT chk_products_cost_price_non_negative CHECK (cost_price >= 0),
    CONSTRAINT chk_products_stock_non_negative CHECK (stock_quantity >= 0),
    CONSTRAINT chk_products_min_stock_non_negative CHECK (minimum_stock >= 0)
);
```

### 7.2 Índices

```sql
CREATE INDEX idx_products_code ON products(code);
CREATE INDEX idx_products_integration_code ON products(integration_code);
CREATE INDEX idx_products_description ON products(description);
CREATE INDEX idx_products_family ON products(family);
CREATE INDEX idx_products_brand ON products(brand);
CREATE INDEX idx_products_status ON products(status);
CREATE INDEX idx_products_ncm_code ON products(ncm_code);
CREATE INDEX idx_products_type ON products(type);
```

---

## 8. Configurações

### 8.1 application.yaml

```yaml
server:
  port: 8080

spring:
  application:
    name: erp-cloud

  datasource:
    url: jdbc:postgresql://localhost:5432/erp_cloud
    username: erp_user
    password: erp_password
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect

  liquibase:
    change-log: classpath:db/changelog/db.changelog-master.yaml
    enabled: true

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
```

---

## 9. Como Executar

### 9.1 Pré-requisitos

- Java 25+
- Maven 3.8+
- PostgreSQL 16+
- Docker (opcional)

### 9.2 Banco de Dados com Docker

```bash
docker run -d \
  --name erp-postgres \
  -e POSTGRES_DB=erp_cloud \
  -e POSTGRES_USER=erp_user \
  -e POSTGRES_PASSWORD=erp_password \
  -p 5432:5432 \
  postgres:16
```

### 9.3 Executar Aplicação

```bash
# Compilar
mvn clean compile

# Executar
mvn spring-boot:run

# Ou com JAR
mvn clean package -DskipTests
java -jar target/erp-cloud-1.0.0-SNAPSHOT.jar
```

### 9.4 Acessos

| Recurso | URL |
|---------|-----|
| API | http://localhost:8080/api/v1 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI Spec | http://localhost:8080/api-docs |
| Health Check | http://localhost:8080/actuator/health |

---

## 10. Testes

### 10.1 Executar Testes

```bash
# Todos os testes
mvn test

# Testes específicos
mvn test -Dtest=ProductTest
mvn test -Dtest=ValueObjectsTest
```

### 10.2 Cobertura de Testes

- Testes unitários de domínio (Product, Value Objects)
- Validações de invariantes
- Comportamentos do domínio

---

## 11. Próximos Passos

### 11.1 Funcionalidades Pendentes

- [ ] Autenticação e Autorização (Spring Security)
- [ ] Cache (Redis)
- [ ] Mensageria (RabbitMQ/Kafka)
- [ ] Auditoria de alterações
- [ ] Upload de imagens de produtos
- [ ] Integração com APIs externas (Omie, etc.)
- [ ] Relatórios

### 11.2 Novos Domínios

- [ ] Clientes
- [ ] Fornecedores
- [ ] Pedidos
- [ ] Notas Fiscais
- [ ] Financeiro
- [ ] Estoque (movimentações)

---

## 12. Referências

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Domain-Driven Design](https://domainlanguage.com/ddd/)
- [API Omie](https://app.omie.com.br/api/v1/geral/produtos/)
