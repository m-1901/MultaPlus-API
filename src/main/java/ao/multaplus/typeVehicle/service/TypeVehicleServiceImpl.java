package ao.multaplus.typeVehicle.service;

import ao.multaplus.state.service.StatusService;
import ao.multaplus.typeVehicle.dtos.VehiclesTypeDto;
import ao.multaplus.typeVehicle.entity.TypeVehicles;
import ao.multaplus.typeVehicle.repository.TypeVehicleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeVehicleServiceImpl implements TypeVehiclesService {
    private final TypeVehicleRepository typeVehicleRepository;
    private final StatusService statusService;

    @Override
    @Transactional
    public TypeVehicles addTypeVehicles(VehiclesTypeDto typeVehicles) {
        try {
            TypeVehicles typeVehiclesEntity = TypeVehicles.builder()
                    .type(typeVehicles.type())
                    .description(typeVehicles.description())
                    .state(statusService.getStatus(1L))
                    .build();
            return typeVehicleRepository.save(typeVehiclesEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new RuntimeException("Category  type already exists");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error to saveAction Type Vehicles");
        }
    }

    @Override
    @Transactional
    public void updateTypeVehicles(Long typeVehiclesIdentifiers,
                                   VehiclesTypeDto typeVehicles) {

        typeVehicleRepository.findById(typeVehiclesIdentifiers).ifPresentOrElse(
                typeVehiclesEntity -> {
                    boolean updated = false;
                    if (typeVehicles.type() != null && !typeVehicles.type().equals(
                            typeVehiclesEntity.getType())) {
                        typeVehiclesEntity.setType(typeVehicles.type());
                        updated = true;
                    }
                    if (typeVehicles.description() != null && !typeVehicles.description().equals(
                            typeVehiclesEntity.getDescription())) {
                        typeVehiclesEntity.setDescription(typeVehicles.description());
                        updated = true;
                    }
                    if (typeVehicles.statusId() != null && !typeVehicles.statusId().equals(
                            typeVehiclesEntity.getState().getId())) {
                        typeVehiclesEntity.setState(
                                statusService.getStatus(typeVehicles.statusId()));
                        updated = true;
                    }
                    if (updated) {
                        typeVehicleRepository.save(typeVehiclesEntity);
                    }
                },
                () -> {
                    throw new RuntimeException("Type Vehicles not found");
                }
        );
    }

    @Override
    @Transactional
    public void deleteTypeVehicles(Long typeVehiclesIdentifiers) {
        typeVehicleRepository.findById(typeVehiclesIdentifiers).ifPresentOrElse(
                typeVehiclesEntity -> {
                    if (typeVehiclesEntity.getState().getId() == 3L) {
                        throw new RuntimeException("Type Vehicles already deleted");
                    }
                    typeVehiclesEntity.setState(statusService.getStatus(3L));
                    typeVehicleRepository.save(typeVehiclesEntity);
                },
                () -> {
                    throw new RuntimeException("Type Vehicles not found");
                }
        );
    }

    @Override
    @Transactional
    public TypeVehicles getTypeVehicles(Long typeVehiclesIdentifiers) {
        return typeVehicleRepository.findById(typeVehiclesIdentifiers).orElseThrow(
                () -> new RuntimeException("Type Vehicles not found"));
    }

    @Override
    public List<TypeVehicles> getAllTypeVehicles() {
        return typeVehicleRepository.findAll();
    }

    @PostConstruct
    public void migration() {
        if (typeVehicleRepository.count() == 0) {
            String[] types = {"Car", "Motorcycle", "Truck"};
            for (String type : types) {
                TypeVehicles typeVehicles = new TypeVehicles();
                typeVehicles.setType(type);
                typeVehicles.setDescription("Type of vehicle: " + type);
                typeVehicles.setState(statusService.getStatus(1L));
                typeVehicleRepository.save(typeVehicles);
            }
        }
    }

}
