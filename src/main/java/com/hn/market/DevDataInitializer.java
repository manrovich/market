package com.hn.market;

import com.hn.market.entity.security.Authority;
import com.hn.market.entity.security.Role;
import com.hn.market.entity.security.User;
import com.hn.market.repository.security.AuthorityRepository;
import com.hn.market.repository.security.RoleRepository;
import com.hn.market.repository.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Component
@Profile("dev")
public class DevDataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (authorityRepository.count() == 0) {
            Authority readAuthority = new Authority();
            readAuthority.setName("READ_PRIVILEGE");
            authorityRepository.save(readAuthority);

            Authority writeAuthority = new Authority();
            writeAuthority.setName("WRITE_PRIVILEGE");
            authorityRepository.save(writeAuthority);

            Role superadminRole = new Role();
            superadminRole.setName("ROLE_SUPERADMIN");
            superadminRole.setAuthorities(new HashSet<>(Arrays.asList(readAuthority, writeAuthority)));
            roleRepository.save(superadminRole);

            User superadmin = new User();
            superadmin.setUsername("admin");
            superadmin.setPassword(passwordEncoder.encode("admin"));
            superadmin.setRoles(Collections.singleton(superadminRole));
            userRepository.save(superadmin);
        }
    }
}
