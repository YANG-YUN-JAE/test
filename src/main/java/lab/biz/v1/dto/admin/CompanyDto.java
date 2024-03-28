package lab.biz.v1.dto.admin;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {
    private int seq;
    private String companyPart;
    private String companyForm;
    private String companyName;
    private String rnum;
    private String cnum;
    private String perchasePart;
    private String representName;
    private String companyCategory;
    private String companySector;
    private String establishDate;
    private String entryReason; // enum('N','R')
    private String recommandName;
    private String cnumFlag; // enum('Y','N')
    private String cnumPart;
    private String cnumfileChangename;
    private String cnumfileRealname;
    private String companyPost;
    private String companyAddr1;
    private String companyAddr2;
    private String postPost;
    private String postAddr1;
    private String postAddr2;
    private String representTel;
    private String representHandphone;
    private String representFax;
    private String representEmail;
    private String localCode;
    private String ccountryCode;
    private String birthDate;
    private String note;
    private String bcountryCode;
    private String bankCode;
    private String bnum;
    private String bnumfileChangename;
    private String bnumfileRealname;
    private String depositName;
    private String paymentAccount;
    private String taxPart;
    private String manufacturingCom; // enum('Y','N')
    private String userId;
    private LocalDateTime insertDate; // datetime
    private LocalDateTime updateDate; // datetime

}
