package ao.multaplus.gender.controller;

import ao.multaplus.gender.dtos.GenderDto;
import ao.multaplus.gender.dtos.GenderDtoList;
import ao.multaplus.gender.entity.Genders;
import ao.multaplus.gender.service.GenderServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/gender")
public class GenderController {
    private final GenderServiceImpl genderService;
    public GenderController(GenderServiceImpl genderService) {
        this.genderService = genderService;
    }

    @Operation(description = "List All gender", tags = "gender")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @GetMapping
    public ResponseEntity<?> list(GenderDtoList genderDtoList){
        return genderService.list();
    }

    @Operation(description = "Find a gender", tags = "gender")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @GetMapping("/{id}")
    public Optional<Genders> buscar(@PathVariable long id){
        return genderService.findone(id);
    }

    @Operation(description = "Save a gender", tags = "gender")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @PostMapping
    public ResponseEntity<?> save(@RequestBody GenderDto genderDto){
        return genderService.save(genderDto);
    }

    @Operation(description = "Delete a gender", tags = "gender")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id){
        return genderService.delete(id);
    }

    @Operation(description = "Edit a gender", tags = "gender")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id,@RequestBody GenderDto genderDtoList){
        return genderService.update(id,genderDtoList);
    }
}
