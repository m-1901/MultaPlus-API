package ao.multaplus.role.controller;

import ao.multaplus.role.dto.RoleRequestDto;
import ao.multaplus.role.entity.Roles;
import ao.multaplus.role.service.RoleServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("roles")
@AllArgsConstructor
public class RoleController {
    private final RoleServiceImpl roleService;

    @Operation(description = "List a role", tags = "role")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @GetMapping("/{id}")
    public ResponseEntity<?> getone(@PathVariable long id) {
        return roleService.getone(id);
    }

    @Operation(description = "List a role by name", tags = "role")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @GetMapping("/{role}")
    public Roles getbyname(@PathVariable String role) {
        return roleService.findByRole(role);
    }

    @Operation(description = "List All roles", tags = "role")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @GetMapping
    public ResponseEntity<?> getall() {
        return roleService.getall();
    }

    @Operation(description = "Save a role", tags = "role")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @PostMapping
    public ResponseEntity<?> save(@RequestBody RoleRequestDto save) {
        return roleService.save(save);
    }

    @Operation(description = "Update a role", tags = "role")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id,
                                    @RequestBody RoleRequestDto update) {
        return roleService.update(id, update);
    }

    @Operation(description = "Delete a role", tags = "role")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return roleService.delete(id);
    }
}
