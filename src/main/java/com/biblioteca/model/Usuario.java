package com.biblioteca.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(nullable = false, unique = true, length = 14)
    private String cpf;
    
    @Column(nullable = false, length = 20)
    private String tipo; 
    
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;
    
    public Usuario() {
        this.dataCadastro = LocalDateTime.now();
    }
    
    public Usuario(String nome, String cpf, String tipo) {
        this.nome = nome;
        this.cpf = cpf;
        this.tipo = tipo;
        this.dataCadastro = LocalDateTime.now();
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { 
        this.dataCadastro = dataCadastro; 
    }
}