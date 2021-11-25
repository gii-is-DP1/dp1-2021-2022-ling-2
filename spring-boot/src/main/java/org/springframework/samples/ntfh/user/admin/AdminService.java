package org.springframework.samples.ntfh.user.admin;

import org.springframework.samples.ntfh.user.UserRepository;
import org.springframework.samples.ntfh.user.authorities.AuthoritiesService;
import org.springframework.stereotype.Service;

/**
 * @author JStockwell
 */
@Service
public class AdminService {
    private UserRepository userRepository;
	private AuthoritiesService authoritiesService;
}
