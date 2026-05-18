package com.re.trans_route.mapper;

import com.re.trans_route.dto.RegisterDTO;
import com.re.trans_route.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "fullName", target = "userName")
    @Mapping(target = "passwordHash", ignore = true)
    User toEntity(RegisterDTO userDTO);
}
