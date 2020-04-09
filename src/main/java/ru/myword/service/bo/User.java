package ru.myword.service.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 * Пользователь системы
 *
 * @author Губа Георгий
 */
@Entity
@Table(name  = "tbl_user")
public class User extends Model
{
    /**
     * Логин пользователя (уникальный)
     */
    @Column(unique = true)
    private String login;

    /**
     * Пароль пользователя
     */
    @Column
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    /**
     * Архивность пользователя
     */
    @Column
    private boolean archive;

    public String getLogin()
    {
        return login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    @JsonIgnore
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean isArchive()
    {
        return archive;
    }

    public void setArchive(boolean archive)
    {
        this.archive = archive;
    }
}
