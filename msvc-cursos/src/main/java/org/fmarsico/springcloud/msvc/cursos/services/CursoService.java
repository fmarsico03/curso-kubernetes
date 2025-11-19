package org.fmarsico.springcloud.msvc.cursos.services;

import org.fmarsico.springcloud.msvc.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> findAll();
    Optional<Curso> getById(Long id);
    Curso save(Curso curso);
    void delete(Long id);
    void unlinkUserFromAllCourses(Long usuarioId);
    Optional<Long> addUsuario(Long usuarioId, Long cursoId);
    Optional<Long> removeUsuario(Long usuarioId, Long cursoId);
}
