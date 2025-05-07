package ao.multaplus.province.repository;

import ao.multaplus.province.dtos.ProvinceDto;
import ao.multaplus.province.entity.Province;
import ao.multaplus.province.response.ProvinceResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
    String QUERY_GET_PROVINCE_DTO= "SELECT new ao.multaplus.province.response" +
            ".ProvinceResponse(p.id, p" +
            ".province, new ao.multaplus.state.dtos.StateSenderDto(p.state.id,p.state" +
            ".state)) FROM " +
            "Province p ";
    Optional<Province> findById(Long id);

    boolean existsByProvince(String province);

    @Query(QUERY_GET_PROVINCE_DTO+"WHERE p.id =:provinceIdentifier")
    Optional<ProvinceResponse> getProvince(@Param("provinceIdentifier") Long provinceIdentifier);
}
