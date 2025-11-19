package org.fmarsico.springcloud.msvc.usuarios.controllers;

import jakarta.validation.Valid;
import org.fmarsico.springcloud.msvc.usuarios.models.entity.Usuario;
import org.fmarsico.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public Map<String,List<Usuario>> findAll() {
        return Collections.singletonMap("usuarios",service.findAll());
    }

    @GetMapping(params = "ids")
    public ResponseEntity<?> findAllByIds(@RequestParam List<Long> ids){
        return ResponseEntity.ok(service.findAllById(ids));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> details(@PathVariable Long id) {
        Optional<Usuario> usuarioOp = service.getByID(id);
        if (usuarioOp.isPresent())
            return ResponseEntity.ok().body(usuarioOp.get());
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Usuario usuario, BindingResult result){
        if (result.hasErrors()) {
            return validateFields(result);
        }
        if (service.existsByEmail(usuario.getEmail()))
            return ResponseEntity
                    .badRequest()
                    .body(Collections
                            .singletonMap("Error","Ya existe un usuario con ese email"));
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(usuario));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> edit(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validateFields(result);
        }
        Optional<Usuario> o = service.getByID(id);
        if (o.isPresent()) {
            Usuario usuarioDB = o.get();
            if (!usuario.getEmail().equalsIgnoreCase(usuarioDB.getEmail()) &&
                service.findByEmail(usuario.getEmail()).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(Collections
                                .singletonMap("Error", "Ya existe un usuario con ese email"));
            }
            usuarioDB.setNombre(usuario.getNombre());
            usuarioDB.setEmail(usuario.getEmail());
            usuarioDB.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(usuarioDB));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Usuario> o = service.getByID(id);
        if (o.isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> validateFields(BindingResult result) {
            Map<String,String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> {
                errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errores);
    }
}
