-- H2 PostgreSQL Compatible Schema
-- Updated from Vigilancia_V2.sql

CREATE TABLE IF NOT EXISTS "Account" (
    "id" INT NOT NULL AUTO_INCREMENT,
    "name" VARCHAR(255) NOT NULL,
    "region" VARCHAR(255),
    "status" VARCHAR(255),
    "attributes" VARCHAR(5000),
    PRIMARY KEY("id")
);

CREATE TABLE IF NOT EXISTS "Chapter" (
    "id" INT NOT NULL AUTO_INCREMENT,
    "name" VARCHAR(255) NOT NULL,
    PRIMARY KEY("id")
);

CREATE TABLE IF NOT EXISTS "Pragmatic" (
    "id" INT NOT NULL AUTO_INCREMENT,
    "chapter_id" INT,
    "email" VARCHAR(255) NOT NULL UNIQUE,
    "first_name" VARCHAR(100),
    "last_name" VARCHAR(100),
    "status" VARCHAR(50) DEFAULT 'Active',
    PRIMARY KEY("id"),
    FOREIGN KEY("chapter_id") REFERENCES "Chapter"("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS "Project" (
    "id" INT NOT NULL AUTO_INCREMENT,
    "account_id" INT NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "status" VARCHAR(255) NOT NULL DEFAULT 'Active',
    "startDate" TIMESTAMP,
    "endDate" TIMESTAMP,
    "type" VARCHAR(255) DEFAULT 'Abierto',
    "attributes" VARCHAR(5000),
    PRIMARY KEY("id"),
    FOREIGN KEY("account_id") REFERENCES "Account"("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON COLUMN "Project"."type" IS 'Abierto, Cerrado, N/A';

CREATE TABLE IF NOT EXISTS "Knowledge_Category" (
    "id" INT NOT NULL AUTO_INCREMENT,
    "name" VARCHAR(50),
    PRIMARY KEY("id")
);

CREATE TABLE IF NOT EXISTS "knowledge" (
    "id" INT NOT NULL AUTO_INCREMENT,
    "category_id" INT NOT NULL,
    "name" VARCHAR(255) NOT NULL DEFAULT '150',
    "description" VARCHAR(500),
    "approved_status" VARCHAR(255),
    "attributes" VARCHAR(5000),
    PRIMARY KEY("id"),
    FOREIGN KEY("category_id") REFERENCES "Knowledge_Category"("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS "Knowledge_Level" (
    "id" INT NOT NULL AUTO_INCREMENT,
    "name" VARCHAR(255) NOT NULL,
    "attributes" VARCHAR(5000),
    PRIMARY KEY("id")
);

CREATE TABLE IF NOT EXISTS "Applied_Knowledge" (
    "id" INT NOT NULL AUTO_INCREMENT,
    "project_id" INT NOT NULL,
    "pragmatic_id" INT NOT NULL,
    "knowledge_id" INT,
    "startDate" TIMESTAMP,
    "knowledge_level" INT,
    PRIMARY KEY("id"),
    FOREIGN KEY("project_id") REFERENCES "Project"("id") ON UPDATE NO ACTION ON DELETE NO ACTION,
    FOREIGN KEY("pragmatic_id") REFERENCES "Pragmatic"("id") ON UPDATE NO ACTION ON DELETE NO ACTION,
    FOREIGN KEY("knowledge_id") REFERENCES "knowledge"("id") ON UPDATE NO ACTION ON DELETE NO ACTION,
    FOREIGN KEY("knowledge_level") REFERENCES "Knowledge_Level"("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);