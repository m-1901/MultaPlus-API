package ao.multaplus.statePayment.controller;

import ao.multaplus.statePayment.dtos.StatusPaymentDTO;
import ao.multaplus.statePayment.dtos.StatusPaymentSaveDTO;
import ao.multaplus.statePayment.entity.StatusPayment;
import ao.multaplus.statePayment.service.Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/status-payments")
@RequiredArgsConstructor
public class StatusPaymentController {


    public final Service statusPayment;

    @Operation(description = "List All StatusPayment", tags = "statuspayment")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @GetMapping
    public ResponseEntity<?> list(){
        return statusPayment.list();
    }

    @Operation(description = "List All StatusPayment By ID", tags = "statuspayment")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @GetMapping("/{id}")
    public ResponseEntity<?> listbyid(@PathVariable long id){
        return statusPayment.findone(id);
    }

    @Operation(description = "Save New Status", tags = "statuspayment")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @PostMapping
    public ResponseEntity<?> save(@RequestBody StatusPaymentSaveDTO state){
        return statusPayment.save(state);
    }

    @Operation(description = "Update Status", tags = "statuspayment")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody StatusPaymentSaveDTO state){
        return statusPayment.update(id,state);
    }

    @Operation(description = "Delete StatusPayment", tags = "statuspayment")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id){
        return statusPayment.delete(id);
    }
}
