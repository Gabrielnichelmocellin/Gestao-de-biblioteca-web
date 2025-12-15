package com.biblioteca.controller;

import com.biblioteca.model.*;
import com.biblioteca.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private LivroService livroService;
    
    @Autowired
    private EmprestimoService emprestimoService;
    
    @Autowired
    private DevolucaoService devolucaoService;
    
    
    @GetMapping("/test")
    public Map<String, String> test() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "API Biblioteca está funcionando!");
        response.put("timestamp", new Date().toString());
        return response;
    }
    
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Biblioteca API");
        response.put("version", "1.0.0");
        response.put("usuarios", usuarioService.listarTodos().size());
        response.put("livros", livroService.listarTodos().size());
        response.put("emprestimosAtivos", emprestimoService.listarAtivos().size());
        response.put("devolucoes", devolucaoService.listarTodas().size());
        return response;
    }
    
    
    @GetMapping("/usuarios")
    public List<Usuario> getUsuarios() {
        return usuarioService.listarTodos();
    }
    
    @PostMapping("/usuarios")
    public Map<String, Object> criarUsuario(@RequestBody Map<String, String> dados) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String nome = dados.get("nome");
            String cpf = dados.get("cpf");
            String tipo = dados.get("tipo");
            
            if (nome == null || nome.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Nome é obrigatório");
                return response;
            }
            
            if (cpf == null || cpf.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "CPF é obrigatório");
                return response;
            }
            
            if (tipo == null || tipo.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Tipo é obrigatório");
                return response;
            }
            
            if (usuarioService.existePorCpf(cpf)) {
                response.put("success", false);
                response.put("message", "CPF já cadastrado");
                return response;
            }
          
            Usuario usuario = new Usuario(nome, cpf, tipo);
            Usuario usuarioSalvo = usuarioService.salvar(usuario);
            
            response.put("success", true);
            response.put("message", "Usuário cadastrado com sucesso");
            response.put("usuario", usuarioSalvo);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao cadastrar usuário: " + e.getMessage());
        }
        
        return response;
    }
    
    @GetMapping("/livros")
    public List<Livro> getLivros() {
        return livroService.listarTodos();
    }
    
    @GetMapping("/livros/disponiveis")
    public List<Livro> getLivrosDisponiveis() {
        return livroService.listarDisponiveis();
    }
    
    @PostMapping("/livros")
    public Map<String, Object> criarLivro(@RequestBody Map<String, String> dados) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String titulo = dados.get("titulo");
            String autor = dados.get("autor");
            
            if (titulo == null || titulo.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Título é obrigatório");
                return response;
            }
            
            if (autor == null || autor.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Autor é obrigatório");
                return response;
            }
            
            Livro livro = new Livro(titulo, autor);
            Livro livroSalvo = livroService.salvar(livro);
            
            response.put("success", true);
            response.put("message", "Livro cadastrado com sucesso");
            response.put("livro", livroSalvo);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao cadastrar livro: " + e.getMessage());
        }
        
        return response;
    }
    
    @GetMapping("/emprestimos")
    public List<Emprestimo> getEmprestimos() {
        return emprestimoService.listarTodos();
    }
    
    @GetMapping("/emprestimos/ativos")
    public List<Emprestimo> getEmprestimosAtivos() {
        return emprestimoService.listarAtivos();
    }
    
    @PostMapping("/emprestimos")
    public Map<String, Object> criarEmprestimo(@RequestBody Map<String, String> dados) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String cpfUsuario = dados.get("cpfUsuario");
            String tituloLivro = dados.get("tituloLivro");
            
            if (cpfUsuario == null || cpfUsuario.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "CPF do usuário é obrigatório");
                return response;
            }
            
            if (tituloLivro == null || tituloLivro.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Título do livro é obrigatório");
                return response;
            }
            
            Emprestimo emprestimo = emprestimoService.registrarEmprestimo(cpfUsuario, tituloLivro);
            
            response.put("success", true);
            response.put("message", "Empréstimo registrado com sucesso");
            response.put("emprestimo", emprestimo);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        
        return response;
    }
    
    @GetMapping("/devolucoes")
    public List<Devolucao> getDevolucoes() {
        return devolucaoService.listarTodas();
    }
    
    @PostMapping("/emprestimos/{id}/devolver")
    public Map<String, Object> devolverLivro(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Emprestimo emprestimo = emprestimoService.devolverLivro(id);
            
            response.put("success", true);
            response.put("message", "Livro devolvido com sucesso");
            response.put("emprestimo", emprestimo);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        
        return response;
    }
    
    @PostMapping("/inicializar")
    public Map<String, Object> inicializarDados() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (usuarioService.listarTodos().isEmpty()) {
                usuarioService.salvar(new Usuario("Admin", "111.222.333-44", "admin"));
                usuarioService.salvar(new Usuario("João Silva", "123.456.789-00", "cliente"));
                usuarioService.salvar(new Usuario("Maria Santos", "987.654.321-00", "cliente"));
            }
            
            if (livroService.listarTodos().isEmpty()) {
                livroService.salvar(new Livro("Dom Casmurro", "Machado de Assis"));
                livroService.salvar(new Livro("1984", "George Orwell"));
                livroService.salvar(new Livro("O Senhor dos Anéis", "J.R.R. Tolkien"));
                livroService.salvar(new Livro("Harry Potter e a Pedra Filosofal", "J.K. Rowling"));
                livroService.salvar(new Livro("Orgulho e Preconceito", "Jane Austen"));
            }
            
            response.put("success", true);
            response.put("message", "Dados iniciais criados com sucesso");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao criar dados iniciais: " + e.getMessage());
        }
        
        return response;
    }
}