package org.fmarsico.springcloud.msvc.cursos.clients;

import org.fmarsico.springcloud.msvc.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-usuarios", url = "${msvc.usuarios.url}/usuarios")
public interface UsuarioClientRest {
    @GetMapping("/{id}")
    Usuario detail(@PathVariable Long id);

    @PostMapping("/")
    Usuario create(@RequestBody Usuario usuario);

    @GetMapping
    List<Usuario> findAllByIds(@RequestParam Iterable<Long> ids);
}
