CREATE DATABASE biblioteca;
USE biblioteca;

CREATE TABLE usuario (
    matricula VARCHAR(20) PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    endereco  VARCHAR(200),
    tipo      VARCHAR(20) NOT NULL  -- 'Aluno', 'Professor', 'Funcionario'
);

CREATE TABLE livro (
    id                   INT PRIMARY KEY AUTO_INCREMENT,
    titulo               VARCHAR(200) NOT NULL,
    autor                VARCHAR(100) NOT NULL,
    ano_publicacao       INT,
    categoria            VARCHAR(100),
    quantidade_disponivel INT DEFAULT 0
);

CREATE TABLE emprestimo (
    id                  INT PRIMARY KEY AUTO_INCREMENT,
    matricula_usuario   VARCHAR(20),
    id_livro            INT,
    data_retirada       DATETIME,
    data_prevista       DATETIME,
    data_devolucao_real DATETIME,
    status              VARCHAR(20) DEFAULT 'ATIVO',
    FOREIGN KEY (matricula_usuario) REFERENCES usuario(matricula),
    FOREIGN KEY (id_livro)          REFERENCES livro(id)
);

CREATE TABLE reserva (
    id                INT PRIMARY KEY AUTO_INCREMENT,
    matricula_usuario VARCHAR(20),
    id_livro          INT,
    data_solicitacao  DATETIME,
    data_expiracao    DATETIME,
    status            VARCHAR(20) DEFAULT 'ATIVA',
    FOREIGN KEY (matricula_usuario) REFERENCES usuario(matricula),
    FOREIGN KEY (id_livro)          REFERENCES livro(id)
);

INSERT INTO usuario VALUES ('A001', 'Joao Silva',  'Rua A, 10', 'Aluno');
INSERT INTO usuario VALUES ('A002', 'Ana Souza',   'Rua B, 22', 'Aluno');
INSERT INTO usuario VALUES ('P001', 'Prof. Maria', 'Av. C, 5',  'Professor');
INSERT INTO usuario VALUES ('F001', 'Carlos',      'Rua D, 3',  'Funcionario');

INSERT INTO livro (titulo, autor, ano_publicacao, categoria, quantidade_disponivel) VALUES
    ('Clean Code',   'Robert Martin',    2008, 'Tecnologia', 3),
    ('Dom Casmurro', 'Machado de Assis', 1899, 'Literatura', 5),
    ('O Hobbit',     'Tolkien',          1937, 'Fantasia',   2);