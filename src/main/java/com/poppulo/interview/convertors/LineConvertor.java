package com.poppulo.interview.convertors;

import com.poppulo.interview.entities.Line;
import com.poppulo.interview.models.LineResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LineConvertor {

    public LineResource convertToResource(Line entity) {
        return new LineResource.LineBuilder()
                        .content(entity.getContent())
                        .result(entity.getResult())
                        .build();
    }

    public List<LineResource> convertToResourceList(List<Line> entities) {
        return entities.stream()
                .map(e -> convertToResource(e))
                .sorted((line1, line2) -> line2.getResult() - line1.getResult()) // big to small
                .collect(Collectors.toList());
    }

}
