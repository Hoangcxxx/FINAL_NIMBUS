package com.example.duantn.controller;
import com.example.duantn.entity.XacThuc;
import com.example.duantn.TokenUser.Token;
import com.example.duantn.service.XacThucService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/xac-thuc")
public class XacThucController {

    @Autowired
    private XacThucService xacThucService;

    // Create a new verification record
    @PostMapping("/create")
    public ResponseEntity<XacThuc> createXacThuc(@RequestBody XacThuc xacThuc) {
        XacThuc createdXacThuc = xacThucService.createXacThuc(xacThuc);
        return new ResponseEntity<>(createdXacThuc, HttpStatus.CREATED);
    }

    // Get all verification records
    @GetMapping("/all")
    public ResponseEntity<List<XacThuc>> getAllXacThuc() {
        List<XacThuc> xacThucList = xacThucService.getAllXacThuc();
        return new ResponseEntity<>(xacThucList, HttpStatus.OK);
    }

    // Get a verification record by ID
    @GetMapping("/{idXacThuc}")
    public ResponseEntity<XacThuc> getXacThucById(@PathVariable Integer idXacThuc) {
        XacThuc xacThuc = xacThucService.getXacThucById(idXacThuc);
        return new ResponseEntity<>(xacThuc, HttpStatus.OK);
    }

    // Update a verification record by ID
    @PutMapping("/update")
    public ResponseEntity<XacThuc> updateXacThuc(@RequestParam Integer idXacThuc, @RequestBody XacThuc xacThucDetails) {
        XacThuc updatedXacThuc = xacThucService.updateXacThuc(idXacThuc, xacThucDetails);
        return new ResponseEntity<>(updatedXacThuc, HttpStatus.OK);
    }

    // Delete a verification record by ID
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteXacThuc(@RequestParam Integer idXacThuc) {
        xacThucService.deleteXacThuc(idXacThuc);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/xac-nhan")
    public ResponseEntity<Token> xacThuc(@RequestParam String maXacThuc) {

        return new ResponseEntity<>( xacThucService.xacThuc(maXacThuc),HttpStatus.OK);
    }
}
