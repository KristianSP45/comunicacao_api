package com.luizalebs.comunicacao_api.business.service;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.business.converter.ComunicacaoConverterMapper;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.ResourceNotFoundException;
import com.luizalebs.comunicacao_api.infraestructure.repositories.ComunicacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ComunicacaoService {

    private final ComunicacaoRepository repository;
    //private final ComunicacaoConverter converter;
    private final ComunicacaoConverterMapper mapper;

    public ComunicacaoOutDTO agendarComunicacao(ComunicacaoInDTO dto) {
        if (Objects.isNull(dto)) {
            throw new RuntimeException();
        }
        dto.setStatusEnvio(StatusEnvioEnum.PENDENTE);
        ComunicacaoEntity entity = mapper.paraEntity(dto);
        repository.save(entity);
        return mapper.paraDTO(entity);
    }

    public ComunicacaoOutDTO buscarStatusComunicacao(String emailDestinatario) {
        ComunicacaoEntity entity = repository.findByEmailDestinatario(emailDestinatario);
        if (Objects.isNull(entity)) {
            throw new RuntimeException();
        }
        return mapper.paraDTO(entity);
    }

    public ComunicacaoOutDTO buscaComunicacaoPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        try {
            return mapper.paraDTO(repository.findByDataHoraEnvioBetweenAndStatusEnvio(dataInicial, dataFinal, StatusEnvioEnum.PENDENTE));
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
        return (mapper.paraDTO(entity));
    }

    public ComunicacaoOutDTO alterarStatusNotificado(Long id, StatusEnvioEnum statusEnvioEnum) {
        ComunicacaoEntity entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comunicação não encontrada"+id));
        entity.setStatusEnvio(statusEnvioEnum);
        return (mapper.paraDTO(repository.save(entity)));
    }
}
