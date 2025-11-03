package com.app.app_user;

import com.app.enums.Role;
import com.app.enums.WorkwearSize;
import com.app.inventory_ordering.InventoryOrdering;
import com.app.position.PositionService;
import com.app.system.exception.BadRequestException;
import com.app.system.exception.ObjectNotFoundException;
import com.app.workwear_ordering.WorkwearOrdering;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.app.util.Global.saveFile;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final PositionService positionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .map(MyUserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с логином " + username + " не найден"));
    }

    public AppUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return repository.findByUsername(currentUserName).orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));
        }
        return null;
    }

    public List<AppUser> findAll() {
        return repository.findAll();
    }

    public AppUser find(String id) {
        return repository.findById(Long.parseLong(id)).orElseThrow(() -> new ObjectNotFoundException("Не найден пользователь с ИД: " + id));
    }

    public AppUser save(AppUser user) {
        if (repository.findByUsername(user.getUsername()).isPresent()) {
            throw new BadRequestException("Пользователь с таким логином уже существует");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (findAll().isEmpty()) {
            user.setRole(Role.ADMIN);
        }
        return repository.save(user);
    }

    public AppUser update(AppUser user) {
        AppUser old = getCurrentUser();
        old.update(user);
        return repository.save(old);
    }

    public AppUser updateRole(String id, String role) {
        AppUser user = find(id);
        try {
            user.setRole(Role.valueOf(role));
        } catch (Exception e) {
            throw new BadRequestException("Некорректный выбор роли");
        }
        return repository.save(user);
    }

    public AppUser updateFio(String fio) {
        AppUser user = getCurrentUser();
        user.setFio(fio);
        return repository.save(user);
    }

    public AppUser updateImg(MultipartFile img) {
        AppUser user = getCurrentUser();
        try {
            user.setImg(saveFile(img, "user"));
        } catch (IOException e) {
            throw new BadRequestException("Некорректное изображение");
        }
        return repository.save(user);
    }

    public AppUser updateWorkwearSize(String workwearSize) {
        AppUser user = getCurrentUser();
        try {
            user.setWorkwearSize(WorkwearSize.valueOf(workwearSize));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Некорректный выбор размера одежды");
        }
        return repository.save(user);
    }

    public AppUser updatePosition(String positionId) {
        AppUser user = getCurrentUser();
        user.setPosition(positionService.find(positionId));
        return repository.save(user);
    }

    public void delete(String userId) {
        AppUser user = find(userId);

        for (WorkwearOrdering i : user.getWorkwearOrderingsUser()) if (clearWorkwearOrdering(user, i)) break;
        for (WorkwearOrdering i : user.getWorkwearOrderingsManager()) if (clearWorkwearOrdering(user, i)) break;

        for (InventoryOrdering i : user.getInventoryOrderingsUser()) if (clearInventoryOrdering(user, i)) break;
        for (InventoryOrdering i : user.getInventoryOrderingsManager()) if (clearInventoryOrdering(user, i)) break;

        user = repository.save(user);

        repository.deleteById(user.getId());
    }

    private boolean clearWorkwearOrdering(AppUser user, WorkwearOrdering i) {
        if (i.getUser() != null) {
            if (i.getUser().getId().equals(user.getId())) {
                i.setUser(null);
                return true;
            }
        }
        if (i.getManager() != null) {
            if (i.getManager().getId().equals(user.getId())) {
                i.setUser(null);
                return true;
            }
        }
        return false;
    }

    private boolean clearInventoryOrdering(AppUser user, InventoryOrdering i) {
        if (i.getUser() != null) {
            if (i.getUser().getId().equals(user.getId())) {
                i.setUser(null);
                return true;
            }
        }
        if (i.getManager() != null) {
            if (i.getManager().getId().equals(user.getId())) {
                i.setUser(null);
                return true;
            }
        }
        return false;
    }

}
