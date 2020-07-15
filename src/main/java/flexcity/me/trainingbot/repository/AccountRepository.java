package flexcity.me.trainingbot.repository;

import flexcity.me.trainingbot.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUserId(String userId);

    @Query(value = "select a from Account a join fetch a.exercises e where a.userId = ?1")
    Optional<Account> findByUserIdForExercise(String userId);
}
