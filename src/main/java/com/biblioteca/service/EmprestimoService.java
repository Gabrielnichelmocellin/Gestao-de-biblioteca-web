package com.biblioteca.service;

import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Livro;
import com.biblioteca.model.Usuario;
import com.biblioteca.model.Devolucao;
import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.LivroRepository;
import com.biblioteca.repository.UsuarioRepository;
import com.biblioteca.repository.DevolucaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmprestimoService {
    
    @Autowired
    private EmprestimoRepository emprestimoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private LivroRepository livroRepository;
    
    @Autowired
    private DevolucaoRepository devolucaoRepository;
    
    public List<Emprestimo> listarTodos() {
        return emprestimoRepository.findAll();
    }
    
    public List<Emprestimo> listarAtivos() {
        return emprestimoRepository.findByAtivo(true);
    }
    
    public Optional<Emprestimo> buscarPorId(Long id) {
        return emprestimoRepository.findById(id);
    }
    
    public Emprestimo registrarEmprestimo(String cpfUsuario, String tituloLivro) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCpf(cpfUsuario);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado com CPF: " + cpfUsuario);
        }
        
        List<Livro> livros = livroRepository.findByTituloContainingIgnoreCase(tituloLivro);
        Livro livro = null;
        
        for (Livro l : livros) {
            if (l.isDisponivel()) {
                livro = l;
                break;
            }
        }
        
        if (livro == null) {
            throw new RuntimeException("Livro não disponível: " + tituloLivro);
        }
        
        List<Emprestimo> emprestimosAtivos = emprestimoRepository.findByCpfUsuarioAndAtivo(cpfUsuario, true);
        if (emprestimosAtivos.size() >= 3) {
            throw new RuntimeException("Usuário atingiu o limite de 3 empréstimos ativos");
        }
        
        livro.setDisponivel(false);
        livroRepository.save(livro);
        
        Emprestimo emprestimo = new Emprestimo(cpfUsuario, tituloLivro);
        return emprestimoRepository.save(emprestimo);
    }
    
    public Emprestimo devolverLivro(Long emprestimoId) {
        Optional<Emprestimo> emprestimoOpt = emprestimoRepository.findById(emprestimoId);
        if (emprestimoOpt.isEmpty()) {
            throw new RuntimeException("Empréstimo não encontrado");
        }
        
        Emprestimo emprestimo = emprestimoOpt.get();
        
        if (!emprestimo.isAtivo()) {
            throw new RuntimeException("Este empréstimo já foi devolvido");
        }
        
        Devolucao devolucao = new Devolucao(
            emprestimoId,
            emprestimo.getCpfUsuario(),
            emprestimo.getTituloLivro(),
            emprestimo.getDataEmprestimo()
        );
        devolucaoRepository.save(devolucao);
        
        emprestimo.setAtivo(false);
        emprestimo.setStatus("DEVOLVIDO");
        emprestimo.setDataDevolucao(LocalDateTime.now());
        emprestimoRepository.save(emprestimo);
        
        List<Livro> livros = livroRepository.findByTituloContainingIgnoreCase(emprestimo.getTituloLivro());
        for (Livro livro : livros) {
            if (!livro.isDisponivel()) {
                livro.setDisponivel(true);
                livroRepository.save(livro);
                break;
            }
        }
        
        return emprestimo;
    }
}