package com.poppulo.interview.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poppulo.interview.errorhandling.RestErrorCode;
import com.poppulo.interview.errorhandling.exceptions.EmptyTicketIdException;
import com.poppulo.interview.models.LineResource;
import com.poppulo.interview.models.TicketResource;
import com.poppulo.interview.services.TicketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TicketController.class)
@ExtendWith(SpringExtension.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @DisplayName("get a list of all tickets - should return empty collection")
    @Test
    void testGetAllTickets_shouldReturnEmptyCollection() throws Exception {
        // given
        // when
        when(ticketService.getAllTickets()).thenReturn(Collections.emptyList());

        // then
        mockMvc.perform(get("/api/v1/tickets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @DisplayName("get a list of all tickets - should return two tickets")
    @Test
    void testGetAllTickets_shouldReturnTwoTicketsCollection() throws Exception {
        // given
        List<LineResource> linesOfTicket1 = List.of(
                new LineResource.LineBuilder().content("1, 2, 0").build(),
                new LineResource.LineBuilder().content("0, 0, 0").build(),
                new LineResource.LineBuilder().content("0, 2, 1").build()
        );
        TicketResource ticket1 = new TicketResource.TicketBuilder().id("ticket01").checked(false).lines(linesOfTicket1).build();

        List<LineResource> linesOfTicket2 = List.of(
                new LineResource.LineBuilder().content("1, 1, 1").build(),
                new LineResource.LineBuilder().content("2, 2, 0").build(),
                new LineResource.LineBuilder().content("0, 2, 1").build()
        );
        TicketResource ticket2 = new TicketResource.TicketBuilder().id("ticket02").checked(false).lines(linesOfTicket2).build();

        // when
        when(ticketService.getAllTickets()).thenReturn(List.of(ticket1, ticket2));

        // then
        mockMvc.perform(get("/api/v1/tickets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testCreateTicket_shouldReturnOneSpecificCreatedTicket() throws Exception {
        List<LineResource> linesOfTicket1 = List.of(
                new LineResource.LineBuilder().content("1, 2, 0").build(),
                new LineResource.LineBuilder().content("0, 0, 0").build(),
                new LineResource.LineBuilder().content("0, 2, 1").build()
        );
        TicketResource ticket1 = new TicketResource.TicketBuilder().id("ticket01").checked(false).lines(linesOfTicket1).build();

        when(ticketService.createNewTicket(any())).thenReturn(ticket1);

        mockMvc.perform(post("/api/v1/tickets")
                            .content(new ObjectMapper().writeValueAsString(ticket1))
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id", is("ticket01")))
                        .andExpect(jsonPath("$.checked", is(Boolean.FALSE)))
                        .andExpect(jsonPath("$.lines[0].content", is("1, 2, 0")));

    }

    @Test
    void testCreateTicket_shouldReturnErrorDTO() throws Exception {
        List<LineResource> linesOfTicket1 = List.of(
                new LineResource.LineBuilder().content("1, 2, 0").build(),
                new LineResource.LineBuilder().content("0, 0, 0").build(),
                new LineResource.LineBuilder().content("0, 2, 1").build()
        );
        TicketResource ticket1 = new TicketResource.TicketBuilder().id("").checked(false).lines(linesOfTicket1).build();

        when(ticketService.createNewTicket(any())).thenThrow(new EmptyTicketIdException("Ticket id cannot be empty!"));

        mockMvc.perform(post("/api/v1/tickets")
                .content(new ObjectMapper().writeValueAsString(ticket1))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Ticket id cannot be empty!")))
                .andExpect(jsonPath("$.errorCode", is(RestErrorCode.EMPTY_TICKET_ID.toString())))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    void testAmendTicketLines_shouldAcceptLinesListAndReturnUpdatedTicket() throws Exception {
        List<LineResource> linesOfTicket = List.of(
                new LineResource.LineBuilder().content("1, 2, 0").build(),
                new LineResource.LineBuilder().content("0, 0, 0").build(),
                new LineResource.LineBuilder().content("0, 2, 1").build()
        );
        TicketResource ticket1 = new TicketResource.TicketBuilder().id("ticket01").checked(false).lines(linesOfTicket).build();

        when(ticketService.amendTicketLines(any(), eq("ticket01"))).thenReturn(ticket1);

        mockMvc.perform(put("/api/v1/tickets/ticket01/lines")
                            .content(new ObjectMapper().writeValueAsString(linesOfTicket))
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                        .andExpect(status().isResetContent())
                        .andExpect(jsonPath("$.id", is("ticket01")))
                        .andExpect(jsonPath("$.checked", is(Boolean.FALSE)))
                        .andExpect(jsonPath("$.lines[0].content", is("1, 2, 0")));
    }

    @Test
    void testFindTicket_shouldReturnOneTicketWithSpecifiedId() throws Exception {
        List<LineResource> linesOfTicket = List.of(
                new LineResource.LineBuilder().content("1, 2, 0").build(),
                new LineResource.LineBuilder().content("0, 0, 0").build(),
                new LineResource.LineBuilder().content("0, 2, 1").build()
        );
        TicketResource ticket1 = new TicketResource.TicketBuilder().id("ticket01").checked(false).lines(linesOfTicket).build();

        when(ticketService.findOneTicket(eq("ticket01"))).thenReturn(ticket1);

        mockMvc.perform(get("/api/v1/tickets/ticket01"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id", is("ticket01")))
                        .andExpect(jsonPath("$.checked", is(Boolean.FALSE)))
                        .andExpect(jsonPath("$.lines[0].content", is("1, 2, 0")));
    }

    @Test
    void testCheckinTicketStatus_shouldReturnOneTicketWithSpecifiedId() throws Exception {
        List<LineResource> linesOfTicket = List.of(
                new LineResource.LineBuilder().content("1, 2, 0").build(),
                new LineResource.LineBuilder().content("0, 0, 0").build(),
                new LineResource.LineBuilder().content("0, 2, 1").build()
        );
        TicketResource ticket1 = new TicketResource.TicketBuilder().id("ticket01").checked(true).lines(linesOfTicket).build();

        when(ticketService.checkinTicket(eq("ticket01"))).thenReturn(ticket1);

        mockMvc.perform(patch("/api/v1/tickets/ticket01/status"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is("ticket01")))
                .andExpect(jsonPath("$.checked", is(Boolean.TRUE)))
                .andExpect(jsonPath("$.lines[0].content", is("1, 2, 0")));
    }



}
