package lab.biz.v1.controllers.admin;

import com.github.pagehelper.PageInfo;
import lab.biz.v1.dto.admin.CompanyDto;
import lab.biz.v1.entity.admin.CompanyEntity;
import lab.biz.v1.service.admin.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class CompanyController {
    private final CompanyService companyService;
    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

//    @GetMapping("/api/v1/company/{id}")
//    public CompanyDto companyDetail(@PathVariable("id") int seq) {
//        log.info("seq: {}", seq);
//        return companyService.findOne(seq);
//    }
    @GetMapping("/api/v1/company/{id}")
    public ResponseEntity<CompanyEntity> getCompanyById(@PathVariable("id") Integer seq){

        CompanyEntity companyEntity = companyService.findOne(seq);
        return ResponseEntity.ok(companyEntity);
//        return ResponseEntity.ok().headers(headers).body(companyEntity;
    }

    @GetMapping("/api/v1/company/number-check")
    public String numberCheck(
            @RequestParam(required = true, value = "companyNum") String companyNum
    ) {
        return companyService.checkNumber(companyNum);
    }
    @GetMapping("/api/v1/company-list")
    public PageInfo<CompanyDto> getAllCompanies(
            @RequestParam(defaultValue = "0", value="pageNum") int pageNum,
            @RequestParam(defaultValue = "10", value="pageSize") int pageSize,
            @RequestParam(defaultValue = "1", value="type") int type
    ) {
        return companyService.getAllCompanies(pageNum, pageSize, type);
    }

    @PostMapping("/api/v1/company")
    public int registerCompany(@RequestBody CompanyDto companyDto){
        log.info(String.valueOf(companyDto));
        return 2;
    }
}
