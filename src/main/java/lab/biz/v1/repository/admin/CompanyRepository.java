package lab.biz.v1.repository.admin;

import lab.biz.v1.entity.admin.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;


//dao
//자동 bean등록
// @Repository 생략
public interface CompanyRepository extends JpaRepository <CompanyEntity, Integer> {
//    private final EntityManager em;
    CompanyEntity findBySeq(int seq);
    boolean existsByCnum(String cnum);
}
