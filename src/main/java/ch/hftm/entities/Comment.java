package ch.hftm.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
public class Comment extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    
    public String userId;

    @Size(max= 1000)
    @Column(length = 1000)
    public String comment;

    @Builder.Default
    public LocalDateTime date = LocalDateTime.now();
}