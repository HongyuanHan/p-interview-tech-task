package com.poppulo.interview.convertors;

import com.poppulo.interview.entities.Line;
import com.poppulo.interview.entities.Ticket;
import com.poppulo.interview.models.LineResource;
import com.poppulo.interview.models.TicketResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TicketConvertor {

    private LineConvertor lineConvertor;

    @Autowired
    public TicketConvertor(LineConvertor lineConvertor) {
        this.lineConvertor = lineConvertor;
    }

    public TicketResource convertToResource(Ticket entity) {
        final List<LineResource> lineResources = lineConvertor.convertToResourceList(entity.getLines());

        return new TicketResource.TicketBuilder()
                        .id(entity.getTicketIdentifier())
                        .checked(entity.isChecked())
                        .lines(lineResources)
                        .build();
    }

    public List<TicketResource> convertToResourceList(List<Ticket> entities) {
        return entities.stream().map(e -> convertToResource(e)).collect(Collectors.toList());
    }

    public Ticket convertToEntity(TicketResource resource) {
        Ticket ticket = new Ticket();
        ticket.setChecked(resource.isChecked());
        ticket.setTicketIdentifier(resource.getId());

        resource.getLines().stream().forEach(e -> {
            Line line = new Line();
            line.setContent(e.getContent());
            line.setResult(e.getResult());

            ticket.addLine(line);
        });

        return ticket;
    }

}
