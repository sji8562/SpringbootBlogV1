package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {

    private String username;
    private String password;
    private String email;

}
