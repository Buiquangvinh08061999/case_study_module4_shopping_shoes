package com.cg.model;

import com.cg.model.dto.UserDTO;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Accessors(chain = true)

public class User extends BaseEntities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullname;

    private String phone;

//    @Column(name = "is_actice", columnDefinition = "boolean default false")
//    private boolean isActice;

    @Column(name = "url_image")
    private String urlImage;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;

    @OneToOne
    @JoinColumn(name ="location_region_id" )
    private LocationRegion locationRegion;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;


    @OneToMany(mappedBy = "user")
    private Set<Order> orders;


    @OneToMany(mappedBy = "user")
    private Set<Cart> carts;



    public UserDTO toUserDTO(){
        return new UserDTO()
                .setId(id)
                .setUsername(username)
                .setPassword(password)
                .setFullname(fullname)
                .setUrlImage(urlImage)
                .setPhone(phone)
                .setCreatedAt(getCreatedAt())
                .setUpdatedAt(getUpdatedAt())
                .setLocationRegion(locationRegion.toLocationRegionDTO())
                .setRole(role.toRoleDTO());
    }








    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

}
