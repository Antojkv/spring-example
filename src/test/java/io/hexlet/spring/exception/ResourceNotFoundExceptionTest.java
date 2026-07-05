package io.hexlet.spring.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceNotFoundExceptionTest {

    @Test
    void testConstructor_shouldSetMessage() {
        String message = "User not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);
        
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void testConstructor_shouldWorkWithDifferentMessages() {
        String message = "Post with id 1 not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);
        
        assertThat(exception.getMessage()).isEqualTo(message);
    }
}
