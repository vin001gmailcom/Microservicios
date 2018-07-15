/**
 * NOTE: This class is auto generated by the swagger code generator program (1.0.13).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.model.Cotejo;
import io.swagger.model.Torneo;

@Api(value = "torneo", description = "the torneo API")
@RequestMapping("/torneo")
public interface TorneoApi {

    @ApiOperation(value = "Creación de torneos de fútbol", nickname = "agregarTorneo", notes = "Crear un torneo de fútbol", tags={ "adminstradores", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "torneo creado"),
        @ApiResponse(code = 400, message = "objeto no válido"),
        @ApiResponse(code = 409, message = "an existing item already exists") })
    @RequestMapping(value = "/{idTorneo}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<Void> agregarTorneo(@ApiParam(value = "id del torneo a buscar",required=true) @PathVariable("idTorneo") String idTorneo,@ApiParam(value = "Torneo a agregar"  )  @Valid @RequestBody Torneo torneo);


    @ApiOperation(value = "busca un torneo", nickname = "buscarTorneo", notes = "Encviando un ID valido devuelve la informacion del torneo ", response = Object.class, tags={ "administradores","apostadores", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "devuelve el resultado obtenido", response = Object.class),
        @ApiResponse(code = 400, message = "mal entrada de datos") })
    @RequestMapping(value = "/{idTorneo}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.GET)
    ResponseEntity<Torneo> buscarTorneo(@ApiParam(value = "id del torneo a buscar",required=true) @PathVariable("idTorneo") String idTorneo);

    @ApiOperation(value = "busca un listado de cotejos dado un torneo", nickname = "listarCotejos", notes = " ", response = Object.class, tags={ "administradores","apostadores", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "devolver el resultado obtenido", response = Object.class),
            @ApiResponse(code = 400, message = "parametro incorrecto") })
    @RequestMapping(value = "/{idTorneo}/cotejos",
            produces = { "application/json" },
            method = RequestMethod.GET)
    @ResponseBody List<Cotejo>  listarCotejos(@ApiParam(value = "id del torneo a buscar",required=true) @PathVariable("idTorneo") String idTorneo);
    
}