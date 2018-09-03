package com.rafael.cursomc.cursomc.resources.utils;

import com.rafael.cursomc.cursomc.services.CidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CidadeResource {

    @Autowired
    CidadeService cidadeService;

}
