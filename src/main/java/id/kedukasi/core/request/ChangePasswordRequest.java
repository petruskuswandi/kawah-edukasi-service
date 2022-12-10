package id.kedukasi.core.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {

    private long id;

    private String password;

    private String token;

}
