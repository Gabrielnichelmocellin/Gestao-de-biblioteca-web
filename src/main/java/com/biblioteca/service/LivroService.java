package com.biblioteca.service;

import com.biblioteca.model.Livro;
import com.biblioteca.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LivroService {
    
    @Autowired
    private LivroRepository livroRepository;
    
    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }
    
    public List<Livro> listarDisponiveis() {
        return livroRepository.findByDisponivel(true);
    }
    
    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.findById(id);
    }
    
    public Livro salvar(Livro livro) {
        return livroRepository.save(livro);
    }
    
    public void deletar(Long id) {
        livroRepository.deleteById(id);
    }
    
    public List<Livro> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }
}