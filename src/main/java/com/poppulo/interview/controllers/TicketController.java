package com.poppulo.interview.controllers;

import com.google.common.base.Strings;
import com.poppulo.interview.errorhandling.RestErrorDTO;
import com.poppulo.interview.errorhandling.exceptions.EmptyLineListException;
import com.poppulo.interview.errorhandling.exceptions.EmptyTicketIdException;
import com.poppulo.interview.errorhandling.exceptions.LineContentInvalidException;
import com.poppulo.interview.errorhandling.exceptions.UniqueTicketIdConstraintException;
import com.poppulo.interview.models.LineResource;
import com.poppulo.interview.models.TicketResource;
import com.poppulo.interview.services.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.regex.Pattern;

import static com.poppulo.interview.utils.Constants.LINE_CONTENT_VALIDATION_REGEX;
import static com.poppulo.interview.utils.Constants.NUMBER_RANGE_INVALID_MSG;

@RestController
@RequestMapping("/api/v1")
@Tag(name="Tickets", description = "REST API for lottery tickets information")
public class TicketController {

    private TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Operation(summary = "Return all lottery tickets", description = "Returns all lottery tickets, else empty collection", tags={"Tickets"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "For successful fetch.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TicketResource.class))))
    })
    @GetMapping(path ="/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    List<TicketResource> getAllTickets(@RequestParam (required = false, defaultValue = "-1") @Parameter(description = "Page number for pagination, optional") int page,
                                       @RequestParam (required = false, defaultValue = "-1") @Parameter(description = "Page size for pagination, optional") int size) {
        if (page < 0 || size <= 0) {
            return ticketService.getAllTickets();
        } else {
            return ticketService.getAllTickets(page, size);
        }

    }


    @Transactional
    @Operation(summary = "Create a lottery ticket", description = "Create a lottery ticket and return the created instance, else error info returned", tags = {"Tickets"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "For successful creation",
                    content = @Content(schema = @Schema(implementation = TicketResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = RestErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal unknown exception",
                    content = @Content(schema = @Schema(implementation = RestErrorDTO.class))),
    })
    @PostMapping(path = "/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    TicketResource createTicket(@RequestBody @Valid TicketResource ticketResource) {
        validateTicketResource(ticketResource);
        boolean isExisting = validateUniqueTicketConstraint(ticketResource);
        if (isExisting) {
            throw new UniqueTicketIdConstraintException("The ticket id of %s is existing, it is required to be unique!", ticketResource.getId());
        }
        return ticketService.createNewTicket(ticketResource);
    }

    private boolean validateUniqueTicketConstraint(TicketResource ticketResource) {
        return ticketService.isOneSpecifiedTicketExisting(ticketResource.getId());
    }

    private void validateTicketResource(TicketResource ticketResource) {
        if (Strings.isNullOrEmpty(ticketResource.getId())) {
            throw new EmptyTicketIdException("Ticket id cannot be empty!");
        }

        if (ticketResource.getLines() == null || ticketResource.getLines().isEmpty()) {
            throw new EmptyLineListException("The line list in the ticket cannot be empty!");
        }

        validateLineResources(ticketResource.getLines());
    }

    private void validateLineResources(List<LineResource> lines) {
        lines.stream().forEach(
            line -> {
                boolean matches = Pattern.compile(LINE_CONTENT_VALIDATION_REGEX)
                        .matcher(line.getContent()).matches();
                if (!matches) {
                    throw new LineContentInvalidException(NUMBER_RANGE_INVALID_MSG);
                }
            }
        );
    }


    @Transactional
    @Operation(summary = "Amend a lottery ticket lines", description = "Amend lines of a lottery ticket by replacing with n additional new lines.", tags = {"Tickets"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "205", description = "For successful reset",
                    content = @Content(schema = @Schema(implementation = TicketResource.class))),
            @ApiResponse(responseCode = "404", description = "Ticket with the specified id not found",
                    content = @Content(schema = @Schema(implementation = RestErrorDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = RestErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal unknown exception",
                    content = @Content(schema = @Schema(implementation = RestErrorDTO.class))),
    })
    @PutMapping(path = "/tickets/{id}/lines", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    TicketResource amendTicketLines(@RequestBody List<LineResource> lines, @PathVariable String id) {
        validateLineResources(lines);
        return ticketService.amendTicketLines(lines, id);
    }

    @Operation(summary = "Get a lottery ticket by the specific id", description = "Get a lottery ticket by the specific id", tags = {"Tickets"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "For successful fetch",
                    content = @Content(schema = @Schema(implementation = TicketResource.class))),
            @ApiResponse(responseCode = "404", description = "Ticket with the specified id not found",
                    content = @Content(schema = @Schema(implementation = RestErrorDTO.class))),
    })
    @GetMapping(path = "/tickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    TicketResource findTicket(@PathVariable String id) {
        return ticketService.findOneTicket(id);
    }

    @Transactional
    @Operation(summary = "Retrieve and checkin status of ticket", description = "Retrieve and checkin status of lottery ticket by the specific id. Once the status of the ticket has been checked, it will not be possible to amend again.", tags = {"Tickets"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "For successful Retrieving",
                    content = @Content(schema = @Schema(implementation = TicketResource.class))),
            @ApiResponse(responseCode = "500", description = "Internal unknown exception",
                    content = @Content(schema = @Schema(implementation = RestErrorDTO.class))),
    })
    @PatchMapping(path = "/tickets/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    TicketResource checkinTicketStatus(@PathVariable String id) {
        return ticketService.checkinTicket(id);
    }


}
