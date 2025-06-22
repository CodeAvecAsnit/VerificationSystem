package com.techdgnep.login.Cotroller;

import com.techdgnep.login.DataModel.Database.FinalUser;
import com.techdgnep.login.DataModel.External.VerificationRequest;
import com.techdgnep.login.Service.GetCode;
import com.techdgnep.login.Service.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/signup")
public class MailController {

    private final GetCode getCode;
    private final Manager manager;

    @Autowired
    public MailController(GetCode getCode, Manager manager) {
        this.getCode = getCode;
        this.manager = manager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody FinalUser newUser) {
        try {
            int verCode = getCode.GetMailCode(newUser.getEmail());
            if(manager.InsertUser(newUser,verCode)) {
                return ResponseEntity.ok("Verification code sent to email: " + newUser.getEmail());
            }else {
                return ResponseEntity.internalServerError().body("There was some internal Error");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().body("Request was unsuccessful");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestBody VerificationRequest request) {
        try {
            FinalUser user = manager.checkEntry(request.getEmail(), request.getVerificationCode());
            if(user!=null){
                user.setPasscode(manager.hash(user.getPasscode()));
                Long id = manager.Save(user);
                return (id!=-1)?
                      ResponseEntity.ok(user.getUserName()+"was successfully registered with Id "+id):
                        ResponseEntity.badRequest().body("Bad Request");
            }else return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }
}
