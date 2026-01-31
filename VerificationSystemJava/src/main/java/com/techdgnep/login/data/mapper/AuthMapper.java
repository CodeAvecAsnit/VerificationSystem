package com.techdgnep.login.data.mapper;

import com.techdgnep.login.data.dto.DetailsCodeDTO;
import com.techdgnep.login.data.dto.SignInDTO;
import com.techdgnep.login.data.dto.VerificationDTO;
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
