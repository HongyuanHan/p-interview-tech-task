package com.poppulo.interview.services.impl;

import com.poppulo.interview.convertors.LineConvertor;
import com.poppulo.interview.convertors.TicketConvertor;
import com.poppulo.interview.entities.Line;
import com.poppulo.interview.entities.Ticket;
import com.poppulo.interview.errorhandling.RestErrorCode;
import com.poppulo.interview.errorhandling.exceptions.InvalidAmendmentWithStatusCheckedException;
import com.poppulo.interview.errorhandling.exceptions.InvalidInputException;
import com.poppulo.interview.errorhandling.exceptions.TicketNotFoundException;
import com.poppulo.interview.models.LineResource;
import com.poppulo.interview.models.TicketResource;
import com.poppulo.interview.repositories.TicketRepository;
import com.poppulo.interview.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.poppulo.interview.utils.Constants.LOTTERY_RULE_SUPPORTED_DIGIT_NUMBER;

@Service
public class TicketServiceImpl implements TicketService {

    private TicketRepository ticketRepository;

    private TicketConvertor ticketConvertor;

    private LineConvertor lineConvertor;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, TicketConvertor ticketConvertor, LineConvertor lineConvertor) {
        this.ticketRepository = ticketRepository;
        this.ticketConvertor = ticketConvertor;
        this.lineConvertor = lineConvertor;
    }

    @Override
    public List<TicketResource> getAllTickets(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Ticket> pagedResult = ticketRepository.findAll(pageable);
        return convertToTicketResources(pagedResult.getContent());
    }

    @Override
    public List<TicketResource> getAllTickets() {
        List<Ticket> ticketEntities = ticketRepository.findAll();
        return convertToTicketResources(ticketEntities);
    }

    @Override
    public TicketResource createNewTicket(TicketResource ticketResource) {
        TicketResource ticketResUpdated = applyLotteryRules(ticketResource);
        Ticket ticketEntity = convert(ticketResUpdated);
        // For creation, the checked should be initialised with false
        ticketEntity.setChecked(false);
        Ticket saved = ticketRepository.save(ticketEntity);
        return convert(saved);
    }

    @Override
    public TicketResource amendTicketLines(List<LineResource> lines, String id) {
        List<LineResource> linesAppliedWithRules = applyLotteryRules(lines);

        Ticket updated = ticketRepository.findOneByTicketIdentifier(id)
                .map(ticket -> {
                    if (ticket.isChecked()) {
                        throw new InvalidAmendmentWithStatusCheckedException("The status of ticket has been checked in, it is not allowed to amend.");
                    }

                    ticket.clearLines();
                    linesAppliedWithRules.stream().forEach(lineResource -> {
                        Line line = new Line();
                        line.setContent(lineResource.getContent());
                        line.setResult(lineResource.getResult());
                        ticket.addLine(line);
                    });

                    return ticketRepository.save(ticket);
                }).orElseGet(() -> {
                    throw new TicketNotFoundException("Ticket with id %s not found", id);
                });

        return convert(updated);
    }

    @Override
    public TicketResource findOneTicket(String id) {
        Ticket ticket = ticketRepository.findOneByTicketIdentifier(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket with id %s not found", id));

        return convert(ticket);
    }

    @Override
    public boolean isOneSpecifiedTicketExisting(String id) {
        return ticketRepository.findOneByTicketIdentifier(id)
                                    .map(ticket -> true)
                                    .orElseGet(() -> false);
    }

    @Override
    public TicketResource checkinTicket(String id) {
        Ticket updated = ticketRepository.findOneByTicketIdentifier(id)
                .map(ticket -> {
                    ticket.setChecked(true);
                    return ticketRepository.save(ticket);
                }).orElseGet(() -> {
                    throw new TicketNotFoundException("Ticket with id %s not found", id);
        });
        return convert(updated);
    }

    private TicketResource applyLotteryRules(TicketResource ticketResource) {
        applyLotteryRules(ticketResource.getLines());

        return ticketResource;
    }

    private List<LineResource> applyLotteryRules(List<LineResource> lineResources) {
        lineResources.stream().forEach(
                lineResource -> doLotteryRulesForLine(lineResource)
        );

        return lineResources;
    }

    private void doLotteryRulesForLine(LineResource lineResource) {
        int[] numbers = Arrays.stream(lineResource.getContent().split(","))
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .toArray();

        if (numbers.length != LOTTERY_RULE_SUPPORTED_DIGIT_NUMBER) {
            throw new InvalidInputException(RestErrorCode.UNSUPPORTED_DIGIT_COUNT_IN_LINE,
                    "Invalid input of line content! Current Lottery Rule only support %d digits!", LOTTERY_RULE_SUPPORTED_DIGIT_NUMBER);
        }

        if (numbers[0] + numbers[1] + numbers[2] == 2) {
            lineResource.updateResult(10);
        } else if (numbers[0] == numbers[1] && numbers[0] == numbers[2]) {
            lineResource.updateResult(5);
        } else if (numbers[0] != numbers[1] && numbers[0] != numbers[2]) {
            lineResource.updateResult(1);
        } else {
            lineResource.updateResult(0);
        }
    }

    private List<TicketResource> convertToTicketResources(List<Ticket> ticketEntities) {
        return ticketConvertor.convertToResourceList(ticketEntities);
    }

    private Ticket convert(TicketResource ticketResource) {
        return ticketConvertor.convertToEntity(ticketResource);
    }

    private TicketResource convert(Ticket ticketEntity) {
        return ticketConvertor.convertToResource(ticketEntity);
    }

}
