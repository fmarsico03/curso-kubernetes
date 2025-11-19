package org.fmarsico.springcloud.msvc.cursos.controllers;

import feign.FeignException;
import jakarta.validation.Valid;
import org.fmarsico.springcloud.msvc.cursos.models.Usuario;
import org.fmarsico.springcloud.msvc.cursos.models.entity.Curso;
import org.fmarsico.springcloud.msvc.cursos.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService service;

    @GetMapping
    public ResponseEntity<List<Curso>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCurso(@PathVariable Long id){
        Optional<Curso> o = service.getById(id);
        if (o.isPresent()) {
            return ResponseEntity.ok(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Curso curso, BindingResult result){
        if (result.hasErrors()) {
            return validateFields(result);
        }
        return ResponseEntity.ok(service.save(curso));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id){
        if (result.hasErrors()) {
            return validateFields(result);
        }
        Optional<Curso> o = service.getById(id);
        if (o.isPresent()) {
            Curso cursoDB = o.get();
            cursoDB.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(cursoDB));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<Curso> o = service.getById(id);
        if (o.isPresent()) {
            service.delete(o.get().getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{cursoId}/usuarios/{usuarioId}")
    public ResponseEntity<?> addUsuario(@PathVariable Long cursoId,
                                        @PathVariable Long usuarioId) {

        Optional<Long> o;
        try {
            o = service.addUsuario(usuarioId, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", e.getMessage()));
        }

        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{cursoId}/usuarios/{usuarioId}")
    public ResponseEntity<?> removeUsuario(@PathVariable Long cursoId,
                                           @PathVariable Long usuarioId) {

        Optional<Long> o;
        try {
            o = service.removeUsuario(usuarioId, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", e.getMessage()));
        }

        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/usuarios/{usuarioId}")
    public ResponseEntity<?> deleteUserFromAllCourses(@PathVariable Long usuarioId) {
        service.unlinkUserFromAllCourses(usuarioId);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<?> validateFields(BindingResult result) {
        Map<String,String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
