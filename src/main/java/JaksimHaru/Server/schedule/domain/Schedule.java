package JaksimHaru.Server.schedule.domain;

import JaksimHaru.Server.common.domain.BaseEntity;
import JaksimHaru.Server.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String date;

    private String time;

    private String content;




}
