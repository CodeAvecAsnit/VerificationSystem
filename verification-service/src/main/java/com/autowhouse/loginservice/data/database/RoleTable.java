package com.autowhouse.loginservice.data.database;

import com.autowhouse.loginservice.data.enumeration.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


/**
 * @author : Asnit Bakhati
 * @Date : 10th Feb,2026
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RoleTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(mappedBy = "userRoles")
    private List<ApplicationUser> users;
}
