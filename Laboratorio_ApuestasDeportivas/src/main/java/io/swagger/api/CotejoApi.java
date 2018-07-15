/**
 * NOTE: This class is auto generated by the swagger code generator program (1.0.13).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.Cotejo;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Api(value = "cotejo", description = "the cotejo API")
@RequestMapping("/cotejo")
public interface CotejoApi {

    @ApiOperation(value = "Creación de cotejos de fútbol", nickname = "agregarCotejo", notes = "Crear un torneo de fútbol", tags={ "adminstradores", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "cotejo creado"),
        @ApiResponse(code = 400, message = "objeto no válido"),
        @ApiResponse(code = 409, message = "el cotejo ya existe") })
    @RequestMapping(value = "/{idCotejo}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<Void> agregarCotejo(@ApiParam(value = "id del cotejo a buscar",required=true) @PathVariable("idCotejo") String idCotejo,@ApiParam(value = "Cotejo a agregar"  )  @Valid @RequestBody Cotejo cotejo);


    @ApiOperation(value = "busca un cotejo", nickname = "buscarCotejo", notes = "Encviando un ID valido devuelve la informacion del cotejo ", response = Object.class, tags={ "administradores","apostadores", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "devuelve el resultado obtenido", response = Object.class),
        @ApiResponse(code = 400, message = "mal entrada de datos") })
    @RequestMapping(value = "/{idCotejo}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Cotejo> buscarCotejo(@ApiParam(value = "id del cotejo a buscar",required=true) @PathVariable("idCotejo") String idCotejo);

}