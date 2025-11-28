package org.fmarsico.springcloud.msvc.cursos.services;

import org.fmarsico.springcloud.msvc.cursos.models.entity.Curso;
import org.fmarsico.springcloud.msvc.cursos.models.entity.CursoUsuario;
import org.fmarsico.springcloud.msvc.cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService{
    @Autowired
    private CursoRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> findAll() {
        return (List<Curso>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> getById(Long id) {
        return repository.findById(id).map(curso -> {
            if (curso.getCursoUsuarios() != null && !curso.getCursoUsuarios().isEmpty()) {
                List<Long> ids = curso.getCursoUsuarios()
                        .stream()
                        .map(CursoUsuario::getUsuarioId)
                        .toList();
                curso.setUsuarios(ids);
            } else {
                curso.setUsuarios(new ArrayList<>());
            }
            return curso;
        });
    }

    /*@Override
    @Transactional(readOnly = true)
    public Optional<Curso> getById(Long id) {
        return repository.findById(id);
    }*/



    @Override
    @Transactional
    public Curso save(Curso curso) {
        return repository.save(curso);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void unlinkUserFromAllCourses(Long usuarioId) {
        repository.deleteByUsuarioId(usuarioId);
    }

    @Override
    @Transactional
    public Optional<Long> addUsuario(Long usuarioId, Long cursoId) {
        Optional<Curso> o = repository.findById(cursoId);
        if(o.isPresent()){

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioId);
            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioId);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Long> removeUsuario(Long usuarioId, Long cursoId) {
        Optional<Curso> o = repository.findById(cursoId);
        if(o.isPresent()){
            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioId);
            curso.removeCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioId);
        }
        return Optional.empty();
    }
}
