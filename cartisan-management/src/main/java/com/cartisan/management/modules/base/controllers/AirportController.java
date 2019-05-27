package com.cartisan.management.modules.base.controllers;

import com.cartisan.common.dtos.PageResult;
import com.cartisan.common.responses.GenericResponse;
import com.cartisan.management.modules.base.dtos.AirportDto;
import com.cartisan.management.modules.base.dtos.CityDto;
import com.cartisan.management.modules.base.clients.AirportClient;
import com.cartisan.management.modules.base.clients.CityClient;
import com.cartisan.management.modules.base.clients.CountryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.cartisan.common.responses.GenericResponse.success;
import static java.util.stream.Collectors.toList;

/**
 * @author colin
 */
@RestController
@RequestMapping("/airport")
public class AirportController {
    @Autowired
    private AirportClient airportClient;

    @Autowired
    private CityClient cityClient;

    @Autowired
    private CountryClient countryClient;

    @GetMapping
    public List<AirportDto> findAirports(Long cityId) {
        return airportClient.findAirport(cityId);
    }

    @GetMapping("/search/{currentPage}/{pageSize}")
    public GenericResponse<PageResult<AirportDto>> searchAirports(
            @RequestParam(required = false) Long continentId,
            @RequestParam(required = false) Long countryId,
            @RequestParam(required = false) Long cityId,
            @PathVariable Integer currentPage,
            @PathVariable Integer pageSize) {

        Long[] cityIds = null;
        if (cityId != null) {
            cityIds = new Long[]{cityId};
        } else if (countryId != null) {
            final List<CityDto> cities = cityClient.findCities(countryId);
            cityIds = cities.stream().map(CityDto::getId).collect(toList()).toArray(new Long[cities.size()]);
        } else if (continentId != null) {
            final List<CityDto> cities = countryClient.findCountries(continentId)
                    .stream().flatMap(countryDto -> cityClient.findCities(countryDto.getId()).stream())
                    .collect(Collectors.toList());
            cityIds = cities.stream().map(CityDto::getId).collect(toList()).toArray(new Long[cities.size()]);
        }

        return success(airportClient.searchAirports(cityIds, currentPage, pageSize));
    }
}