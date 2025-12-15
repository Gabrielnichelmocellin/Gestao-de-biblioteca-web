package com.biblioteca.service;

import com.biblioteca.model.Devolucao;
import com.biblioteca.repository.DevolucaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DevolucaoService {
    
    @Autowired
    private DevolucaoRepository devolucaoRepository;
    
    public List<Devolucao> listarTodas() {
        return devolucaoRepository.findAll();
    }
    
    public Devolucao salvar(Devolucao devolucao) {
        return devolucaoRepository.save(devolucao);
    }
}