package com.techdgnep.login.Cotroller;

import com.techdgnep.login.DataModel.Database.FinalUser;
import com.techdgnep.login.DataModel.External.VerificationRequest;
import com.techdgnep.login.Service.GetCode;
import com.techdgnep.login.Service.Manager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(MailController.class);


    @Autowired
    public MailController(GetCode getCode, Manager manager) {
        this.getCode = getCode;
        this.manager = manager;
    }

    @Operation(summary = "save user",description = "Send mail code to user and also stores the user in temporarily for registration")
    @ApiResponses(value ={
    @ApiResponse(responseCode = "200",description = "Code successfully sent and user is also stored in the server memory "),
    @ApiResponse(responseCode = "404",description = "There was problem while sending the mail or storing the user in the memory")})
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody FinalUser newUser) {
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

    @Operation(summary = "Validates verification code",description = "Stores the user in the database when the user is verified with help of code ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Code expired or incorrect"),
            @ApiResponse(responseCode = "500", description = "DB error")
    })
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
            logger.error("Error message", ex);
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }
}
