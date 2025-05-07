package ao.multaplus.fineInfringement.Service;

import ao.multaplus.fine.entity.Fines;
import ao.multaplus.fine.repository.FineRepository;
import ao.multaplus.fineInfringement.entity.FineInfringements;
import ao.multaplus.fineInfringement.repository.FineInfringementRepository;
import ao.multaplus.infringement.entity.Infringements;
import ao.multaplus.state.entity.Status;
import ao.multaplus.state.repository.StatusRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FineInfrigmentServiceImpl implements FineInfrigmentService {

    private final FineInfringementRepository fineInfringementRepository;
    private final FineRepository fineRepository;
    private final StatusRepository statusRepository;
    public FineInfrigmentServiceImpl(FineInfringementRepository fineInfringementRepository, FineRepository fineRepository, StatusRepository statusRepository) {
        this.fineInfringementRepository = fineInfringementRepository;
        this.fineRepository = fineRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    public ResponseEntity<List<FineInfringements>> searchInfrigmentsByFine(Long id) {
        List<FineInfringements> fineInfringements= fineInfringementRepository.findByFineId(id);
        return ResponseEntity.ok(fineInfringements);
    }
    @Override
    public ResponseEntity<?> savefineinfrigments(Long id,List<Infringements> infringements){
        Optional<Fines> fines= fineRepository.findById(id);
        Fines fines1=fines.get();
        Optional<Status> status=statusRepository.findById(1);
        Status status1= status.get();
        for (Infringements infringements1:infringements){
            FineInfringements fineInfringements=new FineInfringements();
            fineInfringements.setFine(fines1);
            fineInfringements.setInfringement(infringements1);
            fineInfringements.setState(status1);
            fineInfringementRepository.save(fineInfringements);
        }
        return new ResponseEntity<>("Saved", HttpStatusCode.valueOf(201));
    }
}
