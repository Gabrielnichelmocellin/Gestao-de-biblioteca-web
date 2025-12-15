package com.biblioteca.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "emprestimos")
public class Emprestimo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 14)
    private String cpfUsuario;
    
    @Column(nullable = false, length = 200)
    private String tituloLivro;
    
    @Column(name = "data_emprestimo")
    private LocalDateTime dataEmprestimo;
    
    @Column(name = "data_devolucao")
    private LocalDateTime dataDevolucao;
    
    @Column(nullable = false)
    private boolean ativo = true;
    
    @Column(length = 20)
    private String status = "EMPRESTADO";
   
    public Emprestimo() {
        this.dataEmprestimo = LocalDateTime.now();
    }
    
    public Emprestimo(String cpfUsuario, String tituloLivro) {
        this.cpfUsuario = cpfUsuario;
        this.tituloLivro = tituloLivro;
        this.dataEmprestimo = LocalDateTime.now();
        this.ativo = true;
        this.status = "EMPRESTADO";
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCpfUsuario() { return cpfUsuario; }
    public void setCpfUsuario(String cpfUsuario) { this.cpfUsuario = cpfUsuario; }
    
    public String getTituloLivro() { return tituloLivro; }
    public void setTituloLivro(String tituloLivro) { this.tituloLivro = tituloLivro; }
    
    public LocalDateTime getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(LocalDateTime dataEmprestimo) { 
        this.dataEmprestimo = dataEmprestimo; 
    }
    
    public LocalDateTime getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(LocalDateTime dataDevolucao) { 
        this.dataDevolucao = dataDevolucao; 
    }
    
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}