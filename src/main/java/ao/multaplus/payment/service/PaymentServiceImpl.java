package ao.multaplus.payment.service;
import ao.multaplus.fine.entity.Fines;
import ao.multaplus.fine.repository.FineRepository;
import ao.multaplus.fineInfringement.Service.FineInfrigmentServiceImpl;
import ao.multaplus.fineInfringement.entity.FineInfringements;
import ao.multaplus.payment.dtos.RequestPayment;
import ao.multaplus.payment.entity.Payments;
import ao.multaplus.payment.repository.PaymentRepository;
import ao.multaplus.statePayment.entity.StatusPayment;
import ao.multaplus.statePayment.repository.StatusPaymentRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentServiceImpl paymentService;
    private final FineRepository fineRepository;
    private final FineInfrigmentServiceImpl fineInfrigmentService;
    private final StatusPaymentRepository statusPaymentRepository;
    public PaymentServiceImpl(PaymentRepository paymentRepository,@Lazy PaymentServiceImpl paymentService, FineRepository fineRepository, FineInfrigmentServiceImpl fineInfrigmentService, StatusPaymentRepository statusPaymentRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentService = paymentService;
        this.fineRepository = fineRepository;
        this.fineInfrigmentService = fineInfrigmentService;
        this.statusPaymentRepository = statusPaymentRepository;
    }

    @Override
    public ResponseEntity<?> createnewpayment(long id) {
        String reference= paymentService.generatereference();
        Optional<Fines> fines=fineRepository.findById(id);
        if (fines.isEmpty()){
            return new ResponseEntity<>("Fine not Found",HttpStatus.NOT_FOUND);
        }

        List<FineInfringements> fineInfringements= fineInfrigmentService.searchInfrigmentsByFine(id).getBody();
       if (fineInfringements.isEmpty()){
           return new ResponseEntity<>("The list is empty",HttpStatus.BAD_REQUEST);
       }
        float totalPrice=0;
        for (FineInfringements fineInfringements1:fineInfringements){
            totalPrice += fineInfringements1.getInfringement().getTypeInfringements().getPrice();
        }
        StatusPayment statusPayment=statusPaymentRepository.findById(1);

        Payments payments=new Payments();
        payments.setFine(fines.orElseThrow());
        payments.setReference(reference);
        payments.setStatusPayment(statusPayment);
        payments.setPrice(totalPrice);
        paymentRepository.save(payments);
        return ResponseEntity.ok("Saved");
    }



    @Override
    public ResponseEntity<?> list() {
        return new ResponseEntity<>(paymentRepository.findAll(), HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> listone(long id) {
        Optional<Payments> payments=paymentRepository.findById(id);
        return new ResponseEntity<>(payments,HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> save(RequestPayment payment) {
        try {
            String reference=generatereference();

            Payments payments=new Payments();
            payments.setReference(reference);
            payments.setStatusPayment(payment.statusPayment());
            payments.setFine(payment.fine());
            payments.setPrice(payment.price());
            paymentRepository.save(payments);
            return new ResponseEntity<>("Payment saveAction",HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> update(long id, RequestPayment payment) {
        try {
            Optional<Payments> payments=paymentRepository.findById(id);
            if (payments.isEmpty()){
                return new ResponseEntity<>("Cannot be blank",HttpStatus.BAD_REQUEST);
            }
            payments.orElseThrow().setId(id);
            payments.orElseThrow().setStatusPayment(payment.statusPayment());
            payments.orElseThrow().setFine(payment.fine());
            payments.orElseThrow().setPrice(payment.price());
            Payments payments1=payments.get();
            paymentRepository.save(payments1);
            return new ResponseEntity<>("Payment Updated",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> delete(long id) {
        try {
            Optional<Payments> payments=paymentRepository.findById(id);
            paymentRepository.delete(payments.orElseThrow());
            return new ResponseEntity<>("Deleted with success",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Error deleting or payments doesn`t exist",HttpStatus.NO_CONTENT);
        }

    }

    private String generatereference(){
        final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final int TAMANHO_CODIGO = 6;

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(TAMANHO_CODIGO);

        for (int i = 0; i < TAMANHO_CODIGO; i++) {
            int randomIndex = random.nextInt(CARACTERES.length());
            sb.append(CARACTERES.charAt(randomIndex));
        }
        return sb.toString();
    }
}
