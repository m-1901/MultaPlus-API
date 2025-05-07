package ao.multaplus.vehicle.service;

import ao.multaplus.exception.model.ResourceNotFound;
import ao.multaplus.motorist.service.MotoristServiceImpl;
import ao.multaplus.state.entity.Status;
import ao.multaplus.state.service.StatusService;
import ao.multaplus.typeVehicle.entity.TypeVehicles;
import ao.multaplus.typeVehicle.service.TypeVehicleServiceImpl;
import ao.multaplus.vehicle.dtos.RegisteVehicleDto;
import ao.multaplus.vehicle.entity.Vehicles;
import ao.multaplus.vehicle.repository.VehicleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;

    private final MotoristServiceImpl motoristService;
    private final StatusService statusService;
    private final TypeVehicleServiceImpl typeVehicleService;

    @Override
    @Transactional
    public void registerVehicle(RegisteVehicleDto registeVehicleDto) {
        TypeVehicles typeVehicles =
                typeVehicleService.getTypeVehicles(registeVehicleDto.vehicleTypeId());
        Status status = statusService.getStatus(1L);
        motoristService.findMotorist(registeVehicleDto.bi()).ifPresentOrElse(
                motorists -> {
                    Vehicles vehicle = Vehicles.builder()
                            .motorist(motorists)
                            .plateNumber(registeVehicleDto.plateNumber())
                            .registration(registeVehicleDto.registration())
                            .typeVehicles(typeVehicles)
                            .state(status)
                            .build();
                    vehicleRepository.save(vehicle);
                }, () -> {
                    throw new RuntimeException("user not found");
                });
    }

    @Override
    @Transactional
    public void updateVehicle(String plate, RegisteVehicleDto vehicle) {
        vehicleRepository.findVehiclesByPlateNumber(plate).ifPresentOrElse(v -> {
                    boolean updated = false;
                    if (vehicle.vehicleTypeId() != null && !vehicle.vehicleTypeId().equals(
                            v.getTypeVehicles().getId())) {
                        v.setTypeVehicles(
                                typeVehicleService.getTypeVehicles(vehicle.vehicleTypeId()));
                        updated = true;
                    }
                    if (!plate.equals(v.getPlateNumber())) {
                        v.setPlateNumber(plate);
                        updated = true;
                    }
                    if (!v.getMotorist().getBi().equals(vehicle.bi())) {
                        var owner = motoristService.findMotorist(vehicle.bi()).orElseThrow();
                        v.setMotorist(owner);
                        updated = true;
                    }
                    if (vehicle.vehicleTypeId() != null && !vehicle.vehicleTypeId().equals(
                            v.getTypeVehicles().getId())) {
                        v.setTypeVehicles(
                                typeVehicleService.getTypeVehicles(vehicle.vehicleTypeId()));
                        updated = true;
                    }
                    if (updated)
                        vehicleRepository.save(v);
                },
                () -> {
                    throw new RuntimeException("update-ve");
                });
    }

    @Override
    @Transactional
    public void removeVehicle(String plate) {
        Vehicles vehicles = getVehicle(plate);
        if (vehicles.getState().getId().equals(3L))
            throw new RuntimeException("vehicle already deleted");
        vehicles.setState(statusService.getStatus(3L));
        vehicleRepository.save(vehicles);
    }

    @Override
    public Vehicles getVehicle(String plate) {
        return vehicleRepository.findVehiclesByPlateNumber(plate).orElseThrow(
                () -> new ResourceNotFound("vehicle not found"));
    }

    public List<Vehicles> getVehicles() {
        return vehicleRepository.findAll();
    }

    @PostConstruct
    void add() {
        Status status = statusService.getStatus(1);
        TypeVehicles type = typeVehicleService.getTypeVehicles(1L);
        List<Vehicles> vehicles = new ArrayList<>();

        for (int i = 1; i <= 15; i++) {
            Vehicles vehicle = Vehicles.builder()
                    .typeVehicles(type)
                    .state(status)
                    .plateNumber("ABC123" + i)
                    .registration("REG2025" + i)
                    .build();
            vehicles.add(vehicle);
        }


        vehicleRepository.saveAll(vehicles);
    }
}
