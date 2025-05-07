package ao.multaplus.payment.controller;

import ao.multaplus.payment.dtos.RequestPayment;
import ao.multaplus.payment.service.PaymentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentServiceImpl paymentService;
    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(description = "List All payment", tags = "payment")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @GetMapping
    public ResponseEntity<?> list(){
        return paymentService.list();
    }

    @Operation(description = "List payment By ID", tags = "payment")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @GetMapping("{id}")
    public ResponseEntity<?> listone(@PathVariable long id){
        return paymentService.listone(id);
    }

    @Operation(description = "Save New payment", tags = "payment")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid RequestPayment payment){
        return paymentService.save(payment);
    }

    @Operation(description = "Save New payment By Fine ID", tags = "payment")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @PostMapping("{id}")
    public ResponseEntity<?> createpaymentbyfine(@PathVariable Long id){
        return paymentService.createnewpayment(id);
    }

    @Operation(description = "Update payment", tags = "payment")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id,@RequestBody @Valid RequestPayment payment){
        return paymentService.update(id,payment);
    }

    @Operation(description = "Delete Payment", tags = "payment")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id){
        return paymentService.delete(id);
    }
}
