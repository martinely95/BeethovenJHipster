package kmdm.beethoven.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Profile.
 */
@Entity
@Table(name = "profile")
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "profile")
    @JsonIgnore
    private Set<MelodyEntity> melodyEntities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public Profile user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<MelodyEntity> getMelodyEntities() {
        return melodyEntities;
    }

    public Profile melodyEntities(Set<MelodyEntity> melodyEntities) {
        this.melodyEntities = melodyEntities;
        return this;
    }

    public Profile addMelodyEntities(MelodyEntity melodyEntity) {
        this.melodyEntities.add(melodyEntity);
        melodyEntity.setProfile(this);
        return this;
    }

    public Profile removeMelodyEntities(MelodyEntity melodyEntity) {
        this.melodyEntities.remove(melodyEntity);
        melodyEntity.setProfile(null);
        return this;
    }

    public void setMelodyEntities(Set<MelodyEntity> melodyEntities) {
        this.melodyEntities = melodyEntities;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Profile profile = (Profile) o;
        if (profile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Profile{" +
            "id=" + getId() +
            "}";
    }
}
