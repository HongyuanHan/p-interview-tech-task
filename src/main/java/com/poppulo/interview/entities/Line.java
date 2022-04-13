package com.poppulo.interview.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content", nullable = false, length = 255)
    private String content;

    @Column(name = "result", nullable = false)
    private int result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_Id")
    private Ticket ticket;

    public Line() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", result=" + result +
                ", ticket=" + ticket.getTicketIdentifier() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return result == line.result && Objects.equals(id, line.id) && Objects.equals(content, line.content) && Objects.equals(ticket, line.ticket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, result, ticket);
    }
}
