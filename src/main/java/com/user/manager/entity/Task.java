package com.user.manager.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "task")
@NoArgsConstructor
@AllArgsConstructor
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Version
  @Column(name = "version")
  @JsonIgnore
  private Integer version;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "date_time")
  @JsonProperty("date_time")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
  @Builder.Default
  private Date dateTime = new Date();

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private TaskStatus status = TaskStatus.PENDING;
}
