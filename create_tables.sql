-- Script para criar a tabela cliente
CREATE TABLE cliente (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    telefone VARCHAR(20)
);

-- Script para criar a tabela produto
CREATE TABLE produto (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco NUMERIC(10,2) NOT NULL,
    quantidade INTEGER NOT NULL
);

-- Script para criar a tabela pedido
CREATE TABLE pedido (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER REFERENCES cliente(id),
    produto_id INTEGER REFERENCES produto(id),
    quantidade INTEGER NOT NULL,
    data_pedido DATE NOT NULL
);

