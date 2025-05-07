package ao.multaplus.state.service;

import ao.multaplus.exception.model.ResourceNotFound;
import ao.multaplus.state.dtos.StateDto;
import ao.multaplus.state.dtos.StateSaveDto;
import ao.multaplus.state.entity.StatusMensagem;
import ao.multaplus.state.entity.Status;
import ao.multaplus.state.repository.StatusRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {


    @Override
    @PostConstruct
    public void migration() {
        if (repository.count() == 0) {
            String[] array ={"active", "inactive", "eliminated"};

            for (int i = 0; i < array.length; i++) {
                Status state = new Status();
                state.setState(array[i]);
                repository.save(state);
            }
        }
    }

    @Autowired
    public StatusMensagem sms;

    @Autowired
    private StatusRepository repository;


    @Override
    public ResponseEntity<?> list(){
        List<Status> all = repository.findAll();
        return new ResponseEntity<>( all, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> update(long id, StateSaveDto state){
       Optional<Status> status=repository.findById(id);
       if (status == null){
           sms.setMensagem("State cannot be empty");
           return new ResponseEntity<>(sms, HttpStatus.NOT_FOUND);
       }
       status.orElseThrow().setState(state.state());
       status.orElseThrow().setDescription(state.description());
       status.orElseThrow().setId(id);
       Status stat=status.get();
       if(status.orElseThrow().getState().equals("")){
            sms.setMensagem("State Cannot be empty");
            return new ResponseEntity<>(sms, HttpStatus.BAD_REQUEST);
        }else {
            sms.setMensagem("Updated with success");
            repository.save(stat);
            return new ResponseEntity<>(sms, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<?> save(StateSaveDto state){
        Status status=new Status();
        status.setState(state.state());
        status.setDescription(state.description());
        if(status.getState().equals("")){
            sms.setMensagem("Cannot be empty");
            return new ResponseEntity<>(sms, HttpStatus.BAD_REQUEST);
        }else {
            sms.setMensagem("Saved with success");
            repository.save(status);
            return new ResponseEntity<>(sms, HttpStatus.CREATED);
        }
    }

    @Override
    public Optional<Status> findone(long id){
        Optional<Status> status = repository.findById(id);
        return status;
    }

    @Override
    public ResponseEntity<?> delete(long id){
        Optional<Status> status= Optional.of(new Status());
        status=repository.findById(id);
        if (status==null){
            return new ResponseEntity<>("Not found",HttpStatus.OK);
        }
        Status status1=new Status();
        status1.setId(id);
        status1.setState(status.orElseThrow().getState());
        status1.setDescription(status.orElseThrow().getDescription());
       repository.delete(status1);
        return new ResponseEntity<>("Deleted with success",HttpStatus.OK);
    }

    @Override
    public Status getStatus(long id) {
        return   repository.findById(id).orElseThrow(()->{
            throw new ResourceNotFound("status not found");
        });
    }
}
