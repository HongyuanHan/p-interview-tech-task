package com.poppulo.interview.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = TicketResource.TicketBuilder.class)
public class TicketResource {

    @JsonProperty("id")
    @Size(min= 1, max = 255)
    @NotNull
    private String id;

    @JsonProperty("lines")
    @NotNull
    private List<LineResource> lines;

    @JsonProperty("checked")
    private boolean checked;

    private TicketResource(TicketBuilder builder) {
        this.id = builder.id;
        this.checked = builder.checked;
        this.lines = builder.lines;
    }

    @Schema(description = "The unique ticket identifier string.")
    public String getId() {
        return id;
    }

    @Schema(description = "The associated lines of the ticket.")
    public List<LineResource> getLines() {
        return lines;
    }

    @Schema(description = "The flag of whether the ticket is checked. This is a read-only property and cannot be changed by posted value. Default value is false.", defaultValue = "false")
    public boolean isChecked() {
        return checked;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class TicketBuilder {
        private String id;
        private List<LineResource> lines;
        private boolean checked;

        public TicketBuilder id(String id) {
            this.id = id;
            return this;
        }

        public TicketBuilder lines(List<LineResource> lines) {
            this.lines = lines;
            return this;
        }

        public TicketBuilder checked(boolean isChecked) {
            this.checked = isChecked;
            return this;
        }

        public TicketResource build() {
            TicketResource ticket = new TicketResource(this);
            return ticket;
        }
    }
}
