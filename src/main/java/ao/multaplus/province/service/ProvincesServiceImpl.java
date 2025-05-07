package ao.multaplus.province.service;

import ao.multaplus.exception.model.ResourceNotFound;
import ao.multaplus.province.dtos.ProviceUpdateDto;
import ao.multaplus.province.dtos.ProvinceDto;
import ao.multaplus.province.entity.Province;
import ao.multaplus.province.repository.ProvinceRepository;
import ao.multaplus.province.response.ProvinceResponse;
import ao.multaplus.state.entity.Status;
import ao.multaplus.state.repository.StatusRepository;
import ao.multaplus.state.service.StatusService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvincesServiceImpl implements ProvincesService {

    private final ProvinceRepository repository;
    private final StatusRepository statusRepository;
    private final StatusService statusService;
    private final ProvinceRepository provinceRepository;


    @Override
    public ProvinceResponse create(ProvinceDto requestdtos) {
        if (repository.existsByProvince(requestdtos.province())) {
            throw new RuntimeException("Province already exists");
        }

        Status state = statusRepository.findById(1L)
                .orElse(null);
        Province province = new Province();
        province.setProvince(requestdtos.province());
        province.setState(state);

        repository.save(province);
        return toResponseDTO(province);
    }

    @Override
    public ProvinceResponse update(Long id, ProviceUpdateDto requestdtos) {
        Province existingProvince = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Province not found"));
        if (repository.existsByProvince(requestdtos.province())) {
            throw new RuntimeException(
                    "Another province with the same name already exists!");
        }

        Status state = statusRepository.findById(requestdtos.state().id())
                .orElse(null);

        existingProvince.setState(state);
        existingProvince.setProvince(requestdtos.province());
        repository.save(existingProvince);
        return toResponseDTO(existingProvince);
    }

    @Override
    public void delete(Long id) {
        Province province = getProvince(id);
        Status deletedStatus = statusService.getStatus(3L);

        // Atualizar o estado da província
        province.setState(deletedStatus);
        // Salvar as alterações
        repository.save(province);
    }

    @Override
    public ProvinceResponse findById(Long provinceIdentifier) {

        return provinceRepository.getProvince(provinceIdentifier).orElseThrow(
                () -> new ResourceNotFound(
                        "Province not found"));

    }

    @Override
    public List<ProvinceResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }


    //metodo para a response
    private ProvinceResponse toResponseDTO(Province province) {
        return new ProvinceResponse(
                province.getId(),
                province.getProvince(),
                null);//new StateSenderDto(province.getState().getId())
    }

    @Override
    @PostConstruct
    public void migration() {
        if (repository.count() == 0) {
            String[] array = {"Bengo", "Benguela", "Bié",
                    "Cabinda", "Cuando", "Cubango", "Cuanza Norte",
                    "Cuanza Sul", "Cunene", "Huambo", "Huíla", "Luanda",
                    "Lunda Norte", "Lunda Sul", "Malanje", "Moxico",
                    "Namibe", "Uíge", "Zaire", "Icolo e Bengo", "Moxico Leste", "Cuando"};

            for (int i = 0; i < array.length; i++) {
                Province province = new Province();
                province.setProvince(array[i]);
                province.setState(statusService.getStatus(1L));
                repository.save(province);
            }
        }
    }

    public Province getProvince(Long provinceIdentifier) {
        return repository.findById(provinceIdentifier).orElseThrow(() -> new ResourceNotFound("Province not found"));
    }
}
