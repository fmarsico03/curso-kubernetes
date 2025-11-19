package org.fmarsico.springcloud.msvc.usuarios.services;

import org.fmarsico.springcloud.msvc.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> findAll();
    Optional<Usuario> getByID(Long id);
    Usuario save(Usuario usuario);
    void delete(Long id);
    List<Usuario> findAllById(Iterable<Long> ids);
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);

}
