package com.bennavetta.clef.example.model;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;

public class User implements UserDetails
{
    @Id
    private String id;

    private String clefId;
    private String email;

    /*
    In a larger application, it might make sense to store this outside the main user database,
    maybe in Redis or some other fast in-memory store, especially since it gets checked on every
    request.
     */
    private Instant loggedOutAt;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getClefId()
    {
        return clefId;
    }

    public void setClefId(String clefId)
    {
        this.clefId = clefId;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Instant getLoggedOutAt()
    {
        return loggedOutAt;
    }

    public void setLoggedOutAt(Instant loggedOutAt)
    {
        this.loggedOutAt = loggedOutAt;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("clefId", clefId)
                .add("email", email)
                .toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return Lists.newArrayList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword()
    {
        return null;
    }

    @Override
    public String getUsername()
    {
        return email;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }
}
