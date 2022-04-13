package com.poppulo.interview.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "ticket_Identifier", nullable = false, length = 255, unique = true)
    private String ticketIdentifier;

    @Column(name = "is_Checked")
    private boolean isChecked;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("result desc")
    private List<Line> lines = new ArrayList<>();

    public Ticket() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketIdentifier() {
        return ticketIdentifier;
    }

    public void setTicketIdentifier(String ticketIdentifier) {
        this.ticketIdentifier = ticketIdentifier;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public void addLine(Line line) {
        lines.add(line);
        line.setTicket(this);
    }

    public void removeLine(Line line) {
        lines.remove(line);
        line.setTicket(null);
    }

    public void clearLines() {
        this.lines.stream().forEach(e -> e.setTicket(null));
        this.lines.clear();
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", ticketIdentifier='" + ticketIdentifier + '\'' +
                ", isChecked=" + isChecked +
                ", lines=" + lines +
                '}';
    }
}
