/*
 * This file is generated by jOOQ.
 */
package edu.java.scrapper.domain.jooq.tables.pojos;


import jakarta.validation.constraints.Size;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.7"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Link implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String url;
    private OffsetDateTime lastTracked;

    public Link() {}

    public Link(Link value) {
        this.id = value.id;
        this.url = value.url;
        this.lastTracked = value.lastTracked;
    }

    @ConstructorProperties({ "id", "url", "lastTracked" })
    public Link(
        @Nullable Integer id,
        @NotNull String url,
        @NotNull OffsetDateTime lastTracked
    ) {
        this.id = id;
        this.url = url;
        this.lastTracked = lastTracked;
    }

    /**
     * Getter for <code>LINK.ID</code>.
     */
    @Nullable
    public Integer getId() {
        return this.id;
    }

    /**
     * Setter for <code>LINK.ID</code>.
     */
    public void setId(@Nullable Integer id) {
        this.id = id;
    }

    /**
     * Getter for <code>LINK.URL</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 1000000000)
    @NotNull
    public String getUrl() {
        return this.url;
    }

    /**
     * Setter for <code>LINK.URL</code>.
     */
    public void setUrl(@NotNull String url) {
        this.url = url;
    }

    /**
     * Getter for <code>LINK.LAST_TRACKED</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public OffsetDateTime getLastTracked() {
        return this.lastTracked;
    }

    /**
     * Setter for <code>LINK.LAST_TRACKED</code>.
     */
    public void setLastTracked(@NotNull OffsetDateTime lastTracked) {
        this.lastTracked = lastTracked;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Link other = (Link) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.url == null) {
            if (other.url != null)
                return false;
        }
        else if (!this.url.equals(other.url))
            return false;
        if (this.lastTracked == null) {
            if (other.lastTracked != null)
                return false;
        }
        else if (!this.lastTracked.equals(other.lastTracked))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.url == null) ? 0 : this.url.hashCode());
        result = prime * result + ((this.lastTracked == null) ? 0 : this.lastTracked.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Link (");

        sb.append(id);
        sb.append(", ").append(url);
        sb.append(", ").append(lastTracked);

        sb.append(")");
        return sb.toString();
    }
}