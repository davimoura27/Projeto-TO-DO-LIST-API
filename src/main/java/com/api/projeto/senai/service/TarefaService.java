package com.api.projeto.senai.service;

import com.api.projeto.senai.repository.TarefaRepository;

import com.api.projeto.senai.classes.Tarefa;
import com.api.projeto.senai.dto.TarefaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api.projeto.senai.exception.*;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Emailservice emailService;  
    public TarefaDTO convertToDto(Tarefa tarefa) {
        return modelMapper.map(tarefa, TarefaDTO.class);
    }

    public Tarefa convertToEntity(TarefaDTO tarefaDTO) {
        return modelMapper.map(tarefaDTO, Tarefa.class);
    }

    private void sendEmailNotification(Tarefa tarefaSalva, String subject, String text) {
      emailService.sendEmail(tarefaSalva.getEmail(), text, subject);
        // String subject = "Tarefa Criada com Sucesso!";
        // String text = "Tarefa criada com sucesso: " + tarefaSalva.getNomeTarefa();
        // emailService.sendEmail(tarefaSalva.getEmail(), text, subject);  
    }

    public TarefaDTO criarTarefa(TarefaDTO tarefaDTO) {
        Tarefa tarefa = convertToEntity(tarefaDTO);
        Tarefa tarefaSalva = tarefaRepository.save(tarefa);

        String subject = "Nova tarefa criada!";
        String text = "Parabens! A tarefa " + tarefaSalva.getNomeTarefa() + " foi criada com sucesso!" ;
        sendEmailNotification(tarefaSalva, subject, text);
        
        return convertToDto(tarefaSalva);
    }


    public List<TarefaDTO> getAll() {
        List<Tarefa> tarefa = tarefaRepository.findAll();
        return tarefa.stream().map(this::convertToDto).toList();
    }

    public List<TarefaDTO> buscarPorTag(String tag) {
        if (tag == null || tag.isEmpty()) {
            throw new InvalidInputException("Tag não pode ser nula ou vazia.");
        }

        List<Tarefa> tarefas = tarefaRepository.findByTag(tag);
        List<TarefaDTO> tarefaDTOs = new ArrayList<>();

        for (Tarefa tarefa : tarefas) {
            TarefaDTO dto = convertToDto(tarefa);
            tarefaDTOs.add(dto);
        }
        return tarefaDTOs;
    }
    public Tarefa getByIdTarefa(Long id){
        return tarefaRepository.findById(id).orElse(null);
    }  
    

    public TarefaDTO getById(Long id) {
        if (id == null) {
            throw new InvalidInputException("Id não pode ser nulo ou vazio.");
        }
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com ID: " + id));
        return convertToDto(tarefa);
    }

    public TarefaDTO update(Long id,Tarefa tarefa, TarefaDTO tarefaDTO) {
     
        if (tarefaDTO.getNomeTarefa() != null) {
            tarefa.setNomeTarefa(tarefaDTO.getNomeTarefa());
        }
        if (tarefaDTO.getTag() != null) {
            tarefa.setTag(tarefaDTO.getTag());
        }
        if (tarefaDTO.getEmail() != null) {
            tarefa.setEmail(tarefaDTO.getEmail());
        }
        Tarefa tarefaSalva = tarefaRepository.save(tarefa);

        String subject = "Tarefa atualizada!";
        String text = "Você atualizou a Tarefa:" + tarefaSalva.getNomeTarefa();
        sendEmailNotification(tarefaSalva, subject, text);
     
        return convertToDto(tarefaSalva);
    }

    public void delete(Long id) {

        if (id == null) {
            throw new InvalidInputException("Id não pode ser nulo ou vazio.");
        }
        tarefaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com ID: " + id));
        tarefaRepository.deleteById(id);
    }

    
}