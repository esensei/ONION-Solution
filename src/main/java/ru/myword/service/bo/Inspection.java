package ru.myword.service.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 * Сущность для медицинского обследования
 *
 * @author Губа Георгий
 */
@Entity
@Table(name = "tbl_inspection")
public class Inspection extends Model
{
    @Column(name = "file_name")
    private String fileName;

    @Column(name = "status_inspection")
    private StatusInspection statusInspection;

    @Column(name = "path_to_original_file")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String pathToOriginalFile;

    @Column(name = "path_to_final_file")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String pathToFinalFile;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonProperty(access = Access.WRITE_ONLY)
    private User user;

    public Inspection()
    {
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public StatusInspection getStatusInspection()
    {
        return statusInspection;
    }

    public void setStatusInspection(StatusInspection statusInspection)
    {
        this.statusInspection = statusInspection;
    }

    public String getPathToOriginalFile()
    {
        return pathToOriginalFile;
    }

    public void setPathToOriginalFile(String pathToOriginalFile)
    {
        this.pathToOriginalFile = pathToOriginalFile;
    }

    public String getPathToFinalFile()
    {
        return pathToFinalFile;
    }

    public void setPathToFinalFile(String pathToFinalFile)
    {
        this.pathToFinalFile = pathToFinalFile;
    }

    @JsonIgnore
    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
