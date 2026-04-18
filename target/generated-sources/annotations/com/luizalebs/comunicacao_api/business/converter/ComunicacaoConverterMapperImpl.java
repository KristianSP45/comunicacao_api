package com.luizalebs.comunicacao_api.business.converter;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:26:17-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.18 (Microsoft)"
)
@Component
public class ComunicacaoConverterMapperImpl implements ComunicacaoConverterMapper {

    @Override
    public ComunicacaoEntity paraEntity(ComunicacaoInDTO comunicacaoInDTO) {
        if ( comunicacaoInDTO == null ) {
            return null;
        }

        ComunicacaoEntity.ComunicacaoEntityBuilder comunicacaoEntity = ComunicacaoEntity.builder();

        comunicacaoEntity.dataHoraEnvio( comunicacaoInDTO.getDataHoraEnvio() );
        comunicacaoEntity.nomeDestinatario( comunicacaoInDTO.getNomeDestinatario() );
        comunicacaoEntity.emailDestinatario( comunicacaoInDTO.getEmailDestinatario() );
        comunicacaoEntity.telefoneDestinatario( comunicacaoInDTO.getTelefoneDestinatario() );
        comunicacaoEntity.mensagem( comunicacaoInDTO.getMensagem() );
        comunicacaoEntity.modoDeEnvio( comunicacaoInDTO.getModoDeEnvio() );
        comunicacaoEntity.statusEnvio( comunicacaoInDTO.getStatusEnvio() );

        return comunicacaoEntity.build();
    }

    @Override
    public ComunicacaoOutDTO paraDTO(ComunicacaoEntity comunicacaoEntity) {
        if ( comunicacaoEntity == null ) {
            return null;
        }

        ComunicacaoOutDTO.ComunicacaoOutDTOBuilder comunicacaoOutDTO = ComunicacaoOutDTO.builder();

        comunicacaoOutDTO.id( comunicacaoEntity.getId() );
        comunicacaoOutDTO.dataHoraEnvio( comunicacaoEntity.getDataHoraEnvio() );
        comunicacaoOutDTO.nomeDestinatario( comunicacaoEntity.getNomeDestinatario() );
        comunicacaoOutDTO.emailDestinatario( comunicacaoEntity.getEmailDestinatario() );
        comunicacaoOutDTO.telefoneDestinatario( comunicacaoEntity.getTelefoneDestinatario() );
        comunicacaoOutDTO.mensagem( comunicacaoEntity.getMensagem() );
        comunicacaoOutDTO.modoDeEnvio( comunicacaoEntity.getModoDeEnvio() );
        comunicacaoOutDTO.statusEnvio( comunicacaoEntity.getStatusEnvio() );

        return comunicacaoOutDTO.build();
    }
}
