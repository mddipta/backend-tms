CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE tb_roles (
    id text DEFAULT uuid_generate_v4() NOT NULL,
    code VARCHAR(10) NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_by TEXT NOT NULL DEFAULT 'SYSTEM',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_by TEXT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMPTZ NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version INT NOT NULL DEFAULT 0,
    deleted_at TIMESTAMPTZ NULL
);

ALTER TABLE tb_roles ADD CONSTRAINT role_pk PRIMARY KEY (id);
ALTER TABLE tb_roles ADD CONSTRAINT role_bk UNIQUE(code);

CREATE TABLE tb_ticket_statuses(
    id text DEFAULT uuid_generate_v4() NOT NULL,
    name VARCHAR(50) NOT NULL,
    code VARCHAR(10) NOT NULL,
    created_by TEXT NOT NULL DEFAULT 'SYSTEM',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_by TEXT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMPTZ NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version INT NOT NULL DEFAULT 0,
    deleted_at TIMESTAMPTZ NULL
);

ALTER TABLE tb_ticket_statuses ADD CONSTRAINT ticket_status_pk PRIMARY KEY(id);
ALTER TABLE tb_ticket_statuses ADD CONSTRAINT ticket_status_bk UNIQUE(code);



CREATE TABLE tb_priority_ticket_statuses(
    id text DEFAULT uuid_generate_v4() NOT NULL,
    name VARCHAR(20) NOT NULL,
    code VARCHAR(10) NOT NULL,
    created_by TEXT NOT NULL DEFAULT 'SYSTEM',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_by TEXT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMPTZ NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version INT NOT NULL DEFAULT 0,
    deleted_at TIMESTAMPTZ NULL
);

ALTER TABLE tb_priority_ticket_statuses ADD CONSTRAINT priority_ticket_status_pk PRIMARY KEY(id);
ALTER TABLE tb_priority_ticket_statuses ADD CONSTRAINT priority_ticket_status_bk UNIQUE(code);

CREATE TABLE tb_companies(
    id text DEFAULT uuid_generate_v4() NOT NULL,
    name VARCHAR(100) NOT NULL,
--    code VARCHAR(10) NOT NULL,
    address TEXT NOT NULL,
    created_by TEXT NOT NULL DEFAULT 'SYSTEM',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_by TEXT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMPTZ NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version INT NOT NULL DEFAULT 0,
    deleted_at TIMESTAMPTZ NULL
);

ALTER TABLE tb_companies ADD CONSTRAINT company_pk PRIMARY KEY(id);
--ALTER TABLE tb_companies ADD CONSTRAINT company_bk UNIQUE (code);

CREATE TABLE tb_users (
    id text DEFAULT uuid_generate_v4() NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role_id text NOT NULL,
    created_by TEXT NOT NULL DEFAULT 'SYSTEM',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_by TEXT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMPTZ NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version INT NOT NULL DEFAULT 0,
    deleted_at TIMESTAMPTZ NULL
);

ALTER TABLE tb_users ADD CONSTRAINT user_pk PRIMARY KEY(id);
ALTER TABLE tb_users ADD CONSTRAINT user_username_bk UNIQUE(username);
ALTER TABLE tb_users ADD CONSTRAINT user_email_bk UNIQUE(email);
ALTER TABLE tb_users ADD CONSTRAINT user_role_fk FOREIGN KEY (role_id) REFERENCES tb_roles;

CREATE TABLE tb_customers (
    id text DEFAULT uuid_generate_v4() NOT NULL,
    company_id text NOT NULL,
    user_id text NOT NULL,
    pic_id text NOT NULL,
    created_by TEXT NOT NULL DEFAULT 'SYSTEM',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_by TEXT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMPTZ NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version INT NOT NULL DEFAULT 0,
    deleted_at TIMESTAMPTZ NULL
);

ALTER TABLE tb_customers ADD CONSTRAINT customer_pk PRIMARY KEY(id);
ALTER TABLE tb_customers ADD CONSTRAINT customer_company_fk FOREIGN KEY(company_id) REFERENCES tb_companies;
ALTER TABLE tb_customers ADD CONSTRAINT customer_user_fk FOREIGN KEY(user_id) REFERENCES tb_users;
ALTER TABLE tb_customers ADD CONSTRAINT customer_customer_fk FOREIGN KEY(pic_id) REFERENCES tb_users;
ALTER TABLE tb_customers ADD CONSTRAINT customer_company_bk UNIQUE (company_id);
ALTER TABLE tb_customers ADD CONSTRAINT customer_user_bk UNIQUE (user_id);

--CREATE TABLE tb_pic(
--    id text DEFAULT uuid_generate_v4() NOT NULL,
--    user_id text NOT NULL,
--    customer_id text NOT NULL,
--    created_by TEXT NOT NULL DEFAULT 'SYSTEM',
--    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
--    updated_by TEXT NULL DEFAULT 'SYSTEM',
--    updated_at TIMESTAMPTZ NULL,
--    is_active BOOLEAN NOT NULL DEFAULT TRUE,
--    version INT NOT NULL DEFAULT 0,
--    deleted_at TIMESTAMPTZ NULL
--);
--
--ALTER TABLE tb_pic ADD CONSTRAINT pic_pk PRIMARY KEY(id);
--ALTER TABLE tb_pic ADD CONSTRAINT pic_user_fk FOREIGN KEY(user_id) REFERENCES tb_users;
--ALTER TABLE tb_pic ADD CONSTRAINT pic_customer_fk FOREIGN KEY(customer_id) REFERENCES tb_customers;
--ALTER TABLE tb_pic ADD CONSTRAINT pic_customer_bk UNIQUE (customer_id);

CREATE TABLE tb_tickets(
    id text DEFAULT uuid_generate_v4() NOT NULL,
    code VARCHAR(10) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date DATE,
    date_ticket DATE DEFAULT NOW(),
    priority_ticket_status_id TEXT NOT NULL,
    user_id text NULL,
    customer_id text NOT NULL,
    created_by TEXT NOT NULL DEFAULT 'SYSTEM',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_by TEXT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMPTZ NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version INT NOT NULL DEFAULT 0,
    deleted_at TIMESTAMPTZ NULL
);

ALTER TABLE tb_tickets ADD CONSTRAINT ticket_pk PRIMARY KEY(id);
ALTER TABLE tb_tickets ADD CONSTRAINT ticket_code_bk UNIQUE(code);
ALTER TABLE tb_tickets ADD CONSTRAINT ticket_priority_status_fk FOREIGN KEY(priority_ticket_status_id) REFERENCES tb_priority_ticket_statuses;
ALTER TABLE tb_tickets ADD CONSTRAINT ticket_user_fk FOREIGN KEY(user_id) REFERENCES tb_users;
ALTER TABLE tb_tickets ADD CONSTRAINT ticket_customer_fk FOREIGN KEY(customer_id) REFERENCES tb_customers;
ALTER TABLE tb_tickets ALTER COLUMN user_id DROP NOT NULL;

CREATE TABLE tb_tickets_trx(
    id text DEFAULT uuid_generate_v4() NOT NULL,
    number text NOT NULL,
    ticket_id text NOT NULL,
    user_id text NOT NULL,
    status_ticket_id text NOT NULL,
    created_by TEXT NOT NULL DEFAULT 'SYSTEM',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_by TEXT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMPTZ NULL,
    version INT NOT NULL DEFAULT 0,
    deleted_at TIMESTAMPTZ NULL
);  

ALTER TABLE tb_tickets_trx ADD CONSTRAINT ticket_trx_pk PRIMARY KEY(id);
ALTER TABLE tb_tickets_trx ADD CONSTRAINT ticket_trx_ticket_fk FOREIGN KEY(ticket_id) REFERENCES tb_tickets;
ALTER TABLE tb_tickets_trx ADD CONSTRAINT ticket_trx_user FOREIGN KEY(user_id) REFERENCES tb_users;
ALTER TABLE tb_tickets_trx ADD CONSTRAINT ticket_trx_status_ticket_fk FOREIGN KEY(status_ticket_id) REFERENCES tb_ticket_statuses;


CREATE TABLE tb_ticket_attachments(
    id text DEFAULT uuid_generate_v4() NOT NULL,
    file TEXT,
    ticket_id TEXT NOT NULL,
    created_by TEXT NOT NULL DEFAULT 'SYSTEM',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_by TEXT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMPTZ NULL,
    version INT NOT NULL DEFAULT 0,
    deleted_at TIMESTAMPTZ NULL
);
ALTER TABLE tb_ticket_attachments ADD CONSTRAINT ticket_attachments_pk PRIMARY KEY(id);
ALTER TABLE tb_ticket_attachments ADD CONSTRAINT tickket_attachments_ticket_fk FOREIGN KEY(ticket_id) REFERENCES tb_tickets;

CREATE TABLE tb_ticket_comments(
    id text DEFAULT uuid_generate_v4() NOT NULL,
    comment TEXT NOT NULL,
    ticket_id text NOT NULL,
    user_id text NOT NULL,
    created_by TEXT NOT NULL DEFAULT 'SYSTEM',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_by TEXT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMPTZ NULL,
    version INT NOT NULL DEFAULT 0,
    deleted_at TIMESTAMPTZ NULL
);

ALTER TABLE tb_ticket_comments ADD CONSTRAINT ticket_comment_pk PRIMARY KEY(id);
ALTER TABLE tb_ticket_comments ADD CONSTRAINT ticket_comment_ticket_fk FOREIGN KEY(ticket_id) REFERENCES tb_tickets;
ALTER TABLE tb_ticket_comments ADD CONSTRAINT ticket_comment_user_fk FOREIGN KEY(user_id) REFERENCES tb_users;

CREATE TABLE tb_ticket_comment_attachments(
    id text DEFAULT uuid_generate_v4() NOT NULL,
    file TEXT NOT NULL,
    ticket_comment_id text NOT NULL,
    created_by TEXT NOT NULL DEFAULT 'SYSTEM',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_by TEXT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMPTZ NULL,
    version INT NOT NULL DEFAULT 0,
    deleted_at TIMESTAMPTZ NULL
);

ALTER TABLE tb_ticket_comment_attachments ADD CONSTRAINT ticket_comment_attachment_pk PRIMARY KEY(id);
ALTER TABLE tb_ticket_comment_attachments ADD CONSTRAINT ticket_comment_attachment_comment_fk FOREIGN KEY(ticket_comment_id) REFERENCES tb_ticket_comments ;

INSERT INTO tb_roles (code, name) VALUES
('SPA', 'Super Admin'),
('PIC', 'PIC'),
('DEV', 'Developer'),
('CUS', 'Customer');

INSERT INTO tb_ticket_statuses (code, name) VALUES 
('OP', 'Open'),
('PA', 'Pending Agent'),
('OG', 'On Progress'),
('PC', 'Pending Customer'),
('CL', 'Closed'),
('RA', 'Re-Open');

INSERT INTO tb_priority_ticket_statuses (name, code) VALUES 
('H', 'High'),
('M', 'Medium'),
('L', 'Low');

INSERT INTO tb_companies(name, address) VALUES 
('PT. Makmur Abadi', 'Jalan Kebahagiaan'),
('PT. Sumber Jaya Abadi', 'Jalan  Terbaik');

INSERT INTO tb_users(username, password, name, email, role_id) VALUES 
('superadmin', 'superadminsystem', 'Super Admin', 'supadm@test.com', 'a4459cd2-d8b0-4ed8-b5d1-ffcbba6835a9'),
('pic', 'piccus', 'Andi PIC', 'andi@test.com', '54ab1735-2dac-4353-a440-8ae32813ba6b'),
('dev1', 'developerstress', 'Developer', 'dev@test.com', '2e4bafb1-e29f-40da-881d-18d55c4f3feb'),
('cusmakmur', 'cusmakmur', 'Budi', 'budi@test.com', '83e39c50-925e-4b62-bf9c-3d23345aae30'),
('cusabadi', 'cusabadi', 'Jaya', 'jaya@test.com', '83e39c50-925e-4b62-bf9c-3d23345aae30');
--
--
INSERT INTO tb_customers(company_id, user_id) VALUES 
('a6360946-3954-454b-8df6-acf3d3711f11', '7b293e46-5fce-4548-b784-5b924e176b4c'),
('8b22f96d-c5ce-4a7e-8254-ec66a97a3a97', 'f9d00f8e-0609-4a75-b6a1-0e67f997d59d');
--
INSERT INTO tb_pic(user_id, customer_id) VALUES 
('05154c83-e707-4c61-bbdc-4942656ef6ff', '19a03842-0723-4359-9ef7-7c952ecb7b81');
--
--INSERT INTO tb_tickets(code, title, description, due_date, priority_ticket_status_id, user_id, pic_id) VALUES 
--('#T-001', 'Bug pada Keranjang', 'Bug perhitungan harga setiap barang', '2025-01-01', 3 ,3, 1),
--('#T-002', 'Bug implementasi voucher free ongkir', 'Habis klik apply loading terus', '2024-12-05', 2 ,3, 1);
--
----INSERT INTO tb_tickets_trx (ticket_id, user_id, pic_id, status_ticket_id) VALUES
----(1, 3, 1, 1);
----(1, 3, 1, 1);
--
--
--INSERT INTO tb_ticket_attachments(file, ticket_id) VALUES ('image.png', 1);
--
--INSERT INTO tb_ticket_comments(comment, ticket_id, user_id) VALUES 
--('Lah enak banget nyuruh orang', 1, 3);
--
--INSERT INTO tb_ticket_comment_attachments(file, ticket_comment_id) VALUES 
--('image.jpg', 1);
--
--SELECT * FROM tb_tickets;