package com.luizalebs.comunicacao_api.api;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.business.service.ComunicacaoService;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/comunicacao")
@RequiredArgsConstructor
@Tag(name = "Comunicação", description = "Cadastro, busca e cancelamento de mensagens")
public class ComunicacaoController {

    private final ComunicacaoService service;

    //public ComunicacaoController(ComunicacaoService service) {
    //    this.service = service;
    //}

    @PostMapping("/agendar")
    @Operation(summary = "Agendar mensagem", description = "Cria uma nova mensagem")
    @ApiResponse(responseCode = "200", description = "Agendamento feito com sucesso")
    @ApiResponse(responseCode = "409", description = "Agendamento já cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<ComunicacaoOutDTO> agendar(@RequestBody ComunicacaoInDTO dto)  {
        return ResponseEntity.ok(service.agendarComunicacao(dto));
    }

    @GetMapping()
    @Operation(summary = "Buscar status", description = "Busca um status de uma mensagem")
    @ApiResponse(responseCode = "200", description = "Agendamento encontrado")
    @ApiResponse(responseCode = "404", description = "Agendamento não cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<ComunicacaoOutDTO> buscarStatus(@RequestParam String emailDestinatario) {
        return ResponseEntity.ok(service.buscarStatusComunicacao(emailDestinatario));
    }

    @GetMapping("/periodo")
    @Operation(summary = "Buscar mensagem por periodo", description = "Busca uma mensagem de um periodo determinado")
    @ApiResponse(responseCode = "200", description = "Agendamento encontrado")
    @ApiResponse(responseCode = "404", description = "Agendamento não cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<ComunicacaoOutDTO> buscarComunicacaoPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal) {
        return ResponseEntity.ok(service.buscaComunicacaoPorPeriodo(dataInicial, dataFinal));
    }

    @PatchMapping("/cancelar")
    @Operation(summary = "Cancelar mensagem", description = "Muda o status de uma mensagem para cancelado")
    @ApiResponse(responseCode = "200", description = "Agendamento cancelado com sucesso")
    @ApiResponse(responseCode = "404", description = "Agendamento não cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<ComunicacaoOutDTO> cancelarStatus(@RequestParam String emailDestinatario) {
        return ResponseEntity.ok(service.alterarStatusComunicacaoParaCancelado(emailDestinatario));
    }

    @PatchMapping()
    @Operation(summary = "Alterar status", description = "Busca um status de uma mensagem")
    @ApiResponse(responseCode = "200", description = "Agendamento encontrado")
    @ApiResponse(responseCode = "404", description = "Agendamento não cadastrado")
    @ApiResponse(responseCode = "409", description = "Agendamento já alterado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<ComunicacaoOutDTO> alterarStatusNotificacao(@RequestParam("id") Long id, @RequestParam StatusEnvioEnum statusEnvioEnum) {
        return ResponseEntity.ok(service.alterarStatusNotificado(id, statusEnvioEnum));
    }
}
