package com.autowhouse.loginservice.data.mapper;

import com.autowhouse.loginservice.data.dto.DetailsCodeDTO;
import com.autowhouse.loginservice.data.dto.SignInDTO;
import com.autowhouse.loginservice.data.dto.VerificationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {



    @Mapping(target = "userName", source = "signInDTO.userName")
    @Mapping(target = "password", source = "signInDTO.password")
    DetailsCodeDTO toDetailsCodeDTO(SignInDTO signInDTO, int code);

    @Mapping(target = "code",source = "detailsCodeDTO.code")
    @Mapping(target = "email", source ="detailsCodeDTO.userName")
    VerificationDTO toVerificationDTO(DetailsCodeDTO detailsCodeDTO);
}
