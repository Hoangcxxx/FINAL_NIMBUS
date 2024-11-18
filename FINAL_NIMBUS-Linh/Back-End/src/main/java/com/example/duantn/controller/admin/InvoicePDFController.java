package com.example.duantn.controller.admin;

import com.example.duantn.dto.InHoaDon1DTO;
import com.example.duantn.service.InvoicePDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/invoices")
public class InvoicePDFController {

    @Autowired
    private InvoicePDFService invoicePDFService;

}
