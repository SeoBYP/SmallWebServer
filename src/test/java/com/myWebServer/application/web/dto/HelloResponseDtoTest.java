package com.myWebServer.application.web.dto;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class HelloResponseDtoTest
{
    @Test
    public void Lubbock_Function_Test(){
        //Given
        String name = "text";
        int amount = 100;

        // When
        HelloResponseDto dto = new HelloResponseDto(name,amount);

        // Then
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);
    }
}
