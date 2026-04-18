package com.luizalebs.comunicacao_api.business.service;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CronService {

    private final EmailService emailService;
    private final ComunicacaoService service;

    @Scheduled(cron = "${cron.horario}")
    public void buscarComunicacaoProximaHora(){

    log.info("Iniciada a busca da comunicação");

        LocalDateTime hAtual = LocalDateTime.now();
        LocalDateTime hFim = LocalDateTime.now().plusHours(1);

        ComunicacaoOutDTO outDTO = service.buscaComunicacaoPorPeriodo(hAtual, hFim);

        if (Objects.isNull(outDTO)) {
            log.info("Nenhuma comunicação encontrada no período");
            return;
        }

        List<ComunicacaoOutDTO> listaTarefas = Collections.singletonList(service.buscaComunicacaoPorPeriodo(hAtual, hFim));

    log.info("Comunicações encontradas "+listaTarefas);

        listaTarefas.forEach(comunicacao -> {
            emailService.sendEmail(comunicacao);
    log.info("Email enviado para o usuário "+comunicacao.getEmailDestinatario());
            service.alterarStatusNotificado(comunicacao.getId(), StatusEnvioEnum.ENVIADO);
        });
    log.info("Finalizada a busca e notificada de tarefas");
    }
}
