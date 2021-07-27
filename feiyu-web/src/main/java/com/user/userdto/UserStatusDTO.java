package com.user.userdto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class UserStatusDTO {

    public static final String NORMAL = "1";
    public static final String DELETE = "d";

}
