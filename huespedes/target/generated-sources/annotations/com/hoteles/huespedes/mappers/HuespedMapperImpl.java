package com.hoteles.huespedes.mappers;

import com.hoteles.commons.dto.huespedes.HuespedRequest;
import com.hoteles.commons.dto.huespedes.HuespedResponse;
import com.hoteles.commons.enums.EstadoRegistro;
import com.hoteles.commons.enums.TipoDocumento;
import com.hoteles.huespedes.entities.Huesped;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-31T01:56:45-0600",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Oracle Corporation)"
)
@Component
public class HuespedMapperImpl implements HuespedMapper {

    @Override
    public HuespedResponse entidadAResponse(Huesped huesped) {
        if ( huesped == null ) {
            return null;
        }

        Long id = null;
        String nombre = null;
        String apellidoPaterno = null;
        String apellidoMaterno = null;
        String email = null;
        String telefono = null;
        TipoDocumento tipoDocumento = null;
        String nacionalidad = null;
        EstadoRegistro estadoRegistro = null;

        id = huesped.getId();
        nombre = huesped.getNombre();
        apellidoPaterno = huesped.getApellidoPaterno();
        apellidoMaterno = huesped.getApellidoMaterno();
        email = huesped.getEmail();
        telefono = huesped.getTelefono();
        tipoDocumento = huesped.getTipoDocumento();
        nacionalidad = huesped.getNacionalidad();
        estadoRegistro = huesped.getEstadoRegistro();

        HuespedResponse huespedResponse = new HuespedResponse( id, nombre, apellidoPaterno, apellidoMaterno, email, telefono, tipoDocumento, nacionalidad, estadoRegistro );

        return huespedResponse;
    }

    @Override
    public Huesped requestAEntidad(HuespedRequest huespedRequest) {
        if ( huespedRequest == null ) {
            return null;
        }

        Huesped.HuespedBuilder huesped = Huesped.builder();

        huesped.nombre( huespedRequest.nombre() );
        huesped.apellidoPaterno( huespedRequest.apellidoPaterno() );
        huesped.apellidoMaterno( huespedRequest.apellidoMaterno() );
        huesped.email( huespedRequest.email() );
        huesped.telefono( huespedRequest.telefono() );
        huesped.nacionalidad( huespedRequest.nacionalidad() );

        return huesped.build();
    }
}
