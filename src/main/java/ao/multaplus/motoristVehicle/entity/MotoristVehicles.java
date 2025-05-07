package ao.multaplus.motoristVehicle.entity;

import ao.multaplus.model.AbstractModel;
import ao.multaplus.motorist.entity.Motorists;
import ao.multaplus.state.entity.Status;
import ao.multaplus.typeVehicle.entity.TypeVehicles;
import ao.multaplus.vehicle.entity.Vehicles;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MotoristVehicles extends AbstractModel {

    @ManyToOne
    @JoinColumn(name = "state_id")
    @JsonIgnoreProperties("MotoristVehicles")
    private Status state;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    @JsonIgnoreProperties("MotoristVehicles")
    private Vehicles Vehicles;

    @ManyToOne
    @JoinColumn(name = "motorist_id")
    @JsonIgnoreProperties("MotoristVehicles")
    private Motorists motorist;
}
