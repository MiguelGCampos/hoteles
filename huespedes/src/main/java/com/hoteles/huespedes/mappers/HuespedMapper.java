package com.hoteles.huespedes.mappers;

import com.hoteles.commons.dto.huespedes.HuespedRequest;
import com.hoteles.commons.dto.huespedes.HuespedResponse;
import com.hoteles.huespedes.entities.Huesped;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HuespedMapper {

    HuespedResponse entidadAResponse(Huesped huesped);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estadoRegistro", ignore = true)
    @Mapping(target = "tipoDocumento", ignore = true) // Se manejará en el servicio
    Huesped requestAEntidad(HuespedRequest huespedRequest);
}
