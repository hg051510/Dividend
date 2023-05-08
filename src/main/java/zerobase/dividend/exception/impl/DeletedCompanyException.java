package zerobase.dividend.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.dividend.exception.AbstractException;

public class DeletedCompanyException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 삭제했거나 존재하지 않는 회사입니다";
    }
}
