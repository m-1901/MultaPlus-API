package ao.multaplus.action.service;

import ao.multaplus.action.dto.ActionRequestDto;
import ao.multaplus.action.entity.Actions;
import ao.multaplus.action.repository.ActionRepository;
import ao.multaplus.exception.model.NothingToUpdateException;
import ao.multaplus.exception.model.ResourceNotFound;
import ao.multaplus.state.entity.Status;
import ao.multaplus.state.service.StatusService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService {
    
    private final ActionRepository repository;
    private final StatusService statusService;
    private final ActionRepository actionRepository;

    @Override
    @PostConstruct
    public void migration() {
        Status status = statusService.getStatus(1L);
        if (repository.count() == 0) {
            String[] array ={"create", "update", "delete"};
            for (int i = 0; i < array.length; i++) {
                Actions action = new Actions();
                action.setAction(array[i]);
                action.setState(status);
                repository.save(action);
            }
        }
    }

    @Override
    public ResponseEntity<Actions> getActions(Long actionIdentifier) {
        Actions actionResponse= repository.findById(actionIdentifier).orElseThrow(()-> new ResourceNotFound("Action with identifier = % not found".formatted(actionIdentifier)));
        return new ResponseEntity<>(actionResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getActions() {
        List<Actions> actions = repository.findAll();
        return new ResponseEntity<>(actions,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> saveAction(ActionRequestDto save) {
        Actions actions=new Actions();
        actions.setAction(save.action());
        actions.setDescription(save.description());
        repository.save(actions);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> updateAction(long actionIdentifier, ActionRequestDto save) {
        Actions actions= getAction(actionIdentifier);
        boolean isUpdated = false;
        if(save.action()!=null && save.action().equals(actions.getAction())){
            actions.setAction(save.action());
            isUpdated = true;
        }
        if(save.description() != null && save.action().equals(actions.getDescription())){
            actions.setAction(save.action());
            isUpdated = true;
        }

        if(isUpdated)
            repository.save(actions);
        else
            throw new NothingToUpdateException("These Nothing to update in action with " +
                    "identifier=%s".formatted(actionIdentifier));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> deleteAction(long actionIdentifier) {
        Status status = statusService.getStatus(3L);
        Actions actions = getAction(actionIdentifier);
        if (actions != null && actions.getState().equals(status))
            throw new NothingToUpdateException("These Nothing to update in action with " + "identifier=%s".formatted(actionIdentifier));
        actions.setState(status);
        try {
            actionRepository.save(actions);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Actions getAction(Long actionIdentifier){
        return repository.findById(actionIdentifier).orElseThrow(()-> new ResourceNotFound("Action with identifier = % not found".formatted(actionIdentifier)));
    }
}
