package ao.multaplus.state.controller;

import ao.multaplus.state.dtos.StateSaveDto;
import ao.multaplus.state.entity.Status;
import ao.multaplus.state.service.StatusServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@org.springframework.stereotype.Controller("statusController")

@RequestMapping("/status")
@RestController
@RequiredArgsConstructor
public class StatusController {

    public final StatusServiceImpl statusService;

    @Operation(description = "List All Status", tags = "status")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @GetMapping
    public ResponseEntity<?> list() {
        return statusService.list();
    }

    @Operation(description = "List Status By ID", tags = "status")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @GetMapping("/{id}")
    public Optional<Status> listbyid(@PathVariable long id) {
        return statusService.findone(id);
    }


    @Operation(description = "Save New Status", tags = "status")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @PostMapping
    public ResponseEntity<?> save(@RequestBody StateSaveDto state) {
        return statusService.save(state);
    }

    @Operation(description = "Update Status", tags = "status")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id,
                                    @RequestBody StateSaveDto state) {
        return statusService.update(id, state);
    }

    @Operation(description = "Update Status", tags = "status")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return statusService.delete(id);
    }

}