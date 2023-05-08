package zerobase.dividend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import zerobase.dividend.exception.impl.NoCompanyException;
import zerobase.dividend.model.Company;
import zerobase.dividend.model.Dividend;
import zerobase.dividend.model.ScrapedResult;
import zerobase.dividend.model.constants.CacheKey;
import zerobase.dividend.persist.CompanyRepository;
import zerobase.dividend.persist.DividendRepository;
import zerobase.dividend.persist.entity.CompanyEntity;
import zerobase.dividend.persist.entity.DividendEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FinanceService {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    // 요청이 자주 들어오는가? -> 주식정보 검색 특성 상 특정 데이터에 대한 요청이 몰리는 편
//    (회사 배당금 데이터를 캐싱해놓으면 다음 번에 요청할 때 데이터베이스에서 꺼내지 않아도 됨, 빠른 응답 가능)
    // 데이터가 자주 변경되는가? -> 데이터가 변경이 잦으면 데이터가 업데이트 될 때 마다 캐시에 있는 데이터도 삭제해주거나 업데이트 해줘야 함
    // 데이터가 자주 변경 되면 이 과정에 소요되는 시간도 고려해서 캐싱하는게 효율적일지 생각해 봐야함

    // Cacheable은 캐시에 데이터가 없을 경우에는 메소드 내부 로직 실행 후 리턴 값 캐시에 저장,
    // 캐시에 데이터 있으면 로직을 실행시키지 않고 캐시에 있는 데이터를 바로 반환해 줌
    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {
        log.info("search company -> " + companyName);
        // 1. 회사명을 기준으로 회사 정보를 조회
        CompanyEntity company = this.companyRepository.findByName(companyName)
                .orElseThrow(() -> new NoCompanyException());

        // 2. 조회된 회사 ID로 배당금 정보 조회
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(company.getId());

        // 3. 결과 조합 후 반환
//        List<Dividend> dividends = new ArrayList<>();
//        for(var entity : dividendEntities){
//            dividends.add(Dividend.builder()
//                    .date(entity.getDate())
//                    .dividend(entity.getDividend())
//                    .build());
//        }

        List<Dividend> dividends = dividendEntities.stream()
                .map(e -> new Dividend(e.getDate(), e.getDividend()))
                .collect(Collectors.toList());

        return new ScrapedResult(new Company(company.getTicker(), company.getName())
                , dividends);
    }
}
