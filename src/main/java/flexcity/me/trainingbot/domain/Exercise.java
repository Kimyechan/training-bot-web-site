package flexcity.me.trainingbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Exercise {

    @Id @GeneratedValue
    private Long exerciseId;

    private String kind;

    private Date date;

    private Long purposeCount;

    private Long count;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Account account;
}
