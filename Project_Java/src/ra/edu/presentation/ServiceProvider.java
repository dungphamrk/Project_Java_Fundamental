package ra.edu.presentation;

import ra.edu.business.service.application.ApplicationService;
import ra.edu.business.service.application.ApplicationServiceImp;
import ra.edu.business.service.candidateService.CandidateService;
import ra.edu.business.service.candidateService.CandidateServiceImp;
import ra.edu.business.service.technology.TechnologyService;
import ra.edu.business.service.technology.TechnologyServiceImp;
import ra.edu.business.service.userService.UserService;
import ra.edu.business.service.userService.UserServiceImp;

public class ServiceProvider {
    public static final UserService userService = new UserServiceImp();
    public static final CandidateService candidateService = new CandidateServiceImp();
    public static  final TechnologyService technologyService = new TechnologyServiceImp();
    public static  final ApplicationService applicationService = new ApplicationServiceImp();
}
