package com.cg.model;

import com.cg.model.dto.RoleDTO;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name = "roles")
@Accessors(chain = true)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    /*@ManyToOne, @OneToOne: Mặc định hắn là EAGER*/
    /*@ManyToMany, @OneToMany: Mặc định hắn là LAZY*/
    /*mặc định là hắn LAZY, trường role, thì nó sẽ không hiển thị thằng userId có chưa roleId(2.v.v) tương ứng ra, còn dùng EAGER thì nó sẽ lấy tất cả record tương ứng, userId sẽ lấy hết thằng roleId tương ứng mà nó chưa*/
    @OneToMany(targetEntity = User.class, mappedBy = "role", fetch = FetchType.EAGER)
    private Set<User> users;



    public RoleDTO toRoleDTO(){
        return new RoleDTO()
                .setId(id)
                .setCode(code)
                .setName(name);

    }
}
