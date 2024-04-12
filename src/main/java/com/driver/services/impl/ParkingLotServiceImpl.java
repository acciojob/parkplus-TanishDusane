package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {

    @Autowired
    private ParkingLotRepository parkingLotRepository1;

    @Autowired
    private SpotRepository spotRepository1;

    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);

        return parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        Optional<ParkingLot> parkingLotOptional = parkingLotRepository1.findById(parkingLotId);
        if (!parkingLotOptional.isPresent()) {
            // Handle the case where no parking lot with the given ID exists
            // You can throw an exception, return null, or handle it in another way based on your requirement
            throw new IllegalArgumentException("Parking lot with ID " + parkingLotId + " not found");
        }
        ParkingLot parkingLot = parkingLotOptional.get();

        Spot spot = new Spot();
        if(numberOfWheels <= 2){
            spot.setSpotType(SpotType.TWO_WHEELER);
        } else if (numberOfWheels <= 4) {
            spot.setSpotType(SpotType.FOUR_WHEELER);
        } else {
            spot.setSpotType(SpotType.OTHERS);
        }

        spot.setPricePerHour(pricePerHour);
        spot.setParkingLot(parkingLot);
        spot.setOccupied(Boolean.FALSE);

        List<Spot> spotList = parkingLot.getSpotList();

        spotList.add(spot);
        parkingLot.setSpotList(spotList);
        parkingLotRepository1.save(parkingLot);

        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Optional<ParkingLot> parkingLotOptional = parkingLotRepository1.findById(parkingLotId);
        ParkingLot parkingLot = parkingLotOptional.get();

        List<Spot> spotList = parkingLot.getSpotList();
        Spot spot = null;

        for(Spot spot1 : spotList) {
            if(spot1.getId() == spotId) {
                spot1.setPricePerHour(pricePerHour);
                spot = spot1;

            }
        }

        parkingLot.setSpotList(spotList);
        parkingLotRepository1.save(parkingLot);

        Spot spot2 = spotRepository1.save(spot);

        return spot2;

//        Optional<ParkingLot> parkingLotOptional = parkingLotRepository1.findById(parkingLotId);
//
//        if(parkingLotOptional.isPresent()){
//            ParkingLot parkingLot = parkingLotOptional.get();
//            List<Spot> spotList = parkingLot.getSpotList();
//
//            for (Spot spot : spotList){
//                if(spot.getId() == spotId){
//                    spot.setPricePerHour(pricePerHour);
//                    break;
//                }
//            }
//            parkingLot = parkingLotRepository1.save(parkingLot);
//
//            for(Spot spot : parkingLot.getSpotList()) {
//                if(spot.getId() == spotId){
//                    return spot;
//                }
//            }
//        }
//        return null;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}