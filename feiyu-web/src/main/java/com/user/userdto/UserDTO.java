package com.user.userdto;

import com.user.dto.UserRelationDTO;
import com.user.entity.TUserFriendRelation;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class UserDTO {

    private   List<TUserFriendRelation> relations;
    private  List<UserRelationDTO> relationsDTO;
}
