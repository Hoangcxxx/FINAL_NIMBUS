package com.example.duantn.service;

import com.example.duantn.entity.VaiTro;
import com.example.duantn.repository.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VaiTroService {

    @Autowired
    private VaiTroRepository vaiTroRepository;

    // Create a new role
    public VaiTro createVaiTro(VaiTro vaiTro) {
        vaiTro.setNgayTao(new Date());  // Set creation date
        vaiTro.setNgayCapNhat(new Date());  // Set update date
        return vaiTroRepository.save(vaiTro); // Save the new role
    }

    // Get all roles
    public List<VaiTro> getAllVaiTro() {
        return vaiTroRepository.findAll(); // Return all roles from the database
    }

    // Get a role by ID
    public VaiTro getVaiTroById(Integer idVaiTro) {
        return vaiTroRepository.findById(idVaiTro)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + idVaiTro));
    }

    // Update a role
    public VaiTro updateVaiTro(Integer idVaiTro, VaiTro vaiTroDetails) {
        VaiTro existingVaiTro = vaiTroRepository.findById(idVaiTro)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + idVaiTro));

        // Update the fields
        existingVaiTro.setTen(vaiTroDetails.getTen());
        existingVaiTro.setMoTa(vaiTroDetails.getMoTa());
        existingVaiTro.setNgayCapNhat(new Date()); // Set the update date

        return vaiTroRepository.save(existingVaiTro); // Save the updated role
    }

    // Delete a role by ID
    public void deleteVaiTro(Integer idVaiTro) {
        VaiTro vaiTro = vaiTroRepository.findById(idVaiTro)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + idVaiTro));

        vaiTroRepository.delete(vaiTro); // Delete the role
    }
}
