package com.autowhouse.loginservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VerificationService {

    public static void main(String[] args) {
        SpringApplication.run(VerificationService.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(BCryptPasswordEncoder passwordEncoder,  RoleTableRepository roleTableRepository, AppUserRepository appUserRepository){
//        return args ->{
//            List<RoleTable> roleTableList = new ArrayList<>();
//            for(Role role : Role.values()){
//                RoleTable roleTable = new RoleTable();
//                roleTable.setRole(role);
//                roleTableList.add(roleTable);
//            }
//            roleTableRepository.saveAll(roleTableList);
//            List<ApplicationUser> users = new ArrayList<>();
//            Role role = Role.USER;
//            RoleTable roleTable = roleTableRepository.findByRole(role);
//            List<RoleTable> userRoles =  new ArrayList<>();
//            userRoles.add(roleTable);
//            for(int i = 0 ; i < 10 ; ++i){
//                ApplicationUser user = new ApplicationUser();
//                user.setUserRoles(userRoles);
//                user.setEmail("testuser"+(i+1)+"@gmail.com");
//                user.setPassword(passwordEncoder.encode("testuser123"));
//                users.add(user);
//            }
//            appUserRepository.saveAll(users);
//            Role role2 = Role.ADMIN;
//            RoleTable roleTable1 =  roleTableRepository.findByRole(role2);
//            userRoles.add(roleTable1);
//            ApplicationUser user = new ApplicationUser();
//            user.setEmail("asnit@gmail.com");
//            user.setPassword(passwordEncoder.encode("asnit123"));
//            user.setUserRoles(userRoles);
//            appUserRepository.save(user);
//        };
//    }

}
