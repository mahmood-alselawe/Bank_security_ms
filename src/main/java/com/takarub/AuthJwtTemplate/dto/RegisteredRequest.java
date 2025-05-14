package com.takarub.AuthJwtTemplate.dto;

import com.takarub.AuthJwtTemplate.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisteredRequest {


    @NotBlank(message = "First name cannot be blank")
    private String firstName;
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;
    @Size(min = 6, message = "Password should be at least 6 characters long")
    private String passWord;
    private String phoneNumber;
//    private Role role;


}
// here we should to valid like notNull Email Password all thank