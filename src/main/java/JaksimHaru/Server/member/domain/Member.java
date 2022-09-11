package JaksimHaru.Server.member.domain;

import JaksimHaru.Server.common.domain.BaseEntity;
import JaksimHaru.Server.schedule.domain.Schedule;
import JaksimHaru.Server.todo.domain.Todo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@DynamicUpdate
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    private String name;

    @JsonIgnore
    private String password;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String providerId;

    @OneToMany(mappedBy = "member")
    private List<Todo> todos = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Schedule> schedules = new ArrayList<>();

    @Builder
    public Member(String email, String name, String password, String imageUrl, Role role, Provider provider, String providerId) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.provider = provider;
        this.imageUrl = imageUrl;
        this.providerId = providerId;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
