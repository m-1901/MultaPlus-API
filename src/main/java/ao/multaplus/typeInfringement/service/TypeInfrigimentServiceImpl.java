package ao.multaplus.typeInfringement.service;

import ao.multaplus.state.dtos.StateSenderDto;
import ao.multaplus.state.entity.Status;
import ao.multaplus.state.repository.StatusRepository;
import ao.multaplus.state.service.StatusService;
import ao.multaplus.typeInfringement.dtos.TypeInfringementsDto;
import ao.multaplus.typeInfringement.dtos.TypeInfringementsUpdateDto;
import ao.multaplus.typeInfringement.entity.TypeInfringements;
import ao.multaplus.typeInfringement.repository.TypeInfrigimentsRepository;
import ao.multaplus.typeInfringement.response.TypeInfringementsResponse;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TypeInfrigimentServiceImpl implements TypeInfrigimentService {

   private final TypeInfrigimentsRepository repository;

    private final StatusRepository statusRepository;
    private final StatusService  statusService;

    @Override
    public TypeInfringementsResponse create(TypeInfringementsUpdateDto requestdtos) {

        // Verifica se o tipo de infração já existe no repositório
        if (repository.existsByType(requestdtos.type())) {
            throw new RuntimeException("Infringement type already exists");
        }

        // Cria um novo objeto TypeInfringements
        TypeInfringements infringement = new TypeInfringements();

        // Busca o estado padrão
        Status state = statusRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Default state not found"));

        // Define os valores no objeto de infração
        infringement.setType(requestdtos.type());
        infringement.setDescription(requestdtos.description());
        infringement.setPrice(Float.valueOf(requestdtos.price()));
        infringement.setState(state);

        // Salva a infração no repositório e retorna como DTO
        return toDto(repository.save(infringement));
    }

    @Override
    public TypeInfringementsResponse findById(Long id) {
        TypeInfringements infringement = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TypeInfringement not found"));
        return toDto(infringement);
    }

    @Override
    public List<TypeInfringementsResponse> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public TypeInfringementsResponse update(Long id, TypeInfringementsUpdateDto requestdtos) {
        TypeInfringements infringement = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TypeInfringement not found"));
        Status status = statusRepository.findById(requestdtos.state().id())
                .orElseThrow(() -> new EntityNotFoundException("Status not found"));

        infringement.setType(infringement.getType());
        infringement.setDescription(infringement.getDescription());
        infringement.setPrice(infringement.getPrice());
        infringement.setState(status);

        return toDto(repository.save(infringement));
    }


    @Override
    public void delete(Long id) {
        TypeInfringements infringement = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TypeInfringement not found"));

        // Alterar o estado para "eliminado"
        Status eliminatedStatus = statusRepository.findById(3L)
                .orElseThrow(() -> new EntityNotFoundException("Status 'eliminated' not found"));

        infringement.setState(eliminatedStatus);
        repository.save(infringement);
    }


   @Override
   @PostConstruct
    public void migration() {
       Status status = statusService.getStatus(1L);

     if (repository.count() == 0) {
         String[] types = {
                 "Excesso de velocidade",
                 "Estacionamento em áreas proibidas",
                 "Condução sem licença",
                 "Passagem no sinal vermelho",
                 "Incumprimento de regulamentos internos",
                 "Uso indevido de recursos da empresa",
                 "Não cumprimento de prazos",
                 "Descarte inadequado de resíduos",
                 "Poluição de corpos d'água",
                 "Desmatamento ilegal",
                 "Evasão fiscal",
                 "Não emissão de faturas",
                 "Declarações falsas ou incompletas",
                 "Desordem pública",
                 "Furto menor",
                 "Agressões leves"
         };
         String[] descriptions = {
                 "Ultrapassar o limite de velocidade permitido",
                 "Estacionar veículos em áreas proibidas",
                 "Conduzir um veículo sem licença válida",
                 "Não parar no sinal de trânsito vermelho",
                 "Violar regras organizacionais internas",
                 "Usar recursos para fins pessoais",
                 "Não cumprir prazos estabelecidos",
                 "Descartar resíduos de forma inadequada",
                 "Contaminar rios, lagos ou outros corpos d'água",
                 "Desmatar florestas sem autorização",
                 "Evitar impostos através de informações financeiras falsas ou omissas",
                 "Não emitir faturas necessárias em transações comerciais",
                 "Fornecer informações falsas ou incompletas em documentos legais",
                 "Causar distúrbios ou desordem pública",
                 "Cometer furtos menores de propriedade",
                 "Envolver-se em agressões físicas leves sem ferimentos graves"
         };
         float[] price = {
                 25000,  // Excesso de velocidade (25,000 Kz)
                 15000,  // Estacionamento em áreas proibidas
                 50000,  // Condução sem licença
                 30000,  // Passagem no sinal vermelho
                 10000,  // Incumprimento de regulamentos internos
                 20000,  // Uso indevido de recursos da empresa
                 15000,  // Não cumprimento de prazos
                 35000,  // Descarte inadequado de resíduos
                 75000,  // Poluição de corpos d'água
                 100000, // Desmatamento ilegal
                 150000, // Evasão fiscal
                 50000,  // Não emissão de faturas
                 80000,  // Declarações falsas ou incompletas
                 20000,  // Desordem pública
                 30000,  // Furto menor
                 25000   // Agressões leves
         };

         for (int i = 0; i < types.length; i++) {
             TypeInfringements infringement = new TypeInfringements();
             infringement.setType(types[i]);
             infringement.setDescription(descriptions[i]);
             infringement.setPrice(price[i]);
             infringement.setState(status);
             repository.save(infringement);
         }
     }
  }

    @Override
    public Float getPrice(Long id) {
       Optional<TypeInfringements> typeInfringements=repository.findById(id);
       Float price=typeInfringements.orElseThrow().getPrice();
        return price;
    }

    private TypeInfringementsResponse toDto(TypeInfringements infringement) {
        return new TypeInfringementsResponse(
                infringement.getId(),
                infringement.getType(),
                infringement.getDescription(),
                infringement.getPrice(),
                new StateSenderDto(infringement.getState().getId(), infringement.getState().getState())
        );
    }


}
