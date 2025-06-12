package com.booking.recruitment.hotel.controller;

import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class HotelController {
  private final HotelService hotelService;

  @Autowired
  public HotelController(HotelService hotelService) {
    this.hotelService = hotelService;
  }

  @GetMapping("/hotel")
  @ResponseStatus(HttpStatus.OK)
  public List<Hotel> getAllHotels() {
    return hotelService.getAllHotels();
  }

  @PostMapping("/hotel")
  @ResponseStatus(HttpStatus.CREATED)
  public Hotel createHotel(@RequestBody Hotel hotel) {
    return hotelService.createNewHotel(hotel);
  }

  @GetMapping("/hotel/{id}")
  ResponseEntity<Hotel> getHotelById(@PathVariable Long id){
    List<Hotel>hotelList=hotelService.getAllHotels();
    for(Hotel hotel:hotelList){
      if(hotel.getId()==id){
        return ResponseEntity.status(HttpStatus.OK).body(hotel);
      }
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }

  @DeleteMapping("/hotel/{id}")
  ResponseEntity<String> deleteHotelById(@PathVariable Long id)  {
    List<Hotel>hotelList=hotelService.getAllHotels();
    for(Hotel hotel:hotelList){
      if(hotel.getId()==id){
        hotelService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully Deleted");
      }
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hotel Not Found");
  }

  @GetMapping("/search/{cityId}")
  ResponseEntity<List<Hotel>>getTopThreeHotels(@PathVariable Long cityId,@RequestParam(name = "sortedBy") String sortedBy){
    if("distance".equals(sortedBy)){
     List<Hotel>hotels=hotelService.getTop3HotelClosestToCityCentre(cityId);
     if(hotels.size()==3){
       return ResponseEntity.status(HttpStatus.OK).body(hotels);
     }
     else
     {
       return  ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(hotels);
     }
    }
    else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }


}
