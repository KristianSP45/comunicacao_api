package com.luizalebs.comunicacao_api.infraestructure.repositories;

import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ComunicacaoRepository extends CrudRepository<ComunicacaoEntity, Long> {

    ComunicacaoEntity findByEmailDestinatario(String nomeDestinatario);

    ComunicacaoEntity findByDataHoraEnvioBetweenAndStatusEnvio(
            LocalDateTime dataInicial,
            LocalDateTime dataFinal,
            StatusEnvioEnum statusEnvioEnum);
}
