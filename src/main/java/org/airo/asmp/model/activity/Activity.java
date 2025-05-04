package org.airo.asmp.model.activity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Activity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

  @Column(columnDefinition = "varchar(100)")
  String title;

  @Column(columnDefinition = "text")
  String description;

  @Column()
  LocalDateTime start_time;

  @Column()
  LocalDateTime end_time;

  @Column(columnDefinition = "varchar(200)")
  String location;

  @Column(columnDefinition = "int")
  int max_participants;

  @Column(columnDefinition = "bigint")
  long org_id;

  @Column(columnDefinition = "bigint")
  long creator_id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  Status status=Status.not_started;

 public Activity(String title,String description,LocalDateTime end_time,LocalDateTime start_time,
                 String location,int max_participants,long org_id,
                 long creator_id) {
this.start_time = start_time;
this.end_time = end_time;
this.location = location;
this.max_participants = max_participants;
this.org_id = org_id;
this.creator_id = creator_id;
this.title = title;
this.description = description;
 }

  public void setStart_time(LocalDateTime start_time) {
   this.start_time = start_time;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public void setDescription(String description) {
   this.description = description;
  }
  public void setLocation(String location) {
   this.location = location;
  }
  public void setMax_participants(int max_participants) {
   this.max_participants = max_participants;
  }
  public void setOrg_id(long org_id) {
   this.org_id = org_id;
  }
  public void setCreator_id(long creator_id) {
   this.creator_id = creator_id;
  }
  public void setEnd_time(LocalDateTime end_time) {
   this.end_time = end_time;
  }

  public LocalDateTime getStart_time() {
    return start_time;
  }
  public String getTitle() {
   return title;
  }
  public String getDescription() {
   return description;
  }
  public String getLocation() {
   return location;
  }
  public int getMax_participants() {
   return max_participants;
  }
  public long getOrg_id() {
   return org_id;
  }
  public long getCreator_id() {
   return creator_id;
  }
  public LocalDateTime getEnd_time() {
   return end_time;
  }

  public Activity() {}
}
