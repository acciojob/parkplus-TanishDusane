package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    UserRepository userRepository3;

    @Autowired
    SpotRepository spotRepository3;

    @Autowired
    ReservationRepository reservationRepository3;

    @Autowired
    ParkingLotRepository parkingLotRepository3;

    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        User user;
        ParkingLot parkingLot;

        try {
            user = userRepository3.findById(userId).get();
            parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        } catch (Exception e) {
            throw new Exception("Cannot make reservation");
        }

        List<Spot> spotList = parkingLot.getSpotList();
        Spot spot = null;
        int minCost = Integer.MAX_VALUE;

        for(Spot spot1 : spotList){
            int wheels = 0;

            if(spot1.getSpotType() == SpotType.FOUR_WHEELER){
                wheels = 4;
            } else if (spot1.getSpotType() == SpotType.TWO_WHEELER) {
                wheels = 2;
            } else {
                wheels = 24;
            }

            if(!spot1.getOccupied() && numberOfWheels <= wheels && (timeInHours * spot1.getPricePerHour() < minCost)) {
                spot = spot1;
                minCost = timeInHours * spot1.getPricePerHour();
            }
        }

        if(spot == null){
            throw new Exception("Cannot make reservation");
        }

        Reservation reservation = new Reservation();
        reservation.setNumberOfHours(timeInHours);
        reservation.setSpot(spot);
        reservation.setUser(user);

        List<Reservation> reservationList = user.getReservationList();
        reservationList.add(reservation);
        user.setReservationList(reservationList);

        List<Reservation> reservationList1 = spot.getReservationList();
        reservationList1.add(reservation);
        spot.setReservationList(reservationList1);

        spot.setOccupied(true);
        userRepository3.save(user);
        spotRepository3.save(spot);

        return reservation;
    }
}
