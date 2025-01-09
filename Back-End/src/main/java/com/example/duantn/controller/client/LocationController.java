package com.example.duantn.controller.client;

import com.example.duantn.service.TestDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nguoi_dung/test")
@CrossOrigin(origins = "http://127.0.0.1:5502") // CORS configuration for frontend
public class LocationController {

    private final TestDemoService testDemoService;

    @Autowired
    public LocationController(TestDemoService testDemoService) {
        this.testDemoService = testDemoService;
    }


    // API to get list of cities
    @GetMapping("/cities")
    public ResponseEntity<?> getCities() {
        try {
            List<Map<String, Object>> cities = testDemoService.getCities();
            return ResponseEntity.ok(cities);  // Return cities
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching cities");
        }
    }

    // API to get list of districts by city code
    @GetMapping("/districts/{cityCode}")
    public ResponseEntity<?> getDistricts(@PathVariable String cityCode) {
        try {
            List<Map<String, Object>> districts = testDemoService.getDistricts(cityCode);
            return ResponseEntity.ok(districts);  // Return districts based on city
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching districts");
        }
    }

    // API to get list of wards by district code
    @GetMapping("/wards/{districtCode}")
    public ResponseEntity<?> getWards(@PathVariable String districtCode) {
        try {
            List<Map<String, Object>> wards = testDemoService.getWards(districtCode);
            return ResponseEntity.ok(wards);  // Return wards based on district
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching wards");
        }
    }

    // API to save city, district, and ward information to the database
    @PostMapping("/save-location")
    public ResponseEntity<String> saveLocationToDB(
            @RequestParam Integer userId,
            @RequestParam String cityCode,
            @RequestParam String districtCode,
            @RequestParam String wardCode) {
        try {
            // Save the location information to the database
            testDemoService.saveCityDistrictWardToDB(userId, cityCode, districtCode, wardCode);
            return ResponseEntity.ok("Location saved successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error saving location");
        }
    }
}
