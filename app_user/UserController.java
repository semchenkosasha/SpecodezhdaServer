package com.app.app_user;

import com.app.app_user.converter.UserDtoToUserConverter;
import com.app.app_user.converter.UserToUserDtoConverter;
import com.app.system.Result;
import com.app.system.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

import static com.app.util.Global.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final UserToUserDtoConverter toDtoConverter;
    private final UserDtoToUserConverter toUserConverter;

    @Secured({ADMIN})
    @GetMapping("/all")
    public Result findAll() {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find All",
                service.findAll().stream().map(toDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @Secured({ADMIN, MANAGER, USER})
    @GetMapping
    public Result find() {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find",
                toDtoConverter.convert(service.getCurrentUser())
        );
    }

    @Secured({ADMIN})
    @GetMapping("/{id}")
    public Result findById(@PathVariable String id) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Find By Id",
                toDtoConverter.convert(service.find(id))
        );
    }

    @PostMapping
    public Result save(@Valid @RequestBody AppUser newUser) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Save",
                toDtoConverter.convert(service.save(newUser))
        );
    }

    @Secured({ADMIN, MANAGER, USER})
    @PutMapping
    public Result update(@Valid @RequestBody UserDto updateDto) {
        AppUser update = toUserConverter.convert(updateDto);
        AppUser updated = service.update(update);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update",
                toDtoConverter.convert(updated)
        );
    }

    @Secured({ADMIN})
    @PatchMapping("/{id}/role")
    public Result updateRole(@PathVariable String id, @RequestParam String role) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update Role",
                toDtoConverter.convert(service.updateRole(id, role))
        );
    }

    @Secured({ADMIN, MANAGER, USER})
    @PatchMapping("/fio")
    public Result updateFio(@RequestParam String fio) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update Fio",
                toDtoConverter.convert(service.updateFio(fio))
        );
    }

    @Secured({ADMIN, MANAGER, USER})
    @PatchMapping("/img")
    public Result updateImg(@RequestParam MultipartFile files) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update Img",
                toDtoConverter.convert(service.updateImg(files))
        );
    }

    @Secured({ADMIN, MANAGER, USER})
    @PatchMapping("/workwearSize")
    public Result updateWorkwearSize(@RequestParam String workwearSize) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update WorkwearSize",
                toDtoConverter.convert(service.updateWorkwearSize(workwearSize))
        );
    }

    @Secured({ADMIN, MANAGER, USER})
    @PatchMapping("/position")
    public Result updatePosition(@RequestParam String positionId) {
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Update Position",
                toDtoConverter.convert(service.updatePosition(positionId))
        );
    }

    @Secured({ADMIN})
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        service.delete(id);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Delete"
        );
    }
}
