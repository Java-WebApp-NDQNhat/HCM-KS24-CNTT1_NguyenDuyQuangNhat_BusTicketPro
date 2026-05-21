package com.re.trans_route.service;

import com.re.trans_route.repository.SeatRepository;
import com.re.trans_route.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class TicketTaskService {

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    public TicketTaskService(TicketRepository ticketRepository, SeatRepository seatRepository) {
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
    }

    // cron = "0 */10 * * * *": Giây thứ 0, mỗi 10 phút, mọi giờ, mọi ngày
    @Scheduled(cron = "0 */10 * * * *")
    @Transactional
    public void cancelExpiredTickets() {
//        tinh' thoi` diem? 30 phut' truoc'
        LocalDateTime thirtyMinPrev = LocalDateTime.now().minusMinutes(30);

        System.out.println("\n=====\nQuét vé quá hạn: " + LocalDateTime.now());

//         giai? phong' ghe' cho~ vé' qua' han.
        seatRepository.releaseSeatByExpiredTicket(thirtyMinPrev);

//         Tim` va` cap. nhat. tat' ca? ve' PENDING đa~ tao. truoc' 30 phut'
//         va` chuyen? trang. thai' sang CANCELLED
        int updatedCount = ticketRepository.cancelExpiredTickets(thirtyMinPrev);

        if (updatedCount > 0) {
            System.out.println("Đã tự động hủy " + updatedCount + " vé quá hạn.!!\n=====\n");
        }
    }

    @Scheduled(cron = "0 0 0 * * *") // Cap. nhat. trang. thai' cho ve' het han. 0:00 daily
    @Transactional
    public void resetDailyTicketCounts() {
        System.out.println("\n=====\nUpdate vé quá hạn hàng ngày: " + LocalDateTime.now());

        int updatedCount = ticketRepository.expirePastTickets();

        if (updatedCount > 0) {
            System.out.println("Đã cập nhật " + updatedCount + " vé quá hạn sang EXPIRED.!!\n=====\n");
        }
    }
}