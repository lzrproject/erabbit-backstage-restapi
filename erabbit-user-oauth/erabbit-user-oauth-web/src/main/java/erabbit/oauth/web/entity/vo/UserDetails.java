package erabbit.oauth.web.entity.vo;

import lombok.Data;

@Data
public class UserDetails {

    private String username;
    private String password;

    //小兔鲜页面
    private String account;
}
