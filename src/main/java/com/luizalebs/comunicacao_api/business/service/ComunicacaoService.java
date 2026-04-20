package com.luizalebs.comunicacao_api.business.service;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.business.converter.ComunicacaoConverterMapper;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.ConflictException;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.IllegalArgumentException;
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
            throw new IllegalArgumentException("Dados da comunicação não podem ser nulos");
        }

        if (repository.existsByEmailDestinatario(dto.getEmailDestinatario())) {
            throw new ConflictException("Agendamento já cadastrado");
        }
        dto.setStatusEnvio(StatusEnvioEnum.PENDENTE);
        ComunicacaoEntity entity = mapper.paraEntity(dto);
        repository.save(entity);
        return mapper.paraDTO(entity);
    }

    public ComunicacaoOutDTO buscarStatusComunicacao(String emailDestinatario) {
        ComunicacaoEntity entity = repository.findByEmailDestinatario(emailDestinatario);
        if(emailDestinatario == null){
            throw new IllegalArgumentException("Dados da comunicação não podem ser nulos");
        }

        if (Objects.isNull(entity)) {
            throw new ResourceNotFoundException("Agendamento não cadastrado");
        }
        return mapper.paraDTO(entity);
    }

    public ComunicacaoOutDTO buscaComunicacaoPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        validarDatas(dataInicial, dataFinal);

        ComunicacaoEntity entity = repository.findByDataHoraEnvioBetweenAndStatusEnvio(dataInicial, dataFinal, StatusEnvioEnum.PENDENTE);

        if (Objects.isNull(entity)) {
            throw new ResourceNotFoundException("Agendamento não encontrado para o período informado");
        }
            return mapper.paraDTO(entity);
    }

    private void validarDatas(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        if (Objects.isNull(dataInicial) || Objects.isNull(dataFinal)) {
            throw new IllegalArgumentException("As datas não podem ser nulas");
        }

        if (dataInicial.isAfter(dataFinal)) {
            throw new IllegalArgumentException("A data inicial não pode ser maior que a data final");
        }
    }

    public ComunicacaoOutDTO alterarStatusComunicacaoParaCancelado(String emailDestinatario) {
        ComunicacaoEntity entity = repository.findByEmailDestinatario(emailDestinatario);
        if (Objects.isNull(entity)) {
            throw new ResourceNotFoundException("Agendamento não cadastrado");
        }

        if (entity.getStatusEnvio() == StatusEnvioEnum.CANCELADO) {
            throw new ConflictException("Agendamento já está cancelado");
        }
        entity.setStatusEnvio(StatusEnvioEnum.CANCELADO);
        repository.save(entity);
        return mapper.paraDTO(entity);
    }

    public ComunicacaoOutDTO alterarStatusNotificado(Long id, StatusEnvioEnum statusEnvioEnum) {
        ComunicacaoEntity entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"+id));

        if (entity.getStatusEnvio() == statusEnvioEnum) {
            throw new ConflictException("Agendamento já está com esse status");
        }

        entity.setStatusEnvio(statusEnvioEnum);
        return mapper.paraDTO(repository.save(entity));
    }
}
