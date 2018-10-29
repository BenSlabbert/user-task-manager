package com.user.manager.entity;

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
  private Integer version;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "date_time")
  @Builder.Default
  private Date dateTime = new Date();

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private TaskStatus status = TaskStatus.PENDING;
}
