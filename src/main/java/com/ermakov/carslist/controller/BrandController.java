package com.ermakov.carslist.controller;

import com.ermakov.carslist.model.Brand;
import com.ermakov.carslist.service.BrandService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/brands")
@Validated
public class BrandController {
  private final BrandService brandService;

  public BrandController(BrandService brandService) {
    this.brandService = brandService;
  }

  @PostMapping
  @ApiResponse(responseCode = "200", description = "OK")
  @ApiResponse(responseCode = "400", description = "BAD REQUEST")
  @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Brand> createBrand(@RequestBody @Valid Brand brand) {
    return new ResponseEntity<>(brandService.createBrand(brand), HttpStatus.OK);
  }

  @GetMapping("/{brandId}")
  @ApiResponse(responseCode = "200", description = "OK")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Brand> findBrandById(@PathVariable("brandId")
                                             @NotNull @Positive Long brandId) {
    return new ResponseEntity<>(brandService.getBrand(brandId), HttpStatus.OK);
  }

  @PutMapping("/{brandId}")
  @ApiResponse(responseCode = "200", description = "OK")
  @ApiResponse(responseCode = "400", description = "BAD REQUEST")
  @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Brand> editBrand(@PathVariable("brandId")
                                         @NotNull @Positive Long brandId,
                                         @RequestBody @Valid Brand brand) {
    var result = brandService.editBrand(brandId, brand);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @DeleteMapping("/{brandId}")
  @ApiResponse(responseCode = "204", description = "NO_CONTENT")
  @ApiResponse(responseCode = "400", description = "BAD REQUEST")
  @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> deleteBrand(@PathVariable("brandId")
                               @NotNull @Positive Long brandId) {
    brandService.deleteBrand(brandId);
   return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
