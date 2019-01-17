package com.cartisan.base.services;

import com.cartisan.base.domains.Vehicle;
import com.cartisan.base.dtos.VehicleDto;
import com.cartisan.base.repositories.VehicleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author colin
 */
@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Cacheable(value="cache:base:services:VehicleService:findVehicles", key="#countryId")
    public List<VehicleDto> findVehicles(Long countryId) {
        List<Vehicle> vehicles;
        if (countryId == null) {
            vehicles = vehicleRepository.findAll();
        } else {
            vehicles = vehicleRepository.findByCountryId(countryId);
        }

        return vehicles.stream().map(VehicleDto::convertFrom).collect(Collectors.toList());
    }

    @CacheEvict(value="cache:base:services:VehicleService:findVehicles", allEntries = true)
    @Transactional(rollbackOn = Exception.class)
    public void addVehicle(VehicleDto vehicleDto) {
        final Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleDto, vehicle);
        vehicleRepository.save(vehicle);
    }

    @CacheEvict(value="cache:base:services:VehicleService:findVehicles", allEntries = true)
    @Transactional(rollbackOn = Exception.class)
    public void editVehicle(long id, VehicleDto vehicleDto) {
        final Vehicle vehicle = vehicleRepository.findById(id).get();
        BeanUtils.copyProperties(vehicleDto, vehicle);
        vehicle.setId(id);
        vehicleRepository.save(vehicle);
    }

    @CacheEvict(value="cache:base:services:VehicleService:findVehicles", allEntries = true)
    @Transactional(rollbackOn = Exception.class)
    public void removeVehicle(long id) {
        vehicleRepository.deleteById(id);
    }
}


