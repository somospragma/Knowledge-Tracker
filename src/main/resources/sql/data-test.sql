-- Sample Data for Pragma Knowledge Tracker
-- This file populates the database with realistic test data
-- Table names match schema-dev.sql (PostgreSQL compatible)

-- =====================================================
-- Chapters (Organizational Units)
-- =====================================================
INSERT INTO "chapter" ("id", "name") VALUES
(1, 'Backend Development'),
(2, 'Frontend Development'),
(3, 'DevOps & Infrastructure'),
(4, 'Data Engineering'),
(5, 'Mobile Development'),
(6, 'QA & Testing');

-- =====================================================
-- Accounts (Client Organizations)
-- =====================================================
INSERT INTO "account" ("id", "name", "region", "status", "attributes") VALUES
(1, 'Bancolombia', 'Latin America', 'Active', '{"industry": "Banking", "size": "Enterprise"}'),
(2, 'Rappi', 'Latin America', 'Active', '{"industry": "E-commerce", "size": "Large"}'),
(3, 'Falabella', 'Latin America', 'Active', '{"industry": "Retail", "size": "Enterprise"}'),
(4, 'Telefónica', 'Latin America', 'Active', '{"industry": "Telecommunications", "size": "Enterprise"}'),
(5, 'Gobierno Digital', 'Colombia', 'Active', '{"industry": "Government", "size": "Public Sector"}'),
(6, 'Avianca', 'Latin America', 'Inactive', '{"industry": "Aviation", "size": "Large"}');

-- =====================================================
-- Pragmatics (Pragma SA Employees)
-- =====================================================
INSERT INTO "pragmatic" ("id", "chapter_id", "email", "first_name", "last_name", "status") VALUES
-- Backend chapter
(1, 1, 'juan.perez@pragma.com.co', 'Juan', 'Pérez', 'Active'),
(2, 1, 'maria.garcia@pragma.com.co', 'María', 'García', 'Active'),
(3, 1, 'carlos.rodriguez@pragma.com.co', 'Carlos', 'Rodríguez', 'Active'),
(4, 1, 'ana.martinez@pragma.com.co', 'Ana', 'Martínez', 'OnLeave'),

-- Frontend chapter
(5, 2, 'luis.lopez@pragma.com.co', 'Luis', 'López', 'Active'),
(6, 2, 'sofia.gonzalez@pragma.com.co', 'Sofía', 'González', 'Active'),
(7, 2, 'diego.hernandez@pragma.com.co', 'Diego', 'Hernández', 'Active'),

-- DevOps chapter
(8, 3, 'laura.diaz@pragma.com.co', 'Laura', 'Díaz', 'Active'),
(9, 3, 'jorge.sanchez@pragma.com.co', 'Jorge', 'Sánchez', 'Active'),

-- Data Engineering chapter
(10, 4, 'paula.ramirez@pragma.com.co', 'Paula', 'Ramírez', 'Active'),
(11, 4, 'andres.torres@pragma.com.co', 'Andrés', 'Torres', 'Active'),

-- Mobile chapter
(12, 5, 'camila.flores@pragma.com.co', 'Camila', 'Flores', 'Active'),
(13, 5, 'santiago.castro@pragma.com.co', 'Santiago', 'Castro', 'Active'),

-- QA chapter
(14, 6, 'natalia.moreno@pragma.com.co', 'Natalia', 'Moreno', 'Active'),
(15, 6, 'felipe.vargas@pragma.com.co', 'Felipe', 'Vargas', 'Inactive');

-- =====================================================
-- Projects
-- =====================================================
INSERT INTO "project" ("id", "account_id", "name", "status", "start_date", "end_date", "type", "attributes") VALUES
-- Bancolombia Projects
(1, 1, 'Bancolombia - Core Banking Modernization', 'Active', '2024-01-15 00:00:00', NULL, 'Abierto', '{"budget": "high", "priority": "critical"}'),
(2, 1, 'Bancolombia - Mobile Banking App', 'Active', '2024-03-01 00:00:00', NULL, 'Abierto', '{"budget": "medium", "priority": "high"}'),

-- Rappi Projects
(3, 2, 'Rappi - Microservices Platform', 'Active', '2023-11-20 00:00:00', NULL, 'Abierto', '{"budget": "high", "priority": "high"}'),
(4, 2, 'Rappi - Analytics Dashboard', 'Completed', '2023-08-10 00:00:00', '2024-02-28 00:00:00', 'Cerrado', '{"budget": "medium", "priority": "medium"}'),

-- Falabella Projects
(5, 3, 'Falabella - E-commerce Platform Upgrade', 'Active', '2024-02-01 00:00:00', NULL, 'Abierto', '{"budget": "high", "priority": "high"}'),

-- Telefónica Projects
(6, 4, 'Telefónica - Customer Portal', 'Active', '2024-04-01 00:00:00', '2024-12-31 00:00:00', 'Cerrado', '{"budget": "medium", "priority": "medium"}'),

-- Government Projects
(7, 5, 'GOV.CO - Citizen Services Platform', 'Active', '2023-09-01 00:00:00', NULL, 'Abierto', '{"budget": "high", "priority": "critical"}'),

-- Inactive Account Project
(8, 6, 'Avianca - Legacy System Migration', 'Inactive', '2023-05-01 00:00:00', '2023-10-15 00:00:00', 'Cerrado', '{"budget": "medium", "priority": "low"}');

-- =====================================================
-- Knowledge Categories
-- =====================================================
INSERT INTO "knowledge_category" ("id", "name") VALUES
(1, 'Platform'),
(2, 'Language'),
(3, 'Framework'),
(4, 'Tool'),
(5, 'Technique');

-- =====================================================
-- Knowledge Items
-- =====================================================
INSERT INTO "knowledge" ("id", "category_id", "name", "description", "approved_status", "attributes") VALUES
-- Platforms
(1, 1, 'AWS', 'Amazon Web Services cloud platform', 'Approved', '{"vendor": "Amazon", "type": "Cloud"}'),
(2, 1, 'Azure', 'Microsoft Azure cloud platform', 'Approved', '{"vendor": "Microsoft", "type": "Cloud"}'),
(3, 1, 'Google Cloud Platform', 'Google Cloud infrastructure', 'Approved', '{"vendor": "Google", "type": "Cloud"}'),
(4, 1, 'Kubernetes', 'Container orchestration platform', 'Approved', '{"type": "Container"}'),
(5, 1, 'Docker', 'Containerization platform', 'Approved', '{"type": "Container"}'),

-- Languages
(6, 2, 'Java', 'Object-oriented programming language', 'Approved', '{"paradigm": "OOP", "type": "Backend"}'),
(7, 2, 'Python', 'Multi-paradigm programming language', 'Approved', '{"paradigm": "Multi", "type": "Backend"}'),
(8, 2, 'JavaScript', 'Dynamic scripting language', 'Approved', '{"paradigm": "Multi", "type": "Full Stack"}'),
(9, 2, 'TypeScript', 'Typed superset of JavaScript', 'Approved', '{"paradigm": "OOP", "type": "Full Stack"}'),
(10, 2, 'Kotlin', 'Modern JVM language', 'Approved', '{"paradigm": "Multi", "type": "Backend/Mobile"}'),
(11, 2, 'Go', 'Systems programming language', 'Approved', '{"paradigm": "Concurrent", "type": "Backend"}'),
(12, 2, 'Swift', 'iOS development language', 'Approved', '{"paradigm": "Multi", "type": "Mobile"}'),

-- Frameworks
(13, 3, 'Spring Boot', 'Java application framework', 'Approved', '{"language": "Java", "type": "Backend"}'),
(14, 3, 'React', 'JavaScript UI library', 'Approved', '{"language": "JavaScript", "type": "Frontend"}'),
(15, 3, 'Angular', 'TypeScript web framework', 'Approved', '{"language": "TypeScript", "type": "Frontend"}'),
(16, 3, 'Vue.js', 'Progressive JavaScript framework', 'Approved', '{"language": "JavaScript", "type": "Frontend"}'),
(17, 3, 'Django', 'Python web framework', 'Approved', '{"language": "Python", "type": "Backend"}'),
(18, 3, 'FastAPI', 'Modern Python API framework', 'Approved', '{"language": "Python", "type": "Backend"}'),
(19, 3, 'Node.js', 'JavaScript runtime environment', 'Approved', '{"language": "JavaScript", "type": "Backend"}'),
(20, 3, 'React Native', 'Mobile app framework', 'Approved', '{"language": "JavaScript", "type": "Mobile"}'),

-- Tools
(21, 4, 'PostgreSQL', 'Relational database system', 'Approved', '{"type": "Database"}'),
(22, 4, 'MongoDB', 'NoSQL document database', 'Approved', '{"type": "Database"}'),
(23, 4, 'Redis', 'In-memory data store', 'Approved', '{"type": "Cache"}'),
(24, 4, 'Kafka', 'Event streaming platform', 'Approved', '{"type": "Messaging"}'),
(25, 4, 'Git', 'Version control system', 'Approved', '{"type": "VCS"}'),
(26, 4, 'Jenkins', 'Automation server', 'Approved', '{"type": "CI/CD"}'),
(27, 4, 'GitLab CI', 'Continuous integration tool', 'Approved', '{"type": "CI/CD"}'),
(28, 4, 'Terraform', 'Infrastructure as code tool', 'Approved', '{"type": "IaC"}'),
(29, 4, 'Grafana', 'Observability platform', 'Approved', '{"type": "Monitoring"}'),
(30, 4, 'Elasticsearch', 'Search and analytics engine', 'Approved', '{"type": "Search"}'),

-- Techniques
(31, 5, 'Microservices Architecture', 'Architectural style for building distributed systems', 'Approved', '{"category": "Architecture"}'),
(32, 5, 'Domain-Driven Design', 'Software design approach', 'Approved', '{"category": "Design"}'),
(33, 5, 'Test-Driven Development', 'Software development process', 'Approved', '{"category": "Development"}'),
(34, 5, 'Continuous Integration', 'Development practice', 'Approved', '{"category": "DevOps"}'),
(35, 5, 'Hexagonal Architecture', 'Ports and adapters pattern', 'Approved', '{"category": "Architecture"}'),
(36, 5, 'Event-Driven Architecture', 'Architectural pattern', 'Approved', '{"category": "Architecture"}'),
(37, 5, 'RESTful API Design', 'API design principles', 'Approved', '{"category": "Design"}'),
(38, 5, 'Agile Methodology', 'Project management approach', 'Approved', '{"category": "Process"}'),
(39, 5, 'CQRS Pattern', 'Command Query Responsibility Segregation', 'Approved', '{"category": "Architecture"}'),
(40, 5, 'OAuth 2.0', 'Authorization framework', 'Approved', '{"category": "Security"}');

-- =====================================================
-- Knowledge Levels
-- =====================================================
INSERT INTO "knowledge_level" ("id", "name", "attributes") VALUES
(1, 'Beginner', '{"description": "Basic understanding, can perform simple tasks with guidance"}'),
(2, 'Intermediate', '{"description": "Solid understanding, can work independently on most tasks"}'),
(3, 'Advanced', '{"description": "Deep expertise, can handle complex scenarios and mentor others"}'),
(4, 'Expert', '{"description": "Mastery level, recognized authority, can architect solutions"}');

-- =====================================================
-- Applied Knowledge (Knowledge applied to Projects by Pragmatics)
-- =====================================================
INSERT INTO "applied_knowledge" ("id", "project_id", "pragmatic_id", "knowledge_id", "start_date", "knowledge_level") VALUES
-- Project 1: Bancolombia - Core Banking Modernization
(1, 1, 1, 6, '2024-01-15', 4),   -- Juan: Java - Expert
(2, 1, 1, 13, '2024-01-15', 4),  -- Juan: Spring Boot - Expert
(3, 1, 1, 21, '2024-01-15', 3),  -- Juan: PostgreSQL - Advanced
(4, 1, 1, 32, '2024-01-15', 3),  -- Juan: DDD - Advanced
(5, 1, 2, 6, '2024-01-15', 3),   -- María: Java - Advanced
(6, 1, 2, 13, '2024-01-15', 3),  -- María: Spring Boot - Advanced
(7, 1, 2, 31, '2024-01-15', 3),  -- María: Microservices - Advanced
(8, 1, 8, 4, '2024-01-15', 3),   -- Laura: Kubernetes - Advanced
(9, 1, 8, 1, '2024-01-15', 3),   -- Laura: AWS - Advanced

-- Project 2: Bancolombia - Mobile Banking App
(10, 2, 12, 12, '2024-03-01', 3), -- Camila: Swift - Advanced
(11, 2, 12, 20, '2024-03-01', 2), -- Camila: React Native - Intermediate
(12, 2, 13, 10, '2024-03-01', 3), -- Santiago: Kotlin - Advanced
(13, 2, 5, 14, '2024-03-01', 3),  -- Luis: React - Advanced
(14, 2, 14, 33, '2024-03-01', 2), -- Natalia: TDD - Intermediate

-- Project 3: Rappi - Microservices Platform
(15, 3, 3, 6, '2023-11-20', 3),   -- Carlos: Java - Advanced
(16, 3, 3, 13, '2023-11-20', 4),  -- Carlos: Spring Boot - Expert
(17, 3, 3, 31, '2023-11-20', 4),  -- Carlos: Microservices - Expert
(18, 3, 3, 24, '2023-11-20', 3),  -- Carlos: Kafka - Advanced
(19, 3, 9, 5, '2023-11-20', 4),   -- Jorge: Docker - Expert
(20, 3, 9, 4, '2023-11-20', 3),   -- Jorge: Kubernetes - Advanced
(21, 3, 9, 27, '2023-11-20', 3),  -- Jorge: GitLab CI - Advanced

-- Project 4: Rappi - Analytics Dashboard (Completed)
(22, 4, 10, 7, '2023-08-10', 3),  -- Paula: Python - Advanced
(23, 4, 10, 17, '2023-08-10', 3), -- Paula: Django - Advanced
(24, 4, 10, 30, '2023-08-10', 2), -- Paula: Elasticsearch - Intermediate
(25, 4, 6, 14, '2023-08-10', 4),  -- Sofía: React - Expert
(26, 4, 6, 9, '2023-08-10', 3),   -- Sofía: TypeScript - Advanced

-- Project 5: Falabella - E-commerce Platform
(27, 5, 1, 6, '2024-02-01', 4),   -- Juan: Java - Expert
(28, 5, 1, 13, '2024-02-01', 4),  -- Juan: Spring Boot - Expert
(29, 5, 2, 22, '2024-02-01', 3),  -- María: MongoDB - Advanced
(30, 5, 2, 23, '2024-02-01', 3),  -- María: Redis - Advanced
(31, 5, 5, 14, '2024-02-01', 3),  -- Luis: React - Advanced
(32, 5, 7, 15, '2024-02-01', 3),  -- Diego: Angular - Advanced
(33, 5, 9, 28, '2024-02-01', 3),  -- Jorge: Terraform - Advanced

-- Project 6: Telefónica - Customer Portal
(34, 6, 11, 7, '2024-04-01', 3),  -- Andrés: Python - Advanced
(35, 6, 11, 18, '2024-04-01', 4), -- Andrés: FastAPI - Expert
(36, 6, 11, 21, '2024-04-01', 3), -- Andrés: PostgreSQL - Advanced
(37, 6, 6, 16, '2024-04-01', 3),  -- Sofía: Vue.js - Advanced
(38, 6, 6, 9, '2024-04-01', 3),   -- Sofía: TypeScript - Advanced

-- Project 7: Government - Citizen Services Platform
(39, 7, 3, 6, '2023-09-01', 3),   -- Carlos: Java - Advanced
(40, 7, 3, 13, '2023-09-01', 4),  -- Carlos: Spring Boot - Expert
(41, 7, 3, 35, '2023-09-01', 3),  -- Carlos: Hexagonal Architecture - Advanced
(42, 7, 5, 14, '2023-09-01', 3),  -- Luis: React - Advanced
(43, 7, 8, 2, '2023-09-01', 3),   -- Laura: Azure - Advanced
(44, 7, 8, 26, '2023-09-01', 2),  -- Laura: Jenkins - Intermediate
(45, 7, 14, 33, '2023-09-01', 3), -- Natalia: TDD - Advanced
(46, 7, 14, 38, '2023-09-01', 2); -- Natalia: Agile - Intermediate