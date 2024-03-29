package com.example.driveanalysis.user.entity;

import com.example.driveanalysis.base.config.BaseTimeEntity;
import com.example.driveanalysis.listeners.UserHistoryEntityListeners;
import com.example.driveanalysis.userhistory.entity.UserHistory;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
@EntityListeners(value = UserHistoryEntityListeners.class)
public class SiteUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;
    private boolean isProductPaid;

    public SiteUser(long id) {
        this.id = id;
    }

}
