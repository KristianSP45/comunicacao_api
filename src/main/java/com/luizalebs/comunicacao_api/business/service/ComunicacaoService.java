package com.luizalebs.comunicacao_api.business.service;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.business.converter.ComunicacaoConverter;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.ResourceNotFoundException;
import com.luizalebs.comunicacao_api.infraestructure.repositories.ComunicacaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class ComunicacaoService {

    private final ComunicacaoRepository repository;
    private final ComunicacaoConverter converter;

    public ComunicacaoService(ComunicacaoRepository repository, ComunicacaoConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    public ComunicacaoOutDTO agendarComunicacao(ComunicacaoInDTO dto) {
        if (Objects.isNull(dto)) {
            throw new RuntimeException();
        }
        dto.setStatusEnvio(StatusEnvioEnum.PENDENTE);
        ComunicacaoEntity entity = converter.paraEntity(dto);
        repository.save(entity);
        ComunicacaoOutDTO outDTO = converter.paraDTO(entity);
        return outDTO;
    }

    public ComunicacaoOutDTO buscarStatusComunicacao(String emailDestinatario) {
        ComunicacaoEntity entity = repository.findByEmailDestinatario(emailDestinatario);
        if (Objects.isNull(entity)) {
            throw new RuntimeException();
        }
        return converter.paraDTO(entity);
    }

    public ComunicacaoOutDTO buscaComunicacaoPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        try {
            return converter.paraDTO(repository.findByDataHoraenvioBetweenAndStatusEnvio(dataInicial, dataFinal, StatusEnvioEnum.PENDENTE));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }

    }

    public ComunicacaoOutDTO alterarStatusComunicacaoParaCancelado(String emailDestinatario) {
        ComunicacaoEntity entity = repository.findByEmailDestinatario(emailDestinatario);
        if (Objects.isNull(entity)) {
            throw new RuntimeException();
        } if (entity.getStatusEnvio() == StatusEnvioEnum.CANCELADO) {
            throw new RuntimeException("Já está cancelado");
        }
        entity.setStatusEnvio(StatusEnvioEnum.CANCELADO);
        repository.save(entity);
        return (converter.paraDTO(entity));
    }

    public ComunicacaoOutDTO alterarStatusNotificado(Long id, StatusEnvioEnum statusEnvioEnum) {
        ComunicacaoEntity entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comunicação não encontrada"+id));
        entity.setStatusEnvio(statusEnvioEnum);
        return (converter.paraDTO(repository.save(entity)));
    }
}
