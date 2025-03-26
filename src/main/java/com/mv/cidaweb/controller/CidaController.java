package com.mv.cidaweb.controller;

import com.mv.cidaweb.exceptions.ObjectNotFoundException;
import com.mv.cidaweb.model.dtos.ComentarioDTO;
import com.mv.cidaweb.model.dtos.ComentarioInDTO;
import com.mv.cidaweb.model.dtos.ScriptComComentariosDTO;
import com.mv.cidaweb.model.dtos.ScriptDTO;
import com.mv.cidaweb.service.PessoaService;
import com.mv.cidaweb.service.ScriptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/script")
@Tag(name = "Scripts", description = "Operações relacionadas a scripts")
public class CidaController {

    private final ScriptService scriptService;
    private final PessoaService pessoaService;

    public CidaController(ScriptService scriptService, PessoaService pessoaService) {
        this.scriptService = scriptService;
        this.pessoaService = pessoaService;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ScriptDTO> getScript(@PathVariable Long id) throws ObjectNotFoundException {
        return ResponseEntity.ok().body(scriptService.getScriptById(id));
    }

    @GetMapping("/id/{id}/com-comentarios")
    public ResponseEntity<ScriptComComentariosDTO> getScriptComComentarios(@PathVariable Long id) throws ObjectNotFoundException {
        return ResponseEntity.ok().body(scriptService.getScriptByIdComComentarios(id));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<ScriptDTO> getScript(@PathVariable String nome) throws ObjectNotFoundException {
        return ResponseEntity.ok().body(scriptService.getScriptByNome(nome));
    }

    @GetMapping("/nome/{nome}/com-comentarios")
    public ResponseEntity<ScriptComComentariosDTO> getScriptComComentarios(@PathVariable String nome) throws ObjectNotFoundException {
        return ResponseEntity.ok().body(scriptService.getScriptByNomeComComentarios(nome));
    }

    @GetMapping("/listar")
    public ResponseEntity<ArrayList<ScriptDTO>> listAllScripts() {
        return ResponseEntity.ok().body(scriptService.getAllScripts());
    }

    @GetMapping("/listar/nome/{nome}")
    public ResponseEntity<ArrayList<ScriptDTO>> listAllScripts(@PathVariable String nome) {
        return ResponseEntity.ok().body(scriptService.getAllScriptsByAutor(nome));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScriptDTO> updateScript(@RequestBody ScriptDTO scriptDTO, Long id) throws ObjectNotFoundException {
        return ResponseEntity.ok().body(scriptService.updateScript(scriptDTO, id));
    }

    @PostMapping("/salvar")
    public ResponseEntity<ScriptDTO> saveScript(@RequestBody ScriptDTO scriptDTO) {
        return ResponseEntity.ok().body(scriptService.saveScript(scriptDTO));
    }

    @Operation(summary = "Adicionar comentário a um script")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentário adicionado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Script não encontrado")
    })
    @PostMapping("/{script_id}/comentario")
    public ComentarioDTO salvarComentario(@RequestBody ComentarioInDTO comentario, @PathVariable Long script_id) throws ObjectNotFoundException {
        return scriptService.addComentario(comentario, script_id);
    }

    @GetMapping("/{script_id}/comentarios")
    public ArrayList<ComentarioDTO> getComentarios(@PathVariable Long script_id) throws ObjectNotFoundException {
        return scriptService.getComentarios(script_id);
    }

    @GetMapping("/comentario/{script_id}")
    public ComentarioDTO getComentario(@PathVariable Long script_id) throws ObjectNotFoundException {
        return scriptService.getComentario(script_id);
    }
}
