package zerobase.dividend.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.dividend.exception.AbstractException;

public class EmptyTickerException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 회사 코드입니다";
    }
}
