package ch.hftm.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
public class Entry extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public boolean approved;
    
    @Size(max= 200)
    public String title;

    @Size(max= 10000)
    @Column(length = 10000)
    public String content;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "entry_id")
    public List<Comment> comments;

    @NotNull
    public String autor;

    @ElementCollection
    @CollectionTable(name = "UserLikes", joinColumns = @JoinColumn(name = "entry_id"))
    @Column(name="userId")
    public Set<String> userIdLikes;
}