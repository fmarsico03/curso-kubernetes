package org.fmarsico.springcloud.msvc.gateway.controllers;

import feign.FeignException;
import org.fmarsico.springcloud.msvc.gateway.clients.CursoClientRest;
import org.fmarsico.springcloud.msvc.gateway.clients.UsuarioClientRest;
import org.fmarsico.springcloud.msvc.gateway.models.Curso;
import org.fmarsico.springcloud.msvc.gateway.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cursos")
public class CursosController {

    @Autowired
    private CursoClientRest cursoClient;
    @Autowired
    private UsuarioClientRest usuarioClient;

    @GetMapping
    public ResponseEntity<List<Curso>> findAll() {
        return ResponseEntity.ok(cursoClient.getCursos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCurso(@PathVariable Long id) {
        try {
            Curso curso = cursoClient.getCurso(id);

            if (curso.getUsuarios() == null || curso.getUsuarios().isEmpty()) {
                return ResponseEntity.ok(curso);
            }

            List<Usuario> usuarios = usuarioClient.findAllByIds(curso.getUsuarios());

            Map<String, Object> response = new HashMap<>();
            response.put("curso", curso);
            response.put("usuarios", usuarios);

            return ResponseEntity.ok(response);

        } catch (FeignException.NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<Curso> create(@RequestBody Curso curso) {
        Curso created = cursoClient.createCurso(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Curso curso) {
        try {
            Curso updated = cursoClient.updateCurso(id, curso);
            return ResponseEntity.status(HttpStatus.CREATED).body(updated);
        } catch (FeignException.NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            cursoClient.deleteCurso(id);
            return ResponseEntity.noContent().build();
        } catch (FeignException.NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{cursoId}/usuarios/{usuarioId}")
    public ResponseEntity<?> addUsuarioToCurso(@PathVariable Long cursoId,
                                               @PathVariable Long usuarioId) {
        try {
            Usuario usuarioFinal;

            try {
                usuarioFinal = usuarioClient.getById(usuarioId);
            } catch (FeignException.NotFound e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error usuario no encontrado");
            }

            cursoClient.addUsuarioToCurso(cursoId, usuarioFinal.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioFinal);

        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al agregar usuario al curso");
        }
    }

    @DeleteMapping("/{cursoId}/usuarios/{usuarioId}")
    public ResponseEntity<?> removeUsuarioFromCurso(@PathVariable Long cursoId,
                                                    @PathVariable Long usuarioId) {
        try {
            cursoClient.removeUsuarioFromCurso(cursoId, usuarioId);
            return ResponseEntity.noContent().build();
        } catch (FeignException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se pudo eliminar el usuario del curso");
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar usuario del curso");
        }
    }

}
