package com.app.app_user;

import com.app.enums.Role;
import com.app.enums.WorkwearSize;
import com.app.inventory.Inventory;
import com.app.inventory_ordering.InventoryOrdering;
import com.app.position.Position;
import com.app.workwear.Workwear;
import com.app.workwear_ordering.WorkwearOrdering;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class AppUser implements Serializable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "app_user_g")
    @SequenceGenerator(name = "app_user_g", sequenceName = "app_user_seq", allocationSize = 1)
    private Long id;

    @Size(min = 1, max = 255, message = "username is required length 1-255")
    @NotEmpty(message = "username is required")
    private String username;
    @Size(min = 1, max = 255, message = "password is required length 1-255")
    @NotEmpty(message = "password is required")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private String fio = "ФИО";

    @Column(length = 1000)
    private String img = "/img/avatar.png";

    @Enumerated(EnumType.STRING)
    private WorkwearSize workwearSize = WorkwearSize.M;

    @ManyToOne
    private Position position = null;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Workwear> workwears = new ArrayList<>();
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Inventory> inventories = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REFRESH)
    private List<WorkwearOrdering> workwearOrderingsUser = new ArrayList<>();
    @OneToMany(mappedBy = "manager", cascade = CascadeType.REFRESH)
    private List<WorkwearOrdering> workwearOrderingsManager = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REFRESH)
    private List<InventoryOrdering> inventoryOrderingsUser = new ArrayList<>();
    @OneToMany(mappedBy = "manager", cascade = CascadeType.REFRESH)
    private List<InventoryOrdering> inventoryOrderingsManager = new ArrayList<>();

    public AppUser(String username) {
        this.username = username;
    }

    public void update(AppUser user) {

    }

    public Long getPositionId() {
        return position == null ? 0L : position.getId();
    }

    public String getPositionName() {
        return position == null ? "" : position.getName();
    }
}