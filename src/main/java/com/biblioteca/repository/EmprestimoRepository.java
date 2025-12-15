package com.biblioteca.repository;

import com.biblioteca.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findByCpfUsuario(String cpfUsuario);
    List<Emprestimo> findByAtivo(boolean ativo);
    List<Emprestimo> findByCpfUsuarioAndAtivo(String cpfUsuario, boolean ativo);
}