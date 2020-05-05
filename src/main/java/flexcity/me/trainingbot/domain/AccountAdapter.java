package flexcity.me.trainingbot.domain;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class AccountAdapter extends User {

    private Account account;

    public AccountAdapter(Account account) {
        super(account.getUserId(), account.getPassword(), account.getAuthorities());
        this.account = account;
    }
//    public AccountAdapter(Account account) {
//        super(account.getUserId(), account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_" +account.getRoles())));
//    }
}
