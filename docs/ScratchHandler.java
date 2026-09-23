package com.add2num.lab;

import com.add2num.core.MyBigNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Scratch draft for validating the LAB2.1 rules against the Add2Num domain.
 * This file is a learning artefact and is not part of the production source set.
 */
@RestController
@RequestMapping("/api/additions")
public class ScratchHandler {

    private final MyBigNumber myBigNumber;

    public ScratchHandler(MyBigNumber myBigNumber) {
        this.myBigNumber = myBigNumber;
    }

    @PostMapping
    public ResponseEntity<AdditionResponse> add(
            @Valid @RequestBody AdditionRequest request) {
        String result = myBigNumber.sum(request.number1(), request.number2());
        AdditionResponse response = new AdditionResponse(
                request.number1(), request.number2(), result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public record AdditionRequest(
            @NotBlank @Pattern(regexp = "\\d+") String number1,
            @NotBlank @Pattern(regexp = "\\d+") String number2) {
    }

    public record AdditionResponse(String number1, String number2, String result) {
    }
}
