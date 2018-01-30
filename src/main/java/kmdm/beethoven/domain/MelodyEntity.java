package kmdm.beethoven.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A MelodyEntity.
 */
@Entity
@Table(name = "melody_entity")
public class MelodyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "content")
    private String content;

    @NotNull
    @Column(name = "created_date_time", nullable = false)
    private ZonedDateTime createdDateTime;

    @ManyToOne
    private Profile profile;

    @Column(name = "path")
    private String path;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public MelodyEntity name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public MelodyEntity content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public MelodyEntity createdDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Profile getProfile() {
        return profile;
    }

    public MelodyEntity profile(Profile profile) {
        this.profile = profile;
        return this;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
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
        MelodyEntity melodyEntity = (MelodyEntity) o;
        if (melodyEntity.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), melodyEntity.getId());
    }

    public String getPath() {
        return path;
    }

    public MelodyEntity path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MelodyEntity{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", content='" + getContent() + "'" +
            ", createdDateTime='" + getCreatedDateTime() + "'" +
            ", path='" + getPath() + "'" +
            "}";
    }
}
