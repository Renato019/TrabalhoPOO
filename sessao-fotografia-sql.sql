DROP TABLE pacotes_contratados;
DROP TABLE comparecimentos;
DROP TABLE agendamentos;
DROP TABLE clientes;
DROP TABLE fotografos;
DROP TABLE pacotes;

SELECT * FROM clientes;

-- TABELAS BASE

CREATE TABLE clientes (
   id_cliente         SERIAL,
   nome               VARCHAR(100)   NOT NULL,
   cpf                VARCHAR(11)    NOT NULL,
   telefone           VARCHAR(20)    NOT NULL,
   email              VARCHAR(100)   NOT NULL,
   data_nascimento    DATE           NOT NULL,
   CONSTRAINT pk_clientes
     PRIMARY KEY (id_cliente)
);

CREATE TABLE fotografos (
   id_fotografo       SERIAL,
   nome                VARCHAR(100)    NOT NULL,
   especialidade       VARCHAR(100)    NOT NULL,
   nivel_experiencia   VARCHAR(50)     NOT NULL,
   telefone            VARCHAR(20)     NOT NULL,
   CONSTRAINT pk_fotografos
     PRIMARY KEY (id_fotografo)
);

CREATE TABLE pacotes (
   id_pacote        SERIAL,
   nome             VARCHAR(100)   NOT NULL,
   preco            DECIMAL(7,2)   NOT NULL,
   duracao_minutos  INT            NOT NULL,
   descricao        TEXT           NOT NULL,
   qtd_fotos        INT            NOT NULL,
   CONSTRAINT pk_pacotes
     PRIMARY KEY (id_pacote)
);

-- RELAÇÃO DE CONTRATAÇÃO DE PACOTES POR CLIENTE
CREATE TABLE pacotes_contratados (
   id_pacotes_contratados      SERIAL,
   id_cliente                  INT        NOT NULL,
   id_pacote                   INT        NOT NULL,
   data_contratacao            DATE       NOT NULL DEFAULT CURRENT_DATE,
   CONSTRAINT pk_pacotes_contratados
     PRIMARY KEY (id_pacotes_contratados),
   CONSTRAINT fk_pacotes_contrados_clientes
     FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente),
   CONSTRAINT fk_pacotes_contrados_pacotes
     FOREIGN KEY (id_pacote) REFERENCES pacotes(id_pacote)
);

-- AGENDAMENTO
CREATE TABLE agendamentos (
   id_agendamento        SERIAL,
   id_cliente            INT          NOT NULL,
   id_fotografo          INT          NOT NULL,
   id_pacote             INT          NOT NULL,
   data_hora             TIMESTAMP    NOT NULL,
   CONSTRAINT pk_agendamento
     PRIMARY KEY (id_agendamento),
   CONSTRAINT fk_agendamentos_clientes
     FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente),
   CONSTRAINT fk_agendamentos_fotografos
     FOREIGN KEY (id_fotografo) REFERENCES fotografos(id_fotografo),
   CONSTRAINT fk_agendamentos_pacotes
     FOREIGN KEY (id_pacote) REFERENCES pacotes(id_pacote)
   -- CONSTRAINT uc_fotografo_horario
   --   UNIQUE (fotografo_id, data_hora)
);

-- COMPARECIMENTO
CREATE TABLE comparecimentos (
   id_comparecimento    SERIAL,
   id_agendamento       INT            NOT NULL UNIQUE,
   id_cliente           INT            NOT NULL,
   status               VARCHAR(20)    CHECK (status IN ('confirmado', 'faltou', 'remarcado')),
   observacoes          TEXT,
   data_registro        TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
   CONSTRAINT pk_comparecimentos
     PRIMARY KEY (id_comparecimento),
   CONSTRAINT u_comparecimentos_agendamentos
     UNIQUE (id_agendamento),
   CONSTRAINT fk_comparecimentos_agendamentos
     FOREIGN KEY (id_agendamento) REFERENCES agendamentos(id_agendamento),
   CONSTRAINT fk_comparecimentos_clientes
     FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
);
