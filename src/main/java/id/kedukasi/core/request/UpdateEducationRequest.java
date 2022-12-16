package id.kedukasi.core.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class UpdateEducationRequest {
    @NotNull(message = "name null")
    private String name;
    @NotNull(message = "description null")
    private String description;
}
