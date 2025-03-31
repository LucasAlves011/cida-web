package com.mv.cidaweb.controller;

import com.mv.cidaweb.model.dtos.ComentarioDTO;
import com.mv.cidaweb.model.dtos.ComentarioInDTO;
import com.mv.cidaweb.model.exceptions.ObjectNotFoundException;
import com.mv.cidaweb.service.ComentarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @PostMapping("/salvar/{script_id}")
    public ComentarioDTO salvarComentario(@RequestBody ComentarioInDTO comentario, @PathVariable Long script_id) throws ObjectNotFoundException {
        return comentarioService.addComentario(comentario.comentario(), script_id);
    }

    @GetMapping("/listar/{script_id}")
    public ArrayList<ComentarioDTO> getComentarios(@PathVariable Long script_id) throws ObjectNotFoundException {
        return comentarioService.getComentarios(script_id);
    }

    @GetMapping("/{comentario_id}")
    public ComentarioDTO getComentario(@PathVariable Long comentario_id) throws ObjectNotFoundException {
        return comentarioService.getComentario(comentario_id);
    }

    @PostMapping("/curtir-descurtir/{comentario_id}")
    public ResponseEntity<ComentarioDTO> curtirDescurtirComentario(@PathVariable Long comentario_id) throws ObjectNotFoundException {
        return ResponseEntity.ok().body(comentarioService.curtirDescurtirComentario(comentario_id));
    }
}
