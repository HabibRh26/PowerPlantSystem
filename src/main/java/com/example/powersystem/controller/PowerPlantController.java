package com.example.powersystem.controller;

import com.example.powersystem.Entities.Battery;
import com.example.powersystem.dtos.BatteryRequestDto;
import com.example.powersystem.service.PowerPlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
public class PowerPlantController {

    @Autowired
    PowerPlantService powerPlantService;

    @PostMapping("/batteries")
    public List<Battery> saveBatteryInfo(@RequestBody List<BatteryRequestDto> batteryRequestDtoList) {
        return powerPlantService.handleBatteryData(batteryRequestDtoList);
    }


    @GetMapping ("/batteries")
    public ResponseEntity<Map<String, Object>> getBatteriesByPostcodeRange(@RequestParam String postCodeRange) {
        String[] range = postCodeRange.split("-");
        int minPostcode = Integer.parseInt(range[0]);
        int maxPostcode = Integer.parseInt(range[1]);
        Map<String, Object> result = powerPlantService.getBatteryAnalyticsByPostCodeLimit(minPostcode, maxPostcode);
        return ResponseEntity.ok(result);
    }
}
