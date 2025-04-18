package ra.edu.presentation;

import ra.edu.business.service.candidateService.CandidateService;
import ra.edu.business.service.candidateService.CandidateServiceImp;
import ra.edu.business.service.userService.UserService;
import ra.edu.business.service.userService.UserServiceImp;

public class ServiceProvider {
    public static final UserService userService = new UserServiceImp();
    public static final CandidateService candidateService = new CandidateServiceImp();

}
