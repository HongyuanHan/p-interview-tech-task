package com.poppulo.interview.services;

import com.poppulo.interview.models.LineResource;
import com.poppulo.interview.models.TicketResource;

import java.util.List;

public interface TicketService {

    List<TicketResource> getAllTickets(int page, int size);

    List<TicketResource> getAllTickets();

    TicketResource createNewTicket(TicketResource ticketResource);

    TicketResource amendTicketLines(List<LineResource> lines, String id);

    TicketResource findOneTicket(String id);

    TicketResource checkinTicket(String id);

    public boolean isOneSpecifiedTicketExisting(String id);
}
