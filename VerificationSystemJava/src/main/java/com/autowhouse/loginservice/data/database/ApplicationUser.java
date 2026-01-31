package com.autowhouse.loginservice.data.database;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_table")
public class ApplicationUser extends AuditTable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "register_id")
    private long RegisterId;

    @Column(unique = true,nullable = false,length = 60)
    private String email;

    @Column(nullable = false,length = 60)
    private String password;

    @ManyToMany
    @JoinTable(name = "user_roles",
    joinColumns = @JoinColumn(name = "register_id"),
    inverseJoinColumns = @JoinColumn(referencedColumnName = "role_id"))
    private List<RoleTable> userRoles;
}
