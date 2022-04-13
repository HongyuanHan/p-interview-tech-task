package com.poppulo.interview.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


import static com.poppulo.interview.utils.Constants.LINE_CONTENT_VALIDATION_REGEX;
import static com.poppulo.interview.utils.Constants.NUMBER_RANGE_INVALID_MSG;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = LineResource.LineBuilder.class)
public class LineResource {

    @JsonProperty("content")
    @Pattern(regexp = LINE_CONTENT_VALIDATION_REGEX, message = NUMBER_RANGE_INVALID_MSG)
    @NotNull
    private String content;

    @JsonProperty("result")
    private int result;

    private LineResource(LineBuilder builder) {
        this.content = builder.content;
        this.result = builder.result;
    }

    @Schema(description = "The content of the line with 3 numbers, each of which has a value of 0, 1, or 2. It is separated by comma.")
    public String getContent() {
        return content;
    }

    @Schema(description = "The result of the line that applied with the lottery rules. This is a read-only property and cannot be changed by posted value. ")
    public int getResult() {
        return result;
    }

    public void updateResult(int result) {
        this.result = result;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class LineBuilder {
        private String content;
        private int result;

        public LineBuilder content(String content) {
            this.content = content;
            return this;
        }

        public LineBuilder result(int result) {
            this.result = result;
            return this;
        }

        public LineResource build() {
            LineResource line = new LineResource(this);
            return line;
        }
    }
}
