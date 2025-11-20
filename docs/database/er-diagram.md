# Knowledge Tracker - Entity Relationship Diagram

This diagram was auto-generated from `src/main/resources/sql/Data_Model.sql`

## ER Diagram

```mermaid
erDiagram

    territory ||--o{ account : "has"
    KC-Team ||--o{ Chapter : "has"
    Knowledge_Category ||--o{ knowledge : "has"
    account ||--o{ Project : "has"
    Chapter ||--o{ Pragmatic : "has"
    Project ||--o{ Applied_Knowledge : "has"
    Pragmatic ||--o{ Applied_Knowledge : "has"
    knowledge ||--o{ Applied_Knowledge : "has"
    Knowledge_Level ||--o{ Applied_Knowledge : "has"

    territory {
        BIGINT id "PK"
        VARCHAR name "UK"
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }

    KC-Team {
        BIGINT id "PK"
        VARCHAR name "UK"
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }

    Knowledge_Category {
        BIGINT id "PK"
        VARCHAR name "UK"
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }

    Knowledge_Level {
        BIGINT id "PK"
        VARCHAR name
        JSONB attributes
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }

    User {
        UUID id "PK"
        VARCHAR email "UK"
        VARCHAR first_name
        VARCHAR last_name
        VARCHAR system_role
        BOOLEAN active
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }

    account {
        BIGINT id "PK"
        BIGINT territory_id "FK"
        VARCHAR name
        VARCHAR status
        JSONB attributes
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }

    Chapter {
        BIGINT id "PK"
        BIGINT kc_id "FK"
        VARCHAR name
        VARCHAR status
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }

    knowledge {
        BIGINT id "PK"
        BIGINT category_id "FK"
        VARCHAR name
        VARCHAR description
        VARCHAR approved_status
        JSONB attributes
    }

    Project {
        BIGINT id "PK"
        BIGINT account_id "FK"
        VARCHAR name
        VARCHAR status
        DATE start_date
        DATE end_date
        VARCHAR type
        JSONB attributes
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }

    Pragmatic {
        BIGINT id "PK"
        BIGINT chapter_id "FK"
        VARCHAR email "UK"
        VARCHAR first_name
        VARCHAR last_name
        VARCHAR status
        TIMESTAMP created_at
        TIMESTAMP updated_at
        JSONB attributes
    }

    Applied_Knowledge {
        BIGINT id "PK"
        BIGINT project_id "FK"
        BIGINT pragmatic_id "FK"
        BIGINT knowledge_id "FK"
        DATE onboard_date
        DATE offboard_date
        BIGINT knowledge_level
        JSONB attributes
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }

```

## Tables Overview

### territory
- **Columns**: 4
- **Foreign Keys**: 0

### KC-Team
- **Columns**: 4
- **Foreign Keys**: 0

### Knowledge_Category
- **Columns**: 4
- **Foreign Keys**: 0

### Knowledge_Level
- **Columns**: 5
- **Foreign Keys**: 0

### User
- **Columns**: 8
- **Foreign Keys**: 0

### account
- **Columns**: 7
- **Foreign Keys**: 1
- **References**:
  - `territory_id` → `territory`

### Chapter
- **Columns**: 6
- **Foreign Keys**: 1
- **References**:
  - `kc_id` → `KC-Team`

### knowledge
- **Columns**: 6
- **Foreign Keys**: 1
- **References**:
  - `category_id` → `Knowledge_Category`

### Project
- **Columns**: 10
- **Foreign Keys**: 1
- **References**:
  - `account_id` → `account`

### Pragmatic
- **Columns**: 9
- **Foreign Keys**: 1
- **References**:
  - `chapter_id` → `Chapter`

### Applied_Knowledge
- **Columns**: 10
- **Foreign Keys**: 4
- **References**:
  - `project_id` → `Project`
  - `pragmatic_id` → `Pragmatic`
  - `knowledge_id` → `knowledge`
  - `knowledge_level` → `Knowledge_Level`
