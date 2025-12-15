package com.biblioteca.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "devolucoes")
public class Devolucao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "emprestimo_id")
    private Long emprestimoId;
    
    @Column(nullable = false, length = 14)
    private String cpfUsuario;
    
    @Column(nullable = false, length = 200)
    private String tituloLivro;
    
    @Column(name = "data_emprestimo")
    private LocalDateTime dataEmprestimo;
    
    @Column(name = "data_devolucao")
    private LocalDateTime dataDevolucao;
   
    public Devolucao() {
        this.dataDevolucao = LocalDateTime.now();
    }
    
    public Devolucao(Long emprestimoId, String cpfUsuario, String tituloLivro, LocalDateTime dataEmprestimo) {
        this.emprestimoId = emprestimoId;
        this.cpfUsuario = cpfUsuario;
        this.tituloLivro = tituloLivro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = LocalDateTime.now();
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getEmprestimoId() { return emprestimoId; }
    public void setEmprestimoId(Long emprestimoId) { this.emprestimoId = emprestimoId; }
    
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
}