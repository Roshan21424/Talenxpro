package com.talentxpro.website.models.RoleDTO;

import jakarta.persistence.*;

@Entity
@Table(name="role")
public class Role{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Enumerated(EnumType.STRING)
    @Column(length=20,name="role_name")
    private AppRole roleName;



    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleName=" + roleName +
                '}';
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public AppRole getRoleName() {
        return roleName;
    }

    public void setRoleName(AppRole roleName) {
        this.roleName = roleName;
    }



    public Role() {
    }

    public Role(Integer roleId, AppRole roleName) {
        this.roleId = roleId;
        this.roleName = roleName;

    }


    public Role(AppRole roleName) {
        this.roleName = roleName;
    }
}
