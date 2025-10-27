package com.webserver.evrentalsystem.scheduler;

import com.webserver.evrentalsystem.entity.Reservation;
import com.webserver.evrentalsystem.entity.ReservationStatus;
import com.webserver.evrentalsystem.entity.Vehicle;
import com.webserver.evrentalsystem.entity.VehicleStatus;
import com.webserver.evrentalsystem.repository.ReservationRepository;
import com.webserver.evrentalsystem.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
class ReservationScheduler {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    // Run every 5 minutes to check for expired reservations
    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void expireOldReservations() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.minusHours(1);

        List<Reservation> expiredReservations = reservationRepository.findAllByStatusAndCreatedAtBefore(
                ReservationStatus.PENDING,
                expireTime
        );

        for (Reservation reservation : expiredReservations) {
            reservation.setStatus(ReservationStatus.EXPIRED);

            Vehicle vehicle = reservation.getVehicle();
            vehicle.setStatus(VehicleStatus.AVAILABLE);
        }

        if (!expiredReservations.isEmpty()) {
            reservationRepository.saveAll(expiredReservations);
            vehicleRepository.saveAll(
                    expiredReservations.stream().map(Reservation::getVehicle).toList()
            );
            System.out.println("Đã hết hạn " + expiredReservations.size() + " reservation sau 1 giờ không check-in.");
        }
    }
}