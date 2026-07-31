package com.hoteles.huespedes.services;

import com.hoteles.commons.dto.huespedes.HuespedRequest;
import com.hoteles.commons.dto.huespedes.HuespedResponse;
import com.hoteles.commons.service.CrudService;

public interface HuespedService extends CrudService<HuespedRequest, HuespedResponse> {

    HuespedResponse obtenerHuespedActivoPorId(Long id);

    HuespedResponse obtenerHuespedSinEstadoPorId(Long id);

}
