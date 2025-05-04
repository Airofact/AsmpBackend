package org.airo.asmp.model.Activity_application;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Activity_application{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(columnDefinition = "varchar(100)")
    String title;

    @Column(columnDefinition = "bigint")
    Long alumni_id;

    @Column(columnDefinition = "text")
    String description;

    @Column()
    LocalDateTime apply_time;

    @Column(columnDefinition = "bool")
    boolean signedin;

    public Activity_application(String title,Long alumni_id,String description) {
        this.title = title;
        this.alumni_id = alumni_id;
        this.description = description;
        this.apply_time = LocalDateTime.now();
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setAlumni_id(Long alumni_id) {
        this.alumni_id = alumni_id;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public  LocalDateTime getApply_time() {
        return apply_time;
    }
    public String getTitle() {
        return title;
    }
    public Long getAlumni_id() {
        return alumni_id;
    }
    public String getDescription() {
        return description;
    }
    public Activity_application() {}
}
