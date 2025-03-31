package com.mv.cidaweb.controller;

import com.mv.cidaweb.model.dtos.ScriptComComentariosDTO;
import com.mv.cidaweb.model.dtos.ScriptDTO;
import com.mv.cidaweb.model.dtos.ScriptEntradaDTO;
import com.mv.cidaweb.model.exceptions.ObjectNotFoundException;
import com.mv.cidaweb.service.ScriptService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/script")
@Tag(name = "Scripts", description = "Operações relacionadas a scripts")
public class ScriptController {

    private final ScriptService scriptService;

    public ScriptController(ScriptService scriptService) {
        this.scriptService = scriptService;
    }

    @GetMapping("/listar")
    public ResponseEntity<ArrayList<ScriptDTO>> listAllScripts() {
        return ResponseEntity.ok().body(scriptService.getAllScripts());
    }

    @GetMapping("/listar/meus-favoritos")
    public ResponseEntity<ArrayList<ScriptDTO>> listarMeusScriptsEFavoritos() {
        return ResponseEntity.ok().body(scriptService.listarMeusScriptsEFavoritos());
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

    @GetMapping("/listar/nome/{nome}")
    public ResponseEntity<ArrayList<ScriptDTO>> listAllScripts(@PathVariable String nome) {
        return ResponseEntity.ok().body(scriptService.getAllScriptsByAutor(nome));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScriptDTO> updateScript(@RequestBody ScriptDTO scriptDTO, Long id) throws ObjectNotFoundException {
        return ResponseEntity.ok().body(scriptService.updateScript(scriptDTO, id));
    }

    @PostMapping("/salvar")
    public ResponseEntity<ScriptDTO> saveScript(@RequestBody ScriptEntradaDTO scriptEntradaDTO) {
        return ResponseEntity.ok().body(scriptService.saveScript(scriptEntradaDTO));
    }


    @PostMapping("/curtir-descurtir/{script_id}")
    public ResponseEntity<ScriptDTO> curtirDescurtirScript(@PathVariable Long script_id) throws ObjectNotFoundException {
        return ResponseEntity.ok().body(scriptService.curtirDescurtirScript(script_id));
    }

}
