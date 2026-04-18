package com.luizalebs.comunicacao_api.business.converter;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ComunicacaoConverterMapper {

    @Mapping(source = "dataHoraEnvio", target = "dataHoraEnvio")

    ComunicacaoEntity paraEntity(ComunicacaoInDTO comunicacaoInDTO);

    ComunicacaoOutDTO paraDTO(ComunicacaoEntity comunicacaoEntity);
}
