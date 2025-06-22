package com.techdgnep.login.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.techdgnep.login.DataModel.External.CodeEntry;
import com.techdgnep.login.DataModel.Database.FinalUser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Manager implements UserImpl {
    private final int MAX_ENTRIES;
    private final long TIMEOUT_MILLI;
    private final ConcurrentHashMap<String, CodeEntry> codeMap;
    private final ConcurrentHashMap<String,FinalUser> userMap;
    private final UserRepository repo;

    @Autowired
    public Manager(UserRepository repo){
        this.repo = repo;
        this.MAX_ENTRIES = 5;
        this.TIMEOUT_MILLI = 5*60*1000;
        this.codeMap = new ConcurrentHashMap<>();
        this.userMap = new ConcurrentHashMap<>();
    }

    public boolean InsertUser(FinalUser user,int code){
        codeMap.put(user.getEmail(), new CodeEntry(code));
        userMap.put(user.getEmail(),user);
        return true;
    }

    public FinalUser checkEntry(String mail,int code) throws Exception {
        CodeEntry systemCode = codeMap.get(mail);
        if(systemCode==null){
            throw new Exception("User Not found");
        }
        systemCode.incrementAttempts();
        if((System.currentTimeMillis()-systemCode.getCreatedAt())>TIMEOUT_MILLI){
            Remove(mail);
            throw new Exception("Timeout Reached");
        }

        if(systemCode.getAttempts()>MAX_ENTRIES){
            Remove(mail);
            throw new Exception("Max number of Entries reached");
        }
        if(code == systemCode.getCode()){
            FinalUser returnUser = userMap.get(mail);
            Remove(mail);
            return returnUser;
        }else return null;
    }

    private void Remove(String mail){
        codeMap.remove(mail);
        userMap.remove(mail);
    }

    public String hash(String input) throws RuntimeException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexPass = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) hexPass.append('0');
            hexPass.append(hex);
        }
        return hexPass.toString();
    }

    @Override
    @Transactional
    public Long Save(FinalUser user){
        try{
          FinalUser repoUser = repo.save(user);
          return user.getRegisterId();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return -1L;
        }
    }
}
