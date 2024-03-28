package lab.biz.v1.service.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jdk.swing.interop.SwingInterOpUtils;
import lab.biz.v1.dto.admin.*;
import lab.biz.v1.mapper.admin.AssetCategoryMapper;
import lab.biz.v1.mapper.admin.AssetManagementMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class AssetManagementService {
    private final AssetManagementMapper assetManagementMapper;
    private final AssetCategoryMapper assetCategoryMapper;

    //조회
    public ResponseEntity<List<AssetManagement>> selectManagementList(Integer seq) {
//        //따로 가져오기
//        List<AssetManagement> managementList=assetManagementMapper.selectManagementList();
//        for (AssetManagement management : managementList) {
//            String mainCode= assetCategoryMapper.selectMainCategory(management.getMain_category()).stream().map(AssetMainCategory::getCode).collect(Collectors.joining());
//            String mainName= assetCategoryMapper.selectMainCategory(management.getMain_category()).stream().map(AssetMainCategory::getName).collect(Collectors.joining());
//            String subCode= assetCategoryMapper.selectSubCategory(null,management.getSub_category()).stream().map(AssetSubCategory::getCode).collect(Collectors.joining());
//            String subName= assetCategoryMapper.selectSubCategory(null,management.getSub_category()).stream().map(AssetSubCategory::getName).collect(Collectors.joining());
//
//            System.out.println("management.getMain_category(): "+management.getMain_category());
//            System.out.println("mainCode: "+mainCode);
//            System.out.println("mainName: "+mainName);
//            System.out.println("management.getSub_category(): "+management.getSub_category());
//            System.out.println("subCode: "+subCode);
//            System.out.println("subName: "+subName);
//
//            management.setMain_code(mainCode);
//            management.setMain_name(mainName);
//            management.setSub_code(subCode);
//            management.setSub_name(subName);
//        }
        List<AssetManagement> managementList2;
        //조인해서 가져오기
        if (seq == null) {
            managementList2=assetManagementMapper.selectManagementList2(null);
        } else {
            managementList2=assetManagementMapper.selectManagementList2(seq);
        }

        HttpStatus httpStatus;
        if (managementList2.isEmpty()) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else {
            httpStatus = HttpStatus.OK;
        }
        return ResponseEntity.status(httpStatus).body(managementList2);
    }

    //페이징 조회
    //pagehelper 사용
    public PageInfo<AssetManagement> selectManagementSearch(Integer pageNum,
                                                            Integer pageSize,
                                                            Search search) {
        log.info("===service selectManagementSearch 진입 ===");
        PageHelper.startPage(pageNum, pageSize);

//        Map<String, Object> response = new HashMap<>();  //결과 출력 위해
        Map<String, Object> params = new HashMap<>();    //목록 조회 위해
        params.put("searchKey", search.getSearchKey());

        String searchMain = null;
        String searchSub=null;

        if (search.getSearchWord().isEmpty()) {
            System.out.println("searchWord 비었음!!!");
            search.setSearchWord("-");
        }

        if (search.getSearchWord() != null && !search.getSearchWord().equals("-")) {
            String[] splitMainSub = search.getSearchWord().split("-");
            searchMain = splitMainSub[0];
            System.out.println("splitMainSub.length: "+splitMainSub.length);
            if (splitMainSub.length > 1) {
                searchSub= splitMainSub[1];
            }
        }

        //searchKey가 category인 경우 searchWord값
        System.out.println("search.getSearchKey(): "+search.getSearchKey());
        if (Objects.equals(search.getSearchKey(), "category")) {
            params.put("searchWord1", searchMain);
            params.put("searchWord2", searchSub);
        } else {
            params.put("searchWord", search.getSearchWord());
        }

        System.out.println("params: "+params);
        List<AssetManagement> searchList = assetManagementMapper.selectManagementSearch(params);

        return new PageInfo<>(searchList);
    }

    //등록 및 수정
    public ResponseEntity saveManagements(List<AssetManagementDto> request) {
        //구매년도, 대분류 seq, 소분류 seq, 제조사, 시리얼번호, user_seq, 규격, 비고, rental(N이 기본값), 작성자(write_id - "")
        System.out.println("request: "+request);

        //가장 마지막으로 입력된 code값
        int latestCode;

        for (AssetManagementDto management : request) {
            Integer requestSeq = management.getSeq();   //수정요청시 필요
            Optional<Date> requestPurchaseDate = Optional.ofNullable(management.getPurchaseDate());    //필수 입력
            Optional<Integer> requestMainCategory = Optional.ofNullable(management.getMainCategory()); //필수 입력
            Optional<Integer> requestSubCategory = Optional.ofNullable(management.getSubCategory());   //필수 입력
            Optional<String> requestManufacturer = Optional.ofNullable(management.getManufacturer());
            Optional<String> requestSerial = Optional.ofNullable(management.getSerialNum());
            Integer requestUserSeq = management.getUserSeq();
            Optional<String> requestStandard = Optional.ofNullable(management.getStandard());
            Optional<String> requestNote = Optional.ofNullable(management.getNote());
            Optional<String> requestWriteId = Optional.ofNullable(management.getWriteId());

            String requestWriteId1 = requestWriteId.orElse("");
            management.setWriteId(requestWriteId1);

            System.out.println("requestPurchaseDate.isPresent(): "+requestPurchaseDate.isPresent());  //null인 경우, 아예 전달 안된경우 false
            System.out.println("requestPurchaseDate.isEmpty(): "+requestPurchaseDate.isEmpty());      //null인 경우, 아예 전달 안된경우 true
//            System.out.println("requestPurchaseDate.get(): "+requestPurchaseDate.get());              //null인 경우, 아예 전달 안된경우 아예 확인도 못함

            //1. 필수입력 확인 (작성자(write_id - "")
            //purchase_date값이 null인 경우, purchase_date가 아예 전달이 안된 경우 모두 purchase_date = null로 받아옴
            if (!requestPurchaseDate.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("purchase_date must be present and cannot be null");
            }

            //main_category값이 null인 경우, main_category가 아예 전달이 안된 경우
            if (!requestMainCategory.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("main_category must be present and cannot be null");
            }

            //sub_category값이 null인 경우, sub_category가 아예 전달이 안된 경우
            if (!requestSubCategory.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("sub_category must be present and cannot be null");
            }

            //2. 등록된 대분류 seq인지
            //3. 등록된 소분류 seq인지
//            List<AssetSubCategory> existMainSubCategoryInfo = assetCategoryMapper.selectSubCategory(requestMainCategory.get(),requestSubCategory.get(),null);
            List<AssetCategoryInfo> existMainSubCategoryInfo = assetCategoryMapper.selectMainSub(requestMainCategory.get(),requestSubCategory.get());

            System.out.println("existMainSubCategoryInfo: "+existMainSubCategoryInfo);
            if (existMainSubCategoryInfo.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not registered category main: "+ requestMainCategory+ " sub: "+ requestSubCategory);
            }

            //4. 등록된 사용자 seq인지
            // user_seq가 null이 아닌 경우만 회원목록 검사
            if (!requestUserSeq.equals(0)) {
                Integer existUser = assetManagementMapper.findUser(requestUserSeq);
                System.out.println("existUser: "+existUser);
                if (existUser == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not registered user: "+management.getUserSeq());
                }
            }
            //5. 물품번호 - 대분류 seq와 소분류seq 전달해 max(code)구하기
            latestCode = assetManagementMapper.selectLatestCode(requestMainCategory.get(), requestSubCategory.get());
            System.out.println("main_category: "+requestMainCategory+" sub_category: "+requestSubCategory+ " latestCode: "+latestCode);
            management.setCode(++latestCode);

            //6. 등록 및 수정 구분
            if (requestSeq.equals(0)) {
                //등록 진행
                assetManagementMapper.createManagement(management);
            } else {
                //수정 진행
                System.out.println("수정요청 "+ management);
                ResponseEntity <List<AssetManagement>> existManagement = selectManagementList(requestSeq);
                System.out.println("existManagement: "+existManagement);

                //요청 자산관리가 존재하는지
                if (existManagement.getStatusCode() == HttpStatus.NOT_FOUND) {
                    return ResponseEntity.status(existManagement.getStatusCode()).body("not registered seq");
                }

                //전체 키 가져온 경우: 기존값과 같은지 확인
                boolean boardSamePurchaseDate = existManagement.getBody().stream()
                        .anyMatch(existM -> Objects.equals(existM.getPurchaseDate(),requestPurchaseDate.get()));
                boolean boardSameMainCategory = existManagement.getBody().stream()
                        .anyMatch(existM -> Objects.equals(existM.getMainCategory(),requestMainCategory.get()));
                boolean boardSameSubcategory = existManagement.getBody().stream()
                        .anyMatch(existM -> Objects.equals(existM.getSubCategory(),requestSubCategory.get()));
                boolean boardSameManufacturer = existManagement.getBody().stream()
                        .anyMatch(existM -> Objects.equals(existM.getManufacturer(),requestManufacturer.orElse("")));
                boolean boardSameSerialNum = existManagement.getBody().stream()
                        .anyMatch(existM -> Objects.equals(existM.getSerialNum(),requestSerial.orElse("")));
                boolean boardSameStandard = existManagement.getBody().stream()
                        .anyMatch(existM -> Objects.equals(existM.getStandard(),requestStandard.orElse("")));
                boolean boardSameNote = existManagement.getBody().stream()
                        .anyMatch(existM -> Objects.equals(existM.getNote(),requestNote.orElse("")));

                //4-1. 수정사항 없는 경우
                if (boardSamePurchaseDate && boardSameMainCategory && boardSameSubcategory &&
                        boardSameNote &&boardSameManufacturer &&boardSameSerialNum &&boardSameStandard) {
                    System.out.println("수정사항 없음!!!");
                    return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
                    //4-2. 수정사항 있는 경우
                } else {
                    System.out.println("수정사항 하나라도 존재!!!");
                    assetManagementMapper.updateManagement(management);
                    return ResponseEntity.status(HttpStatus.OK).build();
                }
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //대여
    public ResponseEntity rentalManagement(List<AssetManagementDto> request) {
        //seq를 담아서 옴
        for (AssetManagementDto management : request) {
            List<AssetManagement> existManagement =  selectManagementList(management.getSeq()).getBody();
            String rentalYn = existManagement.stream().map(AssetManagement::getRental).collect(Collectors.joining());

            //해당 seq의 retal이 Y면 N으로, N이면 Y로 변경
            if (rentalYn.equals("N")) {
                management.setRental("Y");
            }
            if (rentalYn.equals("Y")) {
                management.setRental("N");
            }
            System.out.println("management: "+ management);
            assetManagementMapper.rentalManagement(management);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //삭제
    public ResponseEntity deleteManagement(List<AssetManagementDto> request) {
        log.info("===service deleteManagement 진입 ===");
        List<Integer> deleteManagementSeq = new ArrayList<>();
        boolean deleteSuccess = false;

        for (AssetManagementDto subC : request) {
            System.out.println("subC: "+subC);
            int seq = subC.getSeq();
            System.out.println("seq: "+seq);
            //1. 필수입력 체크
            if (seq == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("seq must be present and cannot be null");
            }
//            ResponseEntity <List<AssetSubCategory>> existSubCategory = selectSubCategory(seq);
            ResponseEntity <List<AssetManagement>> existManagement = selectManagementList(seq);
            System.out.println("existManagement: "+existManagement);

            //2. 존재하는 자산목록인지 확인
            if (existManagement.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(existManagement.getStatusCode()).body("not registered seq");
            } else {
                deleteManagementSeq.add(seq);
            }
        }
        //삭제 처리
        if (!deleteManagementSeq.isEmpty()) {
            assetManagementMapper.deleteManagement(deleteManagementSeq);
            deleteSuccess = true;
        }

        log.info("===mapper deleteManagement 진출 ===");

        if (deleteSuccess) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }


}
