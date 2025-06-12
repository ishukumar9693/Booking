package com.booking.recruitment.hotel.service.impl;

import com.booking.recruitment.hotel.Haversine;
import com.booking.recruitment.hotel.exception.BadRequestException;
import com.booking.recruitment.hotel.model.City;
import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.repository.CityRepository;
import com.booking.recruitment.hotel.repository.HotelRepository;
import com.booking.recruitment.hotel.service.CityService;
import com.booking.recruitment.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class DefaultHotelService implements HotelService {
  private final HotelRepository hotelRepository;

  @Autowired
  DefaultHotelService(HotelRepository hotelRepository) {
    this.hotelRepository = hotelRepository;
  }

  @Autowired
  private  CityRepository cityRepository;

  @Override
  public List<Hotel> getAllHotels() {
    return hotelRepository.findAll();
  }

  @Override
  public List<Hotel> getHotelsByCity(Long cityId) {
    return hotelRepository.findAll().stream()
        .filter((hotel) -> cityId.equals(hotel.getCity().getId()))
        .collect(Collectors.toList());
  }

  @Override
  public Hotel createNewHotel(Hotel hotel) {
    if (hotel.getId() != null) {
      throw new BadRequestException("The ID must not be provided when creating a new Hotel");
    }

    return hotelRepository.save(hotel);
  }

  @Override
  public void deleteById(Long id) {
    hotelRepository.deleteById(id);
  }

  public List<Hotel> getTop3HotelClosestToCityCentre(Long cityId) {
    List<Hotel>hotelList=hotelRepository.findAll();
    Optional<City> city= cityRepository.findById(cityId);
    List<Hotel>hotels= hotelList.stream().sorted(Comparator.comparingDouble(hotel->
            Haversine.haversine(city.get().getCityCentreLatitude(), city.get().getCityCentreLatitude(),hotel.getLatitude(),hotel.getLongitude())
    )).limit(3).collect(Collectors.toList());
    return hotels;

  }
}
