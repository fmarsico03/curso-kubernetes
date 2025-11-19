package org.fmarsico.springcloud.msvc.gateway.controllers;

import feign.FeignException;
import org.fmarsico.springcloud.msvc.gateway.clients.CursoClientRest;
import org.fmarsico.springcloud.msvc.gateway.clients.UsuarioClientRest;
import org.fmarsico.springcloud.msvc.gateway.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {

    @Autowired
    private UsuarioClientRest usuarioClient;

    @Autowired
    private CursoClientRest cursoClient;

    @GetMapping
    public ResponseEntity<Map<String, List<Usuario>>> findAll() {
        return ResponseEntity.ok(usuarioClient.findAll());
    }

    @GetMapping(params = "ids")
    public ResponseEntity<List<Usuario>> findAllByIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(usuarioClient.findAllByIds(ids));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuario(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioClient.getById(id);
            return ResponseEntity.ok(usuario);
        } catch (FeignException.NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Usuario usuario) {
        try {
            Usuario created = usuarioClient.create(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear usuario: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario updated = usuarioClient.update(id, usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(updated);
        } catch (FeignException.NotFound e) {
            return ResponseEntity.notFound().build();
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar usuario: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            usuarioClient.delete(id);
            try {
                cursoClient.deleteUserFromAllCourses(id);
            } catch (FeignException.NotFound ignored) {}
            return ResponseEntity.noContent().build();
        } catch (FeignException.NotFound e) {
            return ResponseEntity.notFound().build();
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar usuario: " + e.getMessage());
        }
    }
}
