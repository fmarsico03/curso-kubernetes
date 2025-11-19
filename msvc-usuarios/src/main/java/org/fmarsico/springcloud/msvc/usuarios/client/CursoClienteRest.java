package org.fmarsico.springcloud.msvc.usuarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-cursos", url = "${msvc.cursos.url}/cursos")
public interface CursoClienteRest {

    @DeleteMapping("/usuarios/{usuarioId}")
    void deleteUserFromAllCourses(@PathVariable Long usuarioId);
}
