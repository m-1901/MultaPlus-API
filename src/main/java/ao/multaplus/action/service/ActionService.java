package ao.multaplus.action.service;

import ao.multaplus.action.dto.ActionRequestDto;
import org.springframework.http.ResponseEntity;

public interface ActionService {
    void migration();
    ResponseEntity<?> getActions(Long actionIdentifier);
    ResponseEntity<?> getActions();
    ResponseEntity<?> saveAction(ActionRequestDto save);
    ResponseEntity<?> updateAction(long id, ActionRequestDto save);
    ResponseEntity<?> deleteAction(long id);
}
