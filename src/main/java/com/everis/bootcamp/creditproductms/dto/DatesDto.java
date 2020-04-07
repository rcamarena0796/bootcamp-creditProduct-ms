package com.everis.bootcamp.creditproductms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatesDto {

  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date startDate;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date endDate;
}
