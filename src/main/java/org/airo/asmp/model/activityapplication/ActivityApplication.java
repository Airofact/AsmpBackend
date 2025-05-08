package org.airo.asmp.model.activityapplication;

import jakarta.persistence.*;
import org.airo.asmp.model.activity.Activity;
import org.airo.asmp.model.entity.Alumni;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class ActivityApplication{
    @Id
    @Column(columnDefinition = "char(36)")
    String id= UUID.randomUUID().toString();


    @OneToOne
    @JoinColumn(name = "activity_id", nullable = false)
    Activity activity;

    @ManyToOne
    @JoinColumn(name = "alumni_id", nullable = false)
    Alumni alumni;

    @Column()
    LocalDateTime applyTime;

    @Column(columnDefinition = "bool")
    boolean signedin;

    public ActivityApplication(Activity activity, Alumni alumni) {
        this.applyTime = LocalDateTime.now();
        this.activity = activity;
        this.alumni = alumni;
    }

    public void setActivity(Activity activity) {this.activity = activity;}
    public void setAlumni(Alumni alumni) {this.alumni = alumni;}
    public void setApplyTime() {
        applyTime = LocalDateTime.now();}
    public  LocalDateTime getApplyTime() {return applyTime;}
    public Activity getActivity() {return activity;}
    public Alumni getAlumni() {return alumni;}

    public ActivityApplication() {}
}
