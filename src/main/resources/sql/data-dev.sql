-- =====================================================
-- Sample Data for Pragma knowledge Tracker
-- Updated to match Data-Model.sql structure
-- =====================================================

-- =====================================================
-- region (Base Table)
-- =====================================================
INSERT INTO "territory" (id, name, created_at, updated_at) VALUES
(1, 'Colombia', CURRENT_TIMESTAMP, NULL),
(2, 'México', CURRENT_TIMESTAMP, NULL),
(3, 'Argentina', CURRENT_TIMESTAMP, NULL),
(4, 'Chile', CURRENT_TIMESTAMP, NULL),
(5, 'Perú', CURRENT_TIMESTAMP, NULL),
(6, 'Brasil', CURRENT_TIMESTAMP, NULL);

-- =====================================================
-- kc-team (knowledge Center Teams)
-- =====================================================
INSERT INTO "kc-team" (id, name, created_at, updated_at) VALUES
(1, 'KC Ciencias de la Computación', CURRENT_TIMESTAMP, NULL),
(2, 'KC People-Centered Design', CURRENT_TIMESTAMP, NULL),
(3, 'KC Powerful Teams', CURRENT_TIMESTAMP, NULL),
(4, 'KC Business Development', CURRENT_TIMESTAMP, NULL),
(5, 'KC Data Science', CURRENT_TIMESTAMP, NULL),
(6, 'Unasigned KC', CURRENT_TIMESTAMP, NULL);

-- =====================================================
-- chapter (Organizational Units within kc-teams)
-- =====================================================
INSERT INTO "chapter" (id, kc_id, name, status, created_at, updated_at) VALUES
-- Architecture & Backend kc-team
(1, 1, 'Architecture', 'Active', CURRENT_TIMESTAMP, NULL),
(2, 1, 'Backend', 'Active', CURRENT_TIMESTAMP, NULL),
(3, 1, 'Frontend', 'Active', CURRENT_TIMESTAMP, NULL),

-- Frontend & UX kc-team
(4, 2, 'UX', 'Active', CURRENT_TIMESTAMP, NULL),
(5, 2, 'Business Consulting', 'Active', CURRENT_TIMESTAMP, NULL),
(6, 2, 'Research', 'Active', CURRENT_TIMESTAMP, NULL),

-- Cloud & DevOps kc-team
(7, 3, 'AWS Infrastructure', 'Active', CURRENT_TIMESTAMP, NULL),
(8, 3, 'Azure Infrastructure', 'Active', CURRENT_TIMESTAMP, NULL),
(9, 3, 'DevOps Automation', 'Active', CURRENT_TIMESTAMP, NULL),

-- Data & Analytics kc-team
(10, 4, 'Data Engineering', 'Active', CURRENT_TIMESTAMP, NULL),
(11, 4, 'Analytics & BI', 'Active', CURRENT_TIMESTAMP, NULL),

-- Mobile Development kc-team
(12, 5, 'iOS Development', 'Active', CURRENT_TIMESTAMP, NULL),
(13, 5, 'Android Development', 'Active', CURRENT_TIMESTAMP, NULL),
(14, 5, 'Cross-Platform Mobile', 'Active', CURRENT_TIMESTAMP, NULL),

-- Quality & Testing kc-team
(15, 6, 'QA Automation', 'Active', CURRENT_TIMESTAMP, NULL),
(16, 6, 'Manual Testing', 'Active', CURRENT_TIMESTAMP, NULL);

-- =====================================================
-- account (Client Organizations)
-- =====================================================
INSERT INTO "account" (id, territory_id, name, status, attributes, created_at, updated_at) VALUES
(1, 1, 'Bancolombia', 'Active', '{"industry": "Banking", "size": "Enterprise", "employees": "25000+"}', CURRENT_TIMESTAMP, NULL),
(2, 1, 'Rappi', 'Active', '{"industry": "E-commerce", "size": "Large", "employees": "5000+"}', CURRENT_TIMESTAMP, NULL),
(3, 2, 'Banco Azteca', 'Active', '{"industry": "Banking", "size": "Enterprise", "employees": "15000+"}', CURRENT_TIMESTAMP, NULL),
(4, 3, 'Mercado Libre', 'Active', '{"industry": "E-commerce", "size": "Enterprise", "employees": "20000+"}', CURRENT_TIMESTAMP, NULL),
(5, 4, 'Falabella', 'Active', '{"industry": "Retail", "size": "Enterprise", "employees": "100000+"}', CURRENT_TIMESTAMP, NULL),
(6, 1, 'Gobierno Digital Colombia', 'Active', '{"industry": "Government", "size": "Public Sector"}', CURRENT_TIMESTAMP, NULL),
(7, 2, 'CFE México', 'Active', '{"industry": "Energy", "size": "Enterprise", "employees": "80000+"}', CURRENT_TIMESTAMP, NULL),
(8, 1, 'Avianca', 'Inactive', '{"industry": "Aviation", "size": "Large", "employees": "14000+"}', CURRENT_TIMESTAMP, NULL);

-- =====================================================
-- pragmatic (Pragma SA Employees)
-- =====================================================
INSERT INTO "pragmatic" (id, chapter_id, email, first_name, last_name, status, created_at, updated_at, attributes) VALUES
-- Java Backend chapter
(1, 1, 'juan.perez@pragma.com.co', 'Juan', 'Pérez', 'Active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Senior", "years_experience": 8}'),
(2, 1, 'maria.garcia@pragma.com.co', 'María', 'García', 'Active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Senior", "years_experience": 7}'),
(3, 1, 'carlos.rodriguez@pragma.com.co', 'Carlos', 'Rodríguez', 'Active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Expert", "years_experience": 10}'),
(4, 1, 'ana.martinez@pragma.com.co', 'Ana', 'Martínez', 'OnLeave', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Mid", "years_experience": 4}'),

-- Python Backend chapter
(5, 2, 'paula.ramirez@pragma.com.co', 'Paula', 'Ramírez', 'Active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Senior", "years_experience": 6}'),
(6, 2, 'andres.torres@pragma.com.co', 'Andrés', 'Torres', 'Active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Expert", "years_experience": 9}'),

-- NodeJS Backend chapter
(7, 3, 'sebastian.vega@pragma.com.co', 'Sebastián', 'Vega', 'Active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Senior", "years_experience": 5}'),

-- React Development chapter
(8, 4, 'luis.lopez@pragma.com.co', 'Luis', 'López', 'Active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Senior", "years_experience": 6}'),
(9, 4, 'sofia.gonzalez@pragma.com.co', 'Sofía', 'González', 'Active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Expert", "years_experience": 8}'),

-- Angular Development chapter
(10, 5, 'diego.hernandez@pragma.com.co', 'Diego', 'Hernández', 'Active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Mid", "years_experience": 4}'),

-- Vue.js Development chapter
(11, 6, 'valentina.ruiz@pragma.com.co', 'Valentina', 'Ruiz', 'Active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Senior", "years_experience": 5}'),

-- AWS Infrastructure chapter
(12, 7, 'laura.diaz@pragma.com.co', 'Laura', 'Díaz', 'Active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Senior", "years_experience": 7}'),
(13, 7, 'jorge.sanchez@pragma.com.co', 'Jorge', 'Sánchez', 'Active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Expert", "years_experience": 10}'),

-- Azure Infrastructure chapter
(14, 8, 'carolina.mora@pragma.com.co', 'Carolina', 'Mora', 'Active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Mid", "years_experience": 3}'),

-- DevOps Automation chapter
(15, 9, 'miguel.castro@pragma.com.co', 'Miguel', 'Castro', 'Active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Senior", "years_experience": 6}'),

-- Data Engineering chapter
(16, 10, 'daniela.fernandez@pragma.com.co', 'Daniela', 'Fernández', 'Active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Senior", "years_experience": 5}'),

-- iOS Development chapter
(17, 12, 'camila.flores@pragma.com.co', 'Camila', 'Flores', 'Active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Senior", "years_experience": 6}'),

-- Android Development chapter
(18, 13, 'santiago.parra@pragma.com.co', 'Santiago', 'Parra', 'Active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Senior", "years_experience": 5}'),

-- Cross-Platform Mobile chapter
(19, 14, 'isabella.reyes@pragma.com.co', 'Isabella', 'Reyes', 'Active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Mid", "years_experience": 4}'),

-- QA Automation chapter
(20, 15, 'natalia.moreno@pragma.com.co', 'Natalia', 'Moreno', 'Active', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Senior", "years_experience": 7}'),
(21, 15, 'felipe.vargas@pragma.com.co', 'Felipe', 'Vargas', 'Inactive', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{"seniority": "Mid", "years_experience": 3}');

-- =====================================================
-- project
-- =====================================================
INSERT INTO "project" (id, account_id, name, status, start_date, end_date, type, attributes, created_at, updated_at) VALUES
-- Bancolombia projects
(1, 1, 'Bancolombia - Core Banking Modernization', 'Active', '2024-01-15', NULL, 'Abierto', '{"budget": "high", "priority": "critical", "team_size": 12}', CURRENT_TIMESTAMP, NULL),
(2, 1, 'Bancolombia - Mobile Banking App v2', 'Active', '2024-03-01', NULL, 'Abierto', '{"budget": "medium", "priority": "high", "team_size": 8}', CURRENT_TIMESTAMP, NULL),
(3, 1, 'Bancolombia - Analytics Platform', 'Completed', '2023-06-01', '2024-01-31', 'Cerrado', '{"budget": "medium", "priority": "medium", "team_size": 6}', '2023-06-01 00:00:00', CURRENT_TIMESTAMP),

-- Rappi projects
(4, 2, 'Rappi - Microservices Platform', 'Active', '2023-11-20', NULL, 'Abierto', '{"budget": "high", "priority": "high", "team_size": 15}', CURRENT_TIMESTAMP, NULL),
(5, 2, 'Rappi - Real-time Analytics Dashboard', 'Completed', '2023-08-10', '2024-02-28', 'Cerrado', '{"budget": "medium", "priority": "medium", "team_size": 5}', '2023-08-10 00:00:00', CURRENT_TIMESTAMP),

-- Banco Azteca projects
(6, 3, 'Banco Azteca - Digital Transformation', 'Active', '2024-02-15', NULL, 'Abierto', '{"budget": "high", "priority": "critical", "team_size": 10}', CURRENT_TIMESTAMP, NULL),

-- Mercado Libre projects
(7, 4, 'Mercado Libre - Payment Gateway Enhancement', 'Active', '2024-01-10', NULL, 'Abierto', '{"budget": "high", "priority": "high", "team_size": 8}', CURRENT_TIMESTAMP, NULL),

-- Falabella projects
(8, 5, 'Falabella - E-commerce Platform Upgrade', 'Active', '2024-02-01', '2024-12-31', 'Cerrado', '{"budget": "high", "priority": "high", "team_size": 12}', CURRENT_TIMESTAMP, NULL),

-- Government projects
(9, 6, 'GOV.CO - Citizen Services Platform', 'Active', '2023-09-01', NULL, 'Abierto', '{"budget": "high", "priority": "critical", "team_size": 14}', CURRENT_TIMESTAMP, NULL),

-- CFE México projects
(10, 7, 'CFE - Smart Grid Infrastructure', 'Active', '2024-03-15', NULL, 'Abierto', '{"budget": "high", "priority": "high", "team_size": 9}', CURRENT_TIMESTAMP, NULL),

-- Inactive account project
(11, 8, 'Avianca - Legacy System Migration', 'Inactive', '2023-05-01', '2023-10-15', 'Cerrado', '{"budget": "medium", "priority": "low", "team_size": 6}', '2023-05-01 00:00:00', '2023-10-15 00:00:00');

-- =====================================================
-- knowledge_category
-- =====================================================
INSERT INTO "knowledge_category" (id, name, created_at, updated_at) VALUES
(1, 'Cloud Platform', CURRENT_TIMESTAMP, NULL),
(2, 'Programming Language', CURRENT_TIMESTAMP, NULL),
(3, 'Framework', CURRENT_TIMESTAMP, NULL),
(4, 'Database', CURRENT_TIMESTAMP, NULL),
(5, 'DevOps Tool', CURRENT_TIMESTAMP, NULL),
(6, 'Architecture Pattern', CURRENT_TIMESTAMP, NULL),
(7, 'Development Practice', CURRENT_TIMESTAMP, NULL),
(8, 'Mobile Technology', CURRENT_TIMESTAMP, NULL);

-- =====================================================
-- knowledge Items
-- =====================================================
INSERT INTO "knowledge" (id, category_id, name, description, approved_status, attributes) VALUES
-- Cloud Platforms (Category 1)
(1, 1, 'AWS', 'Amazon Web Services - comprehensive cloud platform', 'APPROVED', '{"vendor": "Amazon", "certification_available": true}'),
(2, 1, 'Azure', 'Microsoft Azure - enterprise cloud platform', 'APPROVED', '{"vendor": "Microsoft", "certification_available": true}'),
(3, 1, 'Google Cloud Platform', 'GCP - Googles cloud infrastructure', 'APPROVED', '{"vendor": "Google", "certification_available": true}'),
(4, 1, 'Kubernetes', 'Container orchestration platform', 'APPROVED', '{"type": "Container Orchestration", "cncf": true}'),
(5, 1, 'Docker', 'Containerization platform', 'APPROVED', '{"type": "Containerization"}'),

-- Programming Languages (Category 2)
(6, 2, 'Java', 'Enterprise-grade object-oriented programming', 'APPROVED', '{"paradigm": "OOP", "version": "21", "use_case": "Backend"}'),
(7, 2, 'Python', 'Versatile multi-paradigm language', 'APPROVED', '{"paradigm": "Multi", "version": "3.12", "use_case": "Backend/Data"}'),
(8, 2, 'JavaScript', 'Dynamic scripting language for web', 'APPROVED', '{"paradigm": "Multi", "version": "ES2023", "use_case": "Full Stack"}'),
(9, 2, 'TypeScript', 'Typed superset of JavaScript', 'APPROVED', '{"paradigm": "OOP", "version": "5.x", "use_case": "Full Stack"}'),
(10, 2, 'Kotlin', 'Modern JVM language', 'APPROVED', '{"paradigm": "Multi", "use_case": "Backend/Mobile"}'),
(11, 2, 'Go', 'Systems and cloud-native programming', 'APPROVED', '{"paradigm": "Concurrent", "use_case": "Backend/Infrastructure"}'),
(12, 2, 'Swift', 'iOS native development', 'APPROVED', '{"paradigm": "Multi", "use_case": "iOS"}'),

-- Frameworks (Category 3)
(13, 3, 'Spring Boot', 'Java application framework', 'APPROVED', '{"language": "Java", "version": "3.x", "type": "Backend"}'),
(14, 3, 'React', 'Component-based UI library', 'APPROVED', '{"language": "JavaScript", "version": "18.x", "type": "Frontend"}'),
(15, 3, 'Angular', 'Full-featured web framework', 'APPROVED', '{"language": "TypeScript", "version": "17.x", "type": "Frontend"}'),
(16, 3, 'Vue.js', 'Progressive JavaScript framework', 'APPROVED', '{"language": "JavaScript", "version": "3.x", "type": "Frontend"}'),
(17, 3, 'Django', 'Python web framework with batteries included', 'APPROVED', '{"language": "Python", "version": "5.x", "type": "Backend"}'),
(18, 3, 'FastAPI', 'Modern high-performance Python API framework', 'APPROVED', '{"language": "Python", "version": "0.1x", "type": "Backend"}'),
(19, 3, 'Node.js', 'JavaScript runtime for backend', 'APPROVED', '{"language": "JavaScript", "version": "20.x", "type": "Backend"}'),
(20, 3, 'React Native', 'Cross-platform mobile framework', 'APPROVED', '{"language": "JavaScript", "version": "0.73", "type": "Mobile"}'),
(21, 3, 'Flutter', 'Googles UI toolkit for mobile', 'APPROVED', '{"language": "Dart", "version": "3.x", "type": "Mobile"}'),

-- Databases (Category 4)
(22, 4, 'PostgreSQL', 'Advanced open-source relational database', 'APPROVED', '{"type": "RDBMS", "version": "16.x"}'),
(23, 4, 'MongoDB', 'Document-oriented NoSQL database', 'APPROVED', '{"type": "NoSQL", "subtype": "Document"}'),
(24, 4, 'Redis', 'In-memory data structure store', 'APPROVED', '{"type": "Cache/NoSQL", "use_case": "Caching/Messaging"}'),
(25, 4, 'MySQL', 'Popular open-source relational database', 'APPROVED', '{"type": "RDBMS", "version": "8.x"}'),
(26, 4, 'Oracle Database', 'Enterprise relational database', 'APPROVED', '{"type": "RDBMS", "vendor": "Oracle"}'),
(27, 4, 'Elasticsearch', 'Distributed search and analytics engine', 'APPROVED', '{"type": "Search Engine", "use_case": "Search/Analytics"}'),

-- DevOps Tools (Category 5)
(28, 5, 'Jenkins', 'Open-source automation server', 'APPROVED', '{"type": "CI/CD"}'),
(29, 5, 'GitLab CI', 'Integrated CI/CD in GitLab', 'APPROVED', '{"type": "CI/CD", "integrated": true}'),
(30, 5, 'GitHub Actions', 'GitHub automation platform', 'APPROVED', '{"type": "CI/CD", "integrated": true}'),
(31, 5, 'Terraform', 'Infrastructure as Code tool', 'APPROVED', '{"type": "IaC", "vendor": "HashiCorp"}'),
(32, 5, 'Ansible', 'Configuration management tool', 'APPROVED', '{"type": "Configuration Management"}'),
(33, 5, 'Grafana', 'Observability and monitoring platform', 'APPROVED', '{"type": "Monitoring"}'),
(34, 5, 'Prometheus', 'Systems monitoring and alerting', 'APPROVED', '{"type": "Monitoring", "cncf": true}'),
(35, 5, 'SonarQube', 'Code quality and security analysis', 'APPROVED', '{"type": "Code Quality"}'),

-- Architecture Patterns (Category 6)
(36, 6, 'Microservices Architecture', 'Distributed systems architectural style', 'APPROVED', '{"complexity": "High", "scalability": "High"}'),
(37, 6, 'Domain-Driven Design', 'Strategic and tactical design patterns', 'APPROVED', '{"complexity": "High", "focus": "Domain Logic"}'),
(38, 6, 'Hexagonal Architecture', 'Ports and adapters pattern', 'APPROVED', '{"aka": "Clean Architecture", "complexity": "Medium"}'),
(39, 6, 'Event-Driven Architecture', 'Asynchronous event-based pattern', 'APPROVED', '{"complexity": "High", "scalability": "High"}'),
(40, 6, 'CQRS Pattern', 'Command Query Responsibility Segregation', 'APPROVED', '{"complexity": "Medium", "use_case": "Complex domains"}'),
(41, 6, 'Serverless Architecture', 'Function-as-a-Service pattern', 'APPROVED', '{"complexity": "Low", "cloud_native": true}'),

-- Development Practices (Category 7)
(42, 7, 'Test-Driven Development', 'TDD methodology', 'APPROVED', '{"type": "Development Practice"}'),
(43, 7, 'Continuous Integration', 'CI practice', 'APPROVED', '{"type": "DevOps Practice"}'),
(44, 7, 'Continuous Deployment', 'CD practice', 'APPROVED', '{"type": "DevOps Practice"}'),
(45, 7, 'RESTful API Design', 'REST architectural principles', 'APPROVED', '{"type": "API Design"}'),
(46, 7, 'GraphQL', 'Query language for APIs', 'APPROVED', '{"type": "API Design", "alternative_to": "REST"}'),
(47, 7, 'Agile Methodology', 'Iterative project management', 'APPROVED', '{"type": "Process"}'),
(48, 7, 'Scrum Framework', 'Agile framework for teams', 'APPROVED', '{"type": "Process", "based_on": "Agile"}'),

-- Mobile Technologies (Category 8)
(49, 8, 'SwiftUI', 'Declarative UI framework for iOS', 'APPROVED', '{"platform": "iOS", "language": "Swift"}'),
(50, 8, 'Jetpack Compose', 'Modern UI toolkit for Android', 'APPROVED', '{"platform": "Android", "language": "Kotlin"}');

-- =====================================================
-- knowledge_level
-- =====================================================
INSERT INTO "knowledge_level" (id, name, attributes, created_at, updated_at) VALUES
(1, 'Beginner', '{"description": "Basic understanding, can perform simple tasks with guidance", "years_experience": "0-1"}', CURRENT_TIMESTAMP, NULL),
(2, 'Intermediate', '{"description": "Solid understanding, can work independently on most tasks", "years_experience": "1-3"}', CURRENT_TIMESTAMP, NULL),
(3, 'Advanced', '{"description": "Deep expertise, can handle complex scenarios and mentor others", "years_experience": "3-5"}', CURRENT_TIMESTAMP, NULL),
(4, 'Expert', '{"description": "Mastery level, recognized authority, can architect solutions", "years_experience": "5+"}', CURRENT_TIMESTAMP, NULL);

-- =====================================================
-- applied_knowledge (Assignments)
-- Note: knowledge_level is now a FK to knowledge_level table
-- =====================================================
INSERT INTO "applied_knowledge" (id, project_id, pragmatic_id, knowledge_id, onboard_date, offboard_date, knowledge_level, attributes, created_at, updated_at) VALUES
-- project 1: Bancolombia - Core Banking Modernization
(1, 1, 1, 6, '2024-01-15', NULL, 4, '{"role": "Tech Lead", "allocation": 100}', CURRENT_TIMESTAMP, NULL),   -- Juan: Java - Expert
(2, 1, 1, 13, '2024-01-15', NULL, 4, '{"role": "Tech Lead", "allocation": 100}', CURRENT_TIMESTAMP, NULL),  -- Juan: Spring Boot - Expert
(3, 1, 1, 22, '2024-01-15', NULL, 3, '{"role": "Tech Lead", "allocation": 100}', CURRENT_TIMESTAMP, NULL),  -- Juan: PostgreSQL - Advanced
(4, 1, 1, 37, '2024-01-15', NULL, 3, '{"role": "Tech Lead", "allocation": 100}', CURRENT_TIMESTAMP, NULL),  -- Juan: DDD - Advanced
(5, 1, 2, 6, '2024-01-15', NULL, 3, '{"role": "Senior Developer", "allocation": 100}', CURRENT_TIMESTAMP, NULL),   -- María: Java - Advanced
(6, 1, 2, 13, '2024-01-15', NULL, 3, '{"role": "Senior Developer", "allocation": 100}', CURRENT_TIMESTAMP, NULL),  -- María: Spring Boot - Advanced
(7, 1, 2, 36, '2024-01-15', NULL, 3, '{"role": "Senior Developer", "allocation": 100}', CURRENT_TIMESTAMP, NULL),  -- María: Microservices - Advanced
(8, 1, 12, 4, '2024-01-15', NULL, 3, '{"role": "DevOps Engineer", "allocation": 100}', CURRENT_TIMESTAMP, NULL),   -- Laura: Kubernetes - Advanced
(9, 1, 12, 1, '2024-01-15', NULL, 3, '{"role": "DevOps Engineer", "allocation": 100}', CURRENT_TIMESTAMP, NULL),   -- Laura: AWS - Advanced

-- project 2: Bancolombia - Mobile Banking App
(10, 2, 17, 12, '2024-03-01', NULL, 3, '{"role": "iOS Developer", "allocation": 100}', CURRENT_TIMESTAMP, NULL), -- Camila: Swift - Advanced
(11, 2, 17, 49, '2024-03-01', NULL, 3, '{"role": "iOS Developer", "allocation": 100}', CURRENT_TIMESTAMP, NULL), -- Camila: SwiftUI - Advanced
(12, 2, 18, 10, '2024-03-01', NULL, 3, '{"role": "Android Developer", "allocation": 100}', CURRENT_TIMESTAMP, NULL), -- Santiago: Kotlin - Advanced
(13, 2, 18, 50, '2024-03-01', NULL, 2, '{"role": "Android Developer", "allocation": 100}', CURRENT_TIMESTAMP, NULL), -- Santiago: Jetpack Compose - Intermediate
(14, 2, 8, 14, '2024-03-01', NULL, 3, '{"role": "Frontend Developer", "allocation": 50}', CURRENT_TIMESTAMP, NULL),  -- Luis: React - Advanced
(15, 2, 20, 42, '2024-03-01', NULL, 3, '{"role": "QA Lead", "allocation": 100}', CURRENT_TIMESTAMP, NULL), -- Natalia: TDD - Advanced

-- project 3: Bancolombia - Analytics Platform (Completed)
(16, 3, 5, 7, '2023-06-01', '2024-01-31', 3, '{"role": "Backend Developer", "allocation": 100}', '2023-06-01 00:00:00', '2024-01-31 00:00:00'),  -- Paula: Python - Advanced
(17, 3, 5, 17, '2023-06-01', '2024-01-31', 3, '{"role": "Backend Developer", "allocation": 100}', '2023-06-01 00:00:00', '2024-01-31 00:00:00'), -- Paula: Django - Advanced
(18, 3, 9, 14, '2023-06-01', '2024-01-31', 4, '{"role": "Frontend Lead", "allocation": 100}', '2023-06-01 00:00:00', '2024-01-31 00:00:00'),  -- Sofía: React - Expert

-- project 4: Rappi - Microservices Platform
(19, 4, 3, 6, '2023-11-20', NULL, 4, '{"role": "Architect", "allocation": 100}', CURRENT_TIMESTAMP, NULL),   -- Carlos: Java - Expert
(20, 4, 3, 13, '2023-11-20', NULL, 4, '{"role": "Architect", "allocation": 100}', CURRENT_TIMESTAMP, NULL),  -- Carlos: Spring Boot - Expert
(21, 4, 3, 36, '2023-11-20', NULL, 4, '{"role": "Architect", "allocation": 100}', CURRENT_TIMESTAMP, NULL),  -- Carlos: Microservices - Expert
(22, 4, 13, 5, '2023-11-20', NULL, 4, '{"role": "DevOps Lead", "allocation": 100}', CURRENT_TIMESTAMP, NULL),   -- Jorge: Docker - Expert
(23, 4, 13, 4, '2023-11-20', NULL, 3, '{"role": "DevOps Lead", "allocation": 100}', CURRENT_TIMESTAMP, NULL),   -- Jorge: Kubernetes - Advanced
(24, 4, 13, 29, '2023-11-20', NULL, 3, '{"role": "DevOps Lead", "allocation": 100}', CURRENT_TIMESTAMP, NULL),  -- Jorge: GitLab CI - Advanced

-- project 5: Rappi - Analytics Dashboard (Completed)
(25, 5, 16, 7, '2023-08-10', '2024-02-28', 3, '{"role": "Data Engineer", "allocation": 100}', '2023-08-10 00:00:00', '2024-02-28 00:00:00'),  -- Daniela: Python - Advanced
(26, 5, 16, 27, '2023-08-10', '2024-02-28', 2, '{"role": "Data Engineer", "allocation": 100}', '2023-08-10 00:00:00', '2024-02-28 00:00:00'), -- Daniela: Elasticsearch - Intermediate
(27, 5, 9, 14, '2023-08-10', '2024-02-28', 4, '{"role": "Frontend Architect", "allocation": 80}', '2023-08-10 00:00:00', '2024-02-28 00:00:00'),  -- Sofía: React - Expert
(28, 5, 9, 9, '2023-08-10', '2024-02-28', 3, '{"role": "Frontend Architect", "allocation": 80}', '2023-08-10 00:00:00', '2024-02-28 00:00:00'),   -- Sofía: TypeScript - Advanced

-- project 6: Banco Azteca - Digital Transformation
(29, 6, 2, 6, '2024-02-15', NULL, 3, '{"role": "Senior Developer", "allocation": 100}', CURRENT_TIMESTAMP, NULL),   -- María: Java - Advanced
(30, 6, 2, 13, '2024-02-15', NULL, 3, '{"role": "Senior Developer", "allocation": 100}', CURRENT_TIMESTAMP, NULL),  -- María: Spring Boot - Advanced
(31, 6, 10, 15, '2024-02-15', NULL, 2, '{"role": "Frontend Developer", "allocation": 100}', CURRENT_TIMESTAMP, NULL),  -- Diego: Angular - Intermediate
(32, 6, 14, 2, '2024-02-15', NULL, 2, '{"role": "Cloud Engineer", "allocation": 50}', CURRENT_TIMESTAMP, NULL),  -- Carolina: Azure - Intermediate

-- project 7: Mercado Libre - Payment Gateway
(33, 7, 6, 7, '2024-01-10', NULL, 4, '{"role": "Backend Architect", "allocation": 100}', CURRENT_TIMESTAMP, NULL),  -- Andrés: Python - Expert
(34, 7, 6, 18, '2024-01-10', NULL, 4, '{"role": "Backend Architect", "allocation": 100}', CURRENT_TIMESTAMP, NULL), -- Andrés: FastAPI - Expert
(35, 7, 6, 22, '2024-01-10', NULL, 3, '{"role": "Backend Architect", "allocation": 100}', CURRENT_TIMESTAMP, NULL), -- Andrés: PostgreSQL - Advanced
(36, 7, 11, 16, '2024-01-10', NULL, 3, '{"role": "Frontend Developer", "allocation": 100}', CURRENT_TIMESTAMP, NULL),  -- Valentina: Vue.js - Advanced

-- project 8: Falabella - E-commerce Platform
(37, 8, 1, 6, '2024-02-01', NULL, 4, '{"role": "Tech Lead", "allocation": 100}', CURRENT_TIMESTAMP, NULL),   -- Juan: Java - Expert
(38, 8, 1, 13, '2024-02-01', NULL, 4, '{"role": "Tech Lead", "allocation": 100}', CURRENT_TIMESTAMP, NULL),  -- Juan: Spring Boot - Expert
(39, 8, 2, 23, '2024-02-01', NULL, 3, '{"role": "Senior Developer", "allocation": 50}', CURRENT_TIMESTAMP, NULL),  -- María: MongoDB - Advanced
(40, 8, 2, 24, '2024-02-01', NULL, 3, '{"role": "Senior Developer", "allocation": 50}', CURRENT_TIMESTAMP, NULL),  -- María: Redis - Advanced
(41, 8, 8, 14, '2024-02-01', NULL, 3, '{"role": "Frontend Developer", "allocation": 100}', CURRENT_TIMESTAMP, NULL),  -- Luis: React - Advanced
(42, 8, 13, 31, '2024-02-01', NULL, 3, '{"role": "Infrastructure Engineer", "allocation": 50}', CURRENT_TIMESTAMP, NULL),  -- Jorge: Terraform - Advanced

-- project 9: Government - Citizen Services Platform
(43, 9, 3, 6, '2023-09-01', NULL, 4, '{"role": "Lead Architect", "allocation": 100}', CURRENT_TIMESTAMP, NULL),   -- Carlos: Java - Expert
(44, 9, 3, 13, '2023-09-01', NULL, 4, '{"role": "Lead Architect", "allocation": 100}', CURRENT_TIMESTAMP, NULL),  -- Carlos: Spring Boot - Expert
(45, 9, 3, 38, '2023-09-01', NULL, 3, '{"role": "Lead Architect", "allocation": 100}', CURRENT_TIMESTAMP, NULL),  -- Carlos: Hexagonal Architecture - Advanced
(46, 9, 8, 14, '2023-09-01', NULL, 3, '{"role": "Frontend Lead", "allocation": 100}', CURRENT_TIMESTAMP, NULL),  -- Luis: React - Advanced
(47, 9, 12, 2, '2023-09-01', NULL, 3, '{"role": "Cloud Architect", "allocation": 100}', CURRENT_TIMESTAMP, NULL),   -- Laura: Azure - Advanced
(48, 9, 15, 28, '2023-09-01', NULL, 2, '{"role": "DevOps Engineer", "allocation": 100}', CURRENT_TIMESTAMP, NULL),  -- Miguel: Jenkins - Intermediate
(49, 9, 20, 42, '2023-09-01', NULL, 3, '{"role": "QA Lead", "allocation": 100}', CURRENT_TIMESTAMP, NULL), -- Natalia: TDD - Advanced
(50, 9, 20, 47, '2023-09-01', NULL, 2, '{"role": "QA Lead", "allocation": 100}', CURRENT_TIMESTAMP, NULL); -- Natalia: Agile - Intermediate

-- =====================================================
-- Reset sequences to match inserted data
-- =====================================================
SELECT setval('"territory_id_seq"', (SELECT MAX(id) FROM "territory"));
SELECT setval('"kc-team_id_seq"', (SELECT MAX(id) FROM "kc-team"));
SELECT setval('"chapter_id_seq"', (SELECT MAX(id) FROM "chapter"));
SELECT setval('"account_id_seq"', (SELECT MAX(id) FROM "account"));
SELECT setval('"pragmatic_id_seq"', (SELECT MAX(id) FROM "pragmatic"));
SELECT setval('"project_id_seq"', (SELECT MAX(id) FROM "project"));
SELECT setval('"knowledge_category_id_seq"', (SELECT MAX(id) FROM "knowledge_category"));
SELECT setval('"knowledge_id_seq"', (SELECT MAX(id) FROM "knowledge"));
SELECT setval('"knowledge_level_id_seq"', (SELECT MAX(id) FROM "knowledge_level"));
SELECT setval('"applied_knowledge_id_seq"', (SELECT MAX(id) FROM "applied_knowledge"));

