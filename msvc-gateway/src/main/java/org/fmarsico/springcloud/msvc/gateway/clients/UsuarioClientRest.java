package org.fmarsico.springcloud.msvc.gateway.clients;

import org.fmarsico.springcloud.msvc.gateway.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "msvc-usuarios", url = "${msvc.usuarios.url}")
public interface UsuarioClientRest {

    @GetMapping("/usuarios")
    Map<String, List<Usuario>> findAll();

    @GetMapping(value = "/usuarios", params = "ids")
    List<Usuario> findAllByIds(@RequestParam List<Long> ids);

    @GetMapping("/usuarios/{id}")
    Usuario getById(@PathVariable("id") Long id);

    @PostMapping("/usuarios")
    Usuario create(@RequestBody Usuario usuario);

    @PutMapping("/usuarios/{id}")
    Usuario update(@PathVariable("id") Long id, @RequestBody Usuario usuario);

    @DeleteMapping("/usuarios/{id}")
    void delete(@PathVariable("id") Long id);
}
