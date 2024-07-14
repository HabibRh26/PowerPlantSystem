package com.example.powersystem.service;


import com.example.powersystem.Entities.Battery;
import com.example.powersystem.dtos.BatteryRequestDto;
import com.example.powersystem.repositories.PowerSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PowerPlantService {

    @Autowired
    private PowerSystemRepository powerSystemRepository;
    public List<Battery> handleBatteryData(List<BatteryRequestDto> batteryRequestDtoList) {
        List<Battery> batteries = batteryRequestDtoList.stream()
                .map(this::convertToBatteryEntity)
                .collect(Collectors.toList());

        return powerSystemRepository.saveAll(batteries);
    }

    private Battery convertToBatteryEntity(BatteryRequestDto dto) {
        Battery battery = new Battery();
        battery.setName(dto.getName());
        battery.setPostCode(Integer.valueOf(dto.getPostCode()));
        battery.setWattCapacity(dto.getWattCapacity());
        return battery;
    }

    public Map<String, Object> getBatteryAnalyticsByPostCodeLimit(int minPostcode, int maxPostcode) {
        // Filter and sort batteries by postcode range
        List<Battery> batteries = powerSystemRepository.findAll().stream()
                .filter(battery -> isWithinPostcodeRange(battery, minPostcode, maxPostcode))
                .sorted(Comparator.comparing(Battery::getName))
                .collect(Collectors.toList());

        if (batteries.isEmpty()) {
            // Prepare a message for no data found
            Map<String, Object> result = new HashMap<>();
            result.put("message", "No data found for the given postcode range");
            return result;
        }

        // Extract battery names
        List<String> batteryNames = batteries.stream()
                .map(Battery::getName)
                .collect(Collectors.toList());

        // Calculate total and average watt capacity
        double totalWattCapacity = calculateTotalWattCapacity(batteries);
        double averageWattCapacity = calculateAverageWattCapacity(batteries, totalWattCapacity);

        // Prepare result map
        Map<String, Object> result = new HashMap<>();
        result.put("batteryNames", batteryNames);
        result.put("statistics", createStatisticsMap(totalWattCapacity, averageWattCapacity));

        return result;
    }

    private boolean isWithinPostcodeRange(Battery battery, int minPostcode, int maxPostcode) {
        int postCode = Integer.parseInt(String.valueOf(battery.getPostCode()));
        return postCode >= minPostcode && postCode <= maxPostcode;
    }

    private double calculateTotalWattCapacity(List<Battery> batteries) {
        return batteries.stream()
                .mapToDouble(Battery::getWattCapacity)
                .sum();
    }

    private double calculateAverageWattCapacity(List<Battery> batteries, double totalWattCapacity) {
        return batteries.isEmpty() ? 0 : totalWattCapacity / batteries.size();
    }

    private Map<String, Object> createStatisticsMap(double totalWattCapacity, double averageWattCapacity) {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalWattCapacity", totalWattCapacity);
        statistics.put("averageWattCapacity", averageWattCapacity);
        return statistics;
    }
}
