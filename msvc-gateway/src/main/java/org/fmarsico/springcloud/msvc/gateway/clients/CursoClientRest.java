package org.fmarsico.springcloud.msvc.gateway.clients;

import org.fmarsico.springcloud.msvc.gateway.models.Curso;
import org.fmarsico.springcloud.msvc.gateway.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-cursos", url = "${msvc.cursos.url}")
public interface CursoClientRest {

    @GetMapping("/cursos")
    List<Curso> getCursos();

    @GetMapping("/cursos/{id}")
    Curso getCurso(@PathVariable("id") Long id);

    @PostMapping("/cursos")
    Curso createCurso(@RequestBody Curso curso);

    @PutMapping("/cursos/{id}")
    Curso updateCurso(@PathVariable("id") Long id, @RequestBody Curso curso);

    @DeleteMapping("/cursos/{id}")
    void deleteCurso(@PathVariable("id") Long id);

    @PutMapping("/cursos/{cursoId}/usuarios/")
    Usuario addUsuarioToCurso(@RequestBody Usuario usuario,
                              @PathVariable("cursoId") Long cursoId);

    @PutMapping("/cursos/{cursoId}/usuarios/{usuarioId}")
    void addUsuarioToCurso(@PathVariable("cursoId") Long cursoId,
                           @PathVariable("usuarioId") Long usuarioId);

    @DeleteMapping("/cursos/{cursoId}/usuarios/{usuarioId}")
    void removeUsuarioFromCurso(@PathVariable("cursoId") Long cursoId,
                                @PathVariable("usuarioId") Long usuarioId);

    @DeleteMapping("/cursos/usuarios/{usuarioId}")
    void deleteUserFromAllCourses(@PathVariable("usuarioId") Long usuarioId);
}
