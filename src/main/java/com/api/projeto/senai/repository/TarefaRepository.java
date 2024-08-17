package com.api.projeto.senai.repository;

import com.api.projeto.senai.classes.Tarefa;
import com.api.projeto.senai.dto.TarefaDTO;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    Tarefa save(@Valid TarefaDTO tarefa);

    List<Tarefa> findByTag(String tag);

}
