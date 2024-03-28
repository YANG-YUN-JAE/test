package lab.biz.v1.entity.admin;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Table(name = "sales_customer_basic")
@Data
@Entity
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seq;

    @Column(name = "company_part")
    private String companyPart;

    @Column(name = "company_form")
    private String companyForm;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "rnum")
    private String rnum;

    @Column(name = "cnum")
    private String cnum;

    @Column(name = "perchase_part")
    private String perchasePart;

    @Column(name = "represent_name")
    private String representName;

    @Column(name = "company_category")
    private String companyCategory;

    @Column(name = "company_sector")
    private String companySector;

    @Column(name = "establish_date")
    private String establishDate;

    @Column(name = "entry_reason")
    private String entryReason;

    @Column(name = "recommand_name")
    private String recommandName;

    @Column(name = "cnum_flag")
    private String cnumFlag;

    @Column(name = "cnum_part")
    private String cnumPart;

    @Column(name = "cnumfile_changename")
    private String cnumfileChangename;

    @Column(name = "cnumfile_realname")
    private String cnumfileRealname;

    @Column(name = "company_post")
    private String companyPost;

    @Column(name = "company_addr1")
    private String companyAddr1;

    @Column(name = "company_addr2")
    private String companyAddr2;

    @Column(name = "post_post")
    private String postPost;

    @Column(name = "post_addr1")
    private String postAddr1;

    @Column(name = "post_addr2")
    private String postAddr2;

    @Column(name = "represent_tel")
    private String representTel;

    @Column(name = "represent_handphone")
    private String representHandphone;

    @Column(name = "represent_fax")
    private String representFax;

    @Column(name = "represent_email")
    private String representEmail;

    @Column(name = "local_code")
    private String localCode;

    @Column(name = "ccountry_code")
    private String ccountryCode;

    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "note")
    private String note;

    @Column(name = "bcountry_code")
    private String bcountryCode;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "bnum")
    private String bnum;

    @Column(name = "bnumfile_changename")
    private String bnumfileChangename;

    @Column(name = "bnumfile_realname")
    private String bnumfileRealname;

    @Column(name = "deposit_name")
    private String depositName;

    @Column(name = "payment_account")
    private String paymentAccount;

    @Column(name = "tax_part")
    private String taxPart;

    @Column(name = "manufacturing_com")
    private String manufacturingCom;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
