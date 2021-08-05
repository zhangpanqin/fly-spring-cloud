package com.mflyyou.cloud.user;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "user_info")
public class UserEntity {
    @Id
    private Long id;
    private String username;
}
