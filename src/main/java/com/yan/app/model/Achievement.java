package com.yan.app.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profile_achievements")
public class Achievement {
    @Id
    @Column(name = "uuid", columnDefinition = "varchar(36)")
    @Type(type = "uuid-char")
    private UUID uuid;

    private String imageName;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_profile_uuid", referencedColumnName = "uuid")
    @ToString.Exclude
    private UserProfile userProfile;
}
