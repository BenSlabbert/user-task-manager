package com.user.manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Version
  @Column(name = "version")
  @JsonIgnore
  private Integer version;

  @JsonProperty("username")
  @Column(name = "user_name")
  private String userName;

  @JsonProperty("first_name")
  @Column(name = "first_name")
  private String firstName;

  @JsonProperty("last_name")
  @Column(name = "last_name")
  private String lastName;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Task> tasks;
}
