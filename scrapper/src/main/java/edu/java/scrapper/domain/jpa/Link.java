package edu.java.scrapper.domain.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.SequenceGenerator;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence-generator")
    @SequenceGenerator(
        name = "sequence-generator",
        sequenceName = "link_id_seq",
        allocationSize = 1
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "url", columnDefinition = "text", nullable = false)
    private String uri;

    @Column(name = "last_tracked")
    private OffsetDateTime lastTracked;

    @ManyToMany(
        fetch = FetchType.LAZY,
        mappedBy = "trackedLinks"
    )
    private Set<Chat> trackingChats = new HashSet<>();

    @PreRemove
    private void removeRefs() {
        for (var chat : trackingChats) {
            chat.deleteLink(this);
        }
    }
}
