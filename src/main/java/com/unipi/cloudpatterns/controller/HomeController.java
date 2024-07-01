package com.unipi.cloudpatterns.controller;

import com.unipi.cloudpatterns.service.AzureBlobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    private final AzureBlobService azureBlobService;

    @Autowired
    public HomeController(AzureBlobService azureBlobService) {
        this.azureBlobService = azureBlobService;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = {"/", "/{path:[^\\.]*}"})
    public String redirect() {
        return "forward:/index.html";
        // return "redirect:" + azureBlobService.getBlobUrl("index.html");
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/static/{folder}/{filename:.+}")
    public String getStaticFile(@PathVariable String folder, @PathVariable String filename) {
        return "redirect:" + azureBlobService.getBlobUrl("static/" + folder + "/" + filename);
    }
}

