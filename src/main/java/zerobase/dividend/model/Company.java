package zerobase.dividend.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    private String ticker;  // 종목코드
    private String name;    // 회사 이름
}
