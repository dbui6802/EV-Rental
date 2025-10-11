package com.webserver.evrentalsystem.scheduler;

import com.webserver.evrentalsystem.entity.Reservation;
import com.webserver.evrentalsystem.entity.ReservationStatus;
import com.webserver.evrentalsystem.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReservationScheduler {

    @Autowired
    private ReservationRepository reservationRepository;

    // Run every 5 minutes to check for expired reservations
    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void expireOldReservations() {
        LocalDateTime now = LocalDateTime.now();

        List<Reservation> expiredReservations = reservationRepository.findAllByStatusAndReservedEndTimeBefore(
                ReservationStatus.PENDING,
                now
        );

        for (Reservation reservation : expiredReservations) {
            reservation.setStatus(ReservationStatus.EXPIRED);
        }

        if (!expiredReservations.isEmpty()) {
            reservationRepository.saveAll(expiredReservations);
            System.out.println("Đã hết hạn " + expiredReservations.size() + " reservation.");
        }
    }
}
