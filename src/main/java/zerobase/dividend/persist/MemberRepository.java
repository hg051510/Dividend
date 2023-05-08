package zerobase.dividend.persist;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import zerobase.dividend.model.MemberEntity;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // 회원가입 할 때 기존 회원인지 확인
    @EntityGraph(attributePaths = {"roles"})
    Optional<MemberEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}
