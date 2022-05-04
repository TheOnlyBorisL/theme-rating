package net.etfbl.themerating.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContentRequest {
    private String information;
    private long clientID;
}
