package com.hoteles.habitaciones.controller;

import com.hoteles.commons.controller.CommonController;
import com.hoteles.commons.dto.habitaciones.HabitacionRequest;
import com.hoteles.commons.dto.habitaciones.HabitacionResponse;
import com.hoteles.habitaciones.service.HabitacionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class HabitacionController extends CommonController<HabitacionRequest, HabitacionResponse, HabitacionService> {
    public HabitacionController(HabitacionService service){super(service);}
}
