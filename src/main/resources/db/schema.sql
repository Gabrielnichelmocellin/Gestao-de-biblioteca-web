CREATE DATABASE biblioteca_db;
USE biblioteca_db;

CREATE TABLE `devolucoes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cpf_usuario` varchar(14) NOT NULL,
  `data_devolucao` datetime(6) DEFAULT NULL,
  `data_emprestimo` datetime(6) DEFAULT NULL,
  `emprestimo_id` bigint DEFAULT NULL,
  `titulo_livro` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `emprestimos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_usuario` int DEFAULT NULL,
  `id_livro` int DEFAULT NULL,
  `data_emprestimo` datetime(6) DEFAULT NULL,
  `data_devolucao` datetime(6) DEFAULT NULL,
  `ativo` bit(1) NOT NULL,
  `cpf_usuario` varchar(14) NOT NULL,
  `titulo_livro` varchar(200) NOT NULL,
  `status` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_usuario` (`id_usuario`),
  KEY `id_livro` (`id_livro`),
  CONSTRAINT `emprestimos_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `emprestimos_ibfk_2` FOREIGN KEY (`id_livro`) REFERENCES `livros` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `livros` (
  `id` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(200) NOT NULL,
  `autor` varchar(100) NOT NULL,
  `disponivel` tinyint(1) DEFAULT '1',
  `data_cadastro` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `usuarios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `cpf` varchar(14) NOT NULL,
  `tipo` varchar(20) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `data_cadastro` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cpf` (`cpf`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;