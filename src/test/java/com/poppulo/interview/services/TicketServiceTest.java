package com.poppulo.interview.services;

import com.poppulo.interview.entities.Line;
import com.poppulo.interview.entities.Ticket;
import com.poppulo.interview.models.TicketResource;
import com.poppulo.interview.repositories.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@EnableAutoConfiguration
public class TicketServiceTest {

    @MockBean
    TicketRepository ticketRepository;

    @Autowired
    TicketService ticketService;

    @Test
    void testGetAllTickets_shouldReturnTwoTicketResources() {

        when(ticketRepository.findAll()).thenReturn(createTicketEntityListWithTwoElement());

        List<TicketResource> ticketResources = ticketService.getAllTickets();

        assertTrue(ticketResources.size() == 2, "The returned ticket resources should have 2 elements");

    }

    private List<Ticket> createTicketEntityListWithTwoElement() {
        Ticket ticket1 = new Ticket();

        Line line1 = new Line();
        line1.setId(1L);
        line1.setContent("1, 2, 0");
        line1.setResult(1);
        line1.setTicket(ticket1);

        Line line2 = new Line();
        line2.setId(2L);
        line2.setContent("0, 0, 0");
        line2.setResult(5);
        line2.setTicket(ticket1);

        Line line3 = new Line();
        line3.setId(3L);
        line3.setContent("2, 2, 1");
        line3.setResult(0);
        line3.setTicket(ticket1);

        ticket1.setId(1L);
        ticket1.setTicketIdentifier("ticket01");
        ticket1.setChecked(false);
        ticket1.setLines(List.of(line1, line2, line3));

        Ticket ticket2 = new Ticket();

        Line line4 = new Line();
        line4.setId(4L);
        line4.setContent("1, 1, 1");
        line4.setResult(5);
        line4.setTicket(ticket2);

        Line line5 = new Line();
        line5.setId(5L);
        line5.setContent("2, 0, 0");
        line5.setResult(10);
        line5.setTicket(ticket2);

        Line line6 = new Line();
        line6.setId(6L);
        line6.setContent("1, 1, 0");
        line6.setResult(10);
        line6.setTicket(ticket2);

        ticket2.setId(2L);
        ticket2.setTicketIdentifier("ticket02");
        ticket2.setChecked(false);
        ticket2.setLines(List.of(line4, line5, line6));

        return List.of(ticket1, ticket2);
    }

    @Test
    void testCreateNewTicket_shouldReturnTheSavedResource() {
        // TODO
    }

    @Test
    void testAmendTicketLines_shouldReturnAmendedResource() {
        // TODO
    }

    @Test
    void testFindOneTicket_shouldReturnResourceWithTheSpecifiedId() {
        // TODO
    }

    @Test
    void testCheckinTicket_shouldCheckTheCheckedStatus(){
        // TODO
    }


}
