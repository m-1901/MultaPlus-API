package ao.multaplus.typeInfringement.response;

import ao.multaplus.state.dtos.StateSenderDto;

public record TypeInfringementsResponse(
        Long id,
        String type,
        String description,
        double price,
        StateSenderDto status
) {
}
