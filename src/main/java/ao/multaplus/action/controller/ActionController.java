package ao.multaplus.action.controller;

import ao.multaplus.action.dto.ActionRequestDto;
import ao.multaplus.action.service.ActionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/actions")
@RequiredArgsConstructor
public class ActionController {

    private final ActionService actionService;

    @Operation(description = "List an action", tags = "action")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @GetMapping("/{actionIdentifier}")
    public ResponseEntity<?> getAction(@PathVariable Long actionIdentifier){
        return actionService.getActions(actionIdentifier);
    }

    @Operation(description = "All actions", tags = "action")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @GetMapping
    public ResponseEntity<?> getActions(){
        return actionService.getActions();
    }

    @Operation(description = "Save an action", tags = "action")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @PostMapping
    public ResponseEntity<?> save(@RequestBody ActionRequestDto save){
        return actionService.saveAction(save);
    }

    @Operation(description = "Update an action", tags = "action")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @PutMapping(" /{actionIdentifier}")
    public ResponseEntity<?> update(@PathVariable long actionIdentifier,@RequestBody ActionRequestDto update){
        return actionService.updateAction(actionIdentifier,update);
    }

    @Operation(description = "Delete an action", tags = "action")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @DeleteMapping("/{actionIdentifier}")
    public ResponseEntity<?> delete(@PathVariable Long  actionIdentifier){
        return actionService.deleteAction(actionIdentifier);
    }
}
